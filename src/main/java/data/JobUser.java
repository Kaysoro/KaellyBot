package data;

import enums.Job;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by steve on 12/11/2016.
 */
public class JobUser extends ObjectUser {

    private final static Logger LOG = LoggerFactory.getLogger(JobUser.class);
    private static final String JOB_PREFIX = "job";
    private static final int NUMBER_FIELD = 3;
    private static MultiKeySearch<JobUser> jobs;
    private Job job;

    public JobUser(long idUser, ServerDofus server, Job job, int level){
        this.job = job;
        if (level > 200)
            level = 200;
        this.level = level;
        this.idUser = idUser;
        this.server = server;
    }

    public synchronized void setLevel(int level){
        if (level > 200)
            level = 200;
        this.level = level;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement;
            if (level > 0) {
                preparedStatement = connection.prepareStatement(
                        "UPDATE Job_User SET level = ?"
                                + "WHERE name_job = ? AND id_user = ? AND server_dofus = ?;");
                preparedStatement.setInt(1, level);
                preparedStatement.setString(2, job.getName());
                preparedStatement.setString(3, String.valueOf(idUser));
                preparedStatement.setString(4, server.getName());
            }
            else {
                preparedStatement = connection.prepareStatement(
                        "DELETE FROM Job_User WHERE name_job = ? AND id_user = ? AND server_dofus = ?;");
                preparedStatement.setString(1, job.getName());
                preparedStatement.setString(2, String.valueOf(idUser));
                preparedStatement.setString(3, server.getName());
                getJobs().remove(idUser, server, job);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e, ClientConfig.DISCORD().getUserByID(idUser));
            LOG.error("setLevel", e);
        }
    }

    /**
     * Ajoute à la base de donnée l'objet si celui-ci n'y est pas déjà.
     */
    public synchronized void addToDatabase(){

        if (! getJobs().containsKeys(idUser, server, job) && level > 0) {
            getJobs().add(this, idUser, server, job);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Job_User(name_job, id_user, server_dofus, level) VALUES(?, ?, ?, ?);");
                preparedStatement.setString(1, job.getName());
                preparedStatement.setString(2, String.valueOf(idUser));
                preparedStatement.setString(3, server.getName());
                preparedStatement.setInt(4, level);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Reporter.report(e, ClientConfig.DISCORD().getUserByID(idUser));
                LOG.error("addToDatabase", e);
            }
        }
    }

    private Job getJob() {
        return job;
    }

    private static synchronized MultiKeySearch<JobUser> getJobs(){
        if(jobs == null){
            jobs = new MultiKeySearch<>(NUMBER_FIELD);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement(
                        "SELECT id_user, server_dofus, name_job, level FROM Job_User;");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()) {
                    Long idUser = resultSet.getLong("id_user");
                    ServerDofus server = ServerDofus.getServersMap().get(resultSet.getString("server_dofus"));
                    Job job = Job.getJob(resultSet.getString("name_job"));
                    int level = resultSet.getInt("level");
                    jobs.add(new JobUser(idUser, server, job, level), idUser, server, job);
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error("getJobs", e);
            }
        }

        return jobs;
    }

    /**
     * @param user Joueur de la guilde
     * @param server Serveur dofus
     * @return Liste des résultats de la recherche
     */
    public static List<EmbedObject> getJobsFromUser(IUser user, ServerDofus server, IGuild guild, Language lg){
        List<JobUser> result = getJobs().get(user.getLongID(), server, null);
        result.sort(JobUser::compare);
        List<EmbedObject> embed = new ArrayList<>();

        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle(Translator.getLabel(lg, "job.user").replace("{user}", user.getDisplayName(guild)));
        builder.withThumbnail(user.getAvatarURL());
        builder.withColor(new Random().nextInt(16777216));

        if (! result.isEmpty()) {
            StringBuilder st = new StringBuilder();
            for (JobUser jobUser : result)
                st.append(jobUser.job.getLabel(lg)).append(" : ").append(jobUser.level).append("\n");
            builder.appendField(Translator.getLabel(lg, "job.jobs"), st.toString(), true);
        }
        else
            builder.withDescription(Translator.getLabel(lg, "job.empty"));
        builder.withFooterText(server.getName());
        embed.add(builder.build());
        return embed;
    }

    /**
     * @param users Joueurs de la guilde
     * @param server Serveur dofus
     * @param jobs Métiers
     * @param level Niveau; si ingérieur à 0, filtre ignoré
     * @return Liste des résultats de la recherche
     */
    public static List<EmbedObject> getJobsFromFilters(List<IUser> users, ServerDofus server, Set<Job> jobs,
                                                             int level, IGuild guild, Language lg){
        List<JobUser> result = new ArrayList<>();
        for(IUser user : users)
            if (! user.isBot()){
                for(Job job : jobs) {
                    List<JobUser> potentials = getJobs().get(user.getLongID(), server, job);
                    if (level > 0) {
                        for (JobUser potential : potentials)
                            if (potential.getLevel() >= level)
                                result.add(potential);
                    } else
                        result.addAll(potentials);
                }
            }
        result.sort(JobUser::compare);
        return getPlayersList(result, guild, server, lg, JOB_PREFIX);
    }

    @Override
    protected String displayLine(IGuild guild, Language lg) {
        IUser user = ClientConfig.DISCORD().getUserByID(idUser);
        return EmojiManager.getEmojiForPresence(user.getPresence().getStatus()) + " "
                + job.getLabel(lg) + ", " + level + " : **"
                + user.getDisplayName(guild) + "**\n";
    }

    /**
     * if JobUser instance contains keys
     * @param user User id
     * @param server Dofus server
     * @param job Job (from Job enum)
     * @return true if it contains
     */
    public static boolean containsKeys(long user, ServerDofus server, Job job){
        return getJobs().containsKeys(user, server, job);
    }


    /**
     * getData with all the keys
     * @param user User id
     * @param server Dofus server
     * @param job Job (from Job enum)
     * @return List of JobUser
     */
    public static List<JobUser> get(long user, ServerDofus server, Job job){
        return getJobs().get(user, server, job);
    }

    private static int compare(JobUser jobUser1, JobUser jobUser2){
        return Comparator.comparingInt(JobUser::getLevel).reversed()
                .thenComparing(JobUser::getJob)
                .compare(jobUser1, jobUser2);
    }
}

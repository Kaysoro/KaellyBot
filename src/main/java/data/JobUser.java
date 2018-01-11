package data;

import enums.Job;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientConfig;
import util.Connexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 12/11/2016.
 */
public class JobUser {

    private final static Logger LOG = LoggerFactory.getLogger(JobUser.class);
    private static Map<Triple<Long, ServerDofus, Job>, JobUser> jobs;
    private Job job;
    private long idUser;
    private int level;
    private ServerDofus server;


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
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ClientConfig.setSentryContext(null, ClientConfig.DISCORD().getUserByID(idUser), null,null);
            LOG.error(e.getMessage());
        }
    }

    /**
     * Ajoute à la base de donnée l'objet si celui-ci n'y est pas déjà.
     */
    public synchronized void addToDatabase(){

        if (! getJobs().containsKey(Triple.of(idUser, server, job)) && level > 0) {
            getJobs().put(Triple.of(idUser, server, job), this);
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
                ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(idUser),
                        ClientConfig.DISCORD().getUserByID(idUser), null,null);
                LOG.error(e.getMessage());
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public ServerDofus getServer() {
        return server;
    }

    private static synchronized Map<Triple<Long, ServerDofus, Job>, JobUser> getJobs(){
        if(jobs == null){
            jobs = new ConcurrentHashMap<>();
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
                    jobs.put(Triple.of(idUser, server, job), new JobUser(idUser, server, job, level));
                }
            } catch (SQLException e) {
                ClientConfig.setSentryContext(null, null, null,null);
                LOG.error(e.getMessage());
            }
        }

        return jobs;
    }

    /**
     * Retourne le niveau du métier de l'utilisateur pour un serveur de jeu donné
     * @param idUser ID de l'utilisateur
     * @param server Serveur dofus
     * @param job Métier souhaité
     * @return Un nombre compris entre 1 et 200 (s'il existe), 0 le cas échéant.
     */
    public static int getJobLevel(long idUser, ServerDofus server, Job job){
        if (getJobs().containsKey(Triple.of(idUser, server, job)))
            return getJobs().get(Triple.of(idUser, server, job)).getLevel();
        return 0;
    }
}

package commands;

import data.Constants;
import data.Job;
import data.User;
import discord.Message;
import exceptions.JobNotFoundDiscordException;
import exceptions.LevelNotFoundDiscordException;
import exceptions.TooMuchPossibilitiesDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class JobCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(JobCommand.class);

    public JobCommand(){
        super("job", "(\\s+([\\p{L}|\\W]+|-all)(\\s+\\d{1,3})?)?");
        setUsableInMP(false);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Matcher m = getMatcher(message);
            m.find();
            if (m.group(1) == null){
                StringBuilder st = new StringBuilder("Voici la liste des métiers du jeu Dofus :\n```");
                for(String job : Job.getJobs())
                    st.append("\n- ").append(job);
                st.append("```");
                Message.sendText(message.getChannel(), st.toString());
            }
            else if (!m.group(2).equals("-all")) {
                List<String> jobs = getJob(m.group(2));

                if (jobs.size() == 1) {
                    if (m.group(3) != null) { // setting data
                        User author = User.getUsers().get(message.getGuild().getStringID())
                                .get(message.getAuthor().getStringID());
                        int level = Integer.parseInt(m.group(3).replaceAll("\\W+", ""));
                        if (!author.getJobs().containsKey(jobs.get(0)))
                            new Job(jobs.get(0), level, author).addToDatabase();
                        else
                            author.getJobs().get(jobs.get(0)).setLevel(level);

                        if (author.getJob(jobs.get(0)) > 0)
                            Message.sendText(message.getChannel(), author.getName()
                                    + " est inscrit dans l'annuaire en tant que " + jobs.get(0)
                                    + " niv. " + author.getJob(jobs.get(0)) + ".");
                        else
                            Message.sendText(message.getChannel(), author.getName()
                                    + " n'est plus inscrit dans l'annuaire en tant que " + jobs.get(0) + ".");
                    } else { // Consultation
                        Map<String, User> users = User.getUsers().get(message.getGuild().getStringID());
                        List<User> artisans = new ArrayList<>();

                        for (IUser iUser : message.getGuild().getUsers())
                            if (users.get(iUser.getStringID()).getJob(jobs.get(0)) > 0)
                                artisans.add(users.get(iUser.getStringID()));

                        artisans.sort((User o1, User o2)->{
                                if (o2.getJob(jobs.get(0)) != o1.getJob(jobs.get(0)))
                                    return o2.getJob(jobs.get(0)) - o1.getJob(jobs.get(0));
                                return o1.getName().compareTo(o2.getName());
                            }
                        );

                        StringBuilder st = new StringBuilder();

                        if (!artisans.isEmpty()) {
                            st.append("Voici l'annuaire des ").append(jobs.get(0))
                                    .append("s de ").append(message.getGuild().getName()).append(" :\n```");

                            for (User user : artisans) {
                                st.append("\n").append(user.getName());
                                for (int i = user.getName().length(); i < (Constants.nicknameLimit + 10); i++)
                                    st.append(" ");
                                String level = String.valueOf(user.getJob(jobs.get(0)));
                                for (int i = level.length(); i < 3; i++)
                                    st.append(" ");
                                st.append(level);
                            }
                            st.append("```");
                        } else
                            st.append("Aucun ").append(jobs.get(0)).append(" n'est inscrit à l'annuaire.");

                        Message.sendText(message.getChannel(), st.toString());
                    }
                } else if (jobs.size() > 1)
                    new TooMuchPossibilitiesDiscordException().throwException(message, this);
                else
                    new JobNotFoundDiscordException().throwException(message, this);
            }
            else if (m.group(3) != null){ // add all jobs for the user
                User author = User.getUsers().get(message.getGuild().getStringID())
                        .get(message.getAuthor().getStringID());
                int level = Integer.parseInt(m.group(3).replaceAll("\\W+", ""));

                for(String job : Job.getJobs()) {
                    if (!author.getJobs().containsKey(job))
                        new Job(job, level, author).addToDatabase();
                    else
                        author.getJobs().get(job).setLevel(level);
                }
                if (level > 0)
                    Message.sendText(message.getChannel(), author.getName()
                            + " est inscrit dans tous les annuaires.");
                else
                    Message.sendText(message.getChannel(), author.getName()
                            + " n'est plus inscrit des annuaires.");
            }
            else
                new LevelNotFoundDiscordException().throwException(message, this);
        }

        return false;
    }

    private List<String> getJob(String nameProposed){
        nameProposed = Normalizer.normalize(nameProposed, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        nameProposed = nameProposed.replaceAll("\\W+", "");
        List<String> jobs = new ArrayList<>();

        for(String job : Job.getJobs())
            if (Normalizer.normalize(job, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase().replaceAll("\\W+", "").startsWith(nameProposed))
                jobs.add(job);
        return jobs;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** renvoie l'annuaire des artisans d'un métier.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + "` : renvoie la liste des métiers du jeu Dofus."
                + "\n" + prefixe + "`"  + name + " `*`métier`* : renvoie l'annuaire des artisans pour ce métier."
                + "\n" + prefixe + "`"  + name + " `*`métier niveau`* : vous ajoute à l'annuaire du métier correspondant." +
                " Si vous indiquez 0, vous êtes supprimé de l'annuaire pour ce métier."
                + "\n" + prefixe + "`"  + name + " -all `*`niveau`* : vous ajoute à l'annuaire pour tous les métiers correspondants." +
                " Si vous indiquez 0, vous êtes supprimé de chaque annuaire.\n";
    }
}

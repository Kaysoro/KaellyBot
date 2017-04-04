package commands;

import data.Constants;
import data.Guild;
import data.Job;
import data.User;
import discord.Message;
import exceptions.InDeveloppmentException;
import exceptions.JobNotFoundException;
import exceptions.LevelNotFoundException;
import exceptions.TooMuchPossibilitiesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.Discord4J;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class JobCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(JobCommand.class);

    public JobCommand(){
        super(Pattern.compile("job"),
        Pattern.compile("^(" + Constants.prefixCommand + "job)(\\s+(\\p{L}+|-all)(\\s+\\d{1,3})?)?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            if (m.group(2) == null){
                StringBuilder st = new StringBuilder("Voici la liste des métiers du jeu Dofus :\n```");
                for(String job : Job.getJobs())
                    st.append("\n- " + job);
                st.append("```");
                Message.send(message.getChannel(), st.toString());
            }
            else if (!m.group(3).equals("-all")) {
                List<String> jobs = getJob(m.group(3));

                if (jobs.size() == 1) {
                    if (m.group(4) != null) { // setting data
                        User author = User.getUsers().get(message.getGuild().getID())
                                .get(message.getAuthor().getID());
                        int level = Integer.parseInt(m.group(4).replaceAll("\\W+", ""));
                        if (!author.getJobs().containsKey(jobs.get(0)))
                            new Job(jobs.get(0), level, author).addToDatabase();
                        else
                            author.getJobs().get(jobs.get(0)).setLevel(level);

                        if (author.getJob(jobs.get(0)) > 0)
                            Message.send(message.getChannel(), author.getName()
                                    + " est inscrit dans l'annuaire en tant que " + jobs.get(0)
                                    + " niv. " + author.getJob(jobs.get(0)) + ".");
                        else
                            Message.send(message.getChannel(), author.getName()
                                    + " n'est plus inscrit dans l'annuaire en tant que " + jobs.get(0) + ".");
                    } else { // Consultation
                        Map<String, User> users = User.getUsers().get(message.getGuild().getID());
                        List<User> artisans = new ArrayList<User>();

                        for (IUser iUser : message.getGuild().getUsers())
                            if (users.get(iUser.getID()).getJob(jobs.get(0)) > 0)
                                artisans.add(users.get(iUser.getID()));

                        artisans.sort(new Comparator<User>() {
                            @Override
                            public int compare(User o1, User o2) {
                                if (o2.getJob(jobs.get(0)) != o1.getJob(jobs.get(0)))
                                    return o2.getJob(jobs.get(0)) - o1.getJob(jobs.get(0));
                                return o1.getName().compareTo(o2.getName());
                            }
                        });

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

                        Message.send(message.getChannel(), st.toString());
                    }
                } else if (jobs.size() > 1)
                    new TooMuchPossibilitiesException().throwException(message, this);
                else
                    new JobNotFoundException().throwException(message, this);
            }
            else if (m.group(4) != null){ // add all jobs for the user
                User author = User.getUsers().get(message.getGuild().getID())
                        .get(message.getAuthor().getID());
                int level = Integer.parseInt(m.group(4).replaceAll("\\W+", ""));

                for(String job : Job.getJobs()) {
                    if (!author.getJobs().containsKey(job))
                        new Job(job, level, author).addToDatabase();
                    else
                        author.getJobs().get(job).setLevel(level);
                }
                if (level > 0)
                    Message.send(message.getChannel(), author.getName()
                            + " est inscrit dans tous les annuaires.");
                else
                    Message.send(message.getChannel(), author.getName()
                            + " n'est plus inscrit des annuaires.");
            }
            else
                new LevelNotFoundException().throwException(message, this);
        }

        return false;
    }

    public List<String> getJob(String nameProposed){
        nameProposed = Normalizer.normalize(nameProposed, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        nameProposed = nameProposed.replaceAll("\\W+", "");
        List<String> jobs = new ArrayList<String>();

        for(String job : Job.getJobs())
            if (Normalizer.normalize(job, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase().startsWith(nameProposed))
                jobs.add(job);
        return jobs;
    }

    @Override
    public boolean isUsableInMP() {
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "job** renvoit l'annuaire des artisans d'un métier.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "job` : renvoit la liste des métiers du jeu Dofus."
                + "\n`" + Constants.prefixCommand + "job `*`métier`* : renvoit l'annuaire des artisans pour ce métier."
                + "\n`" + Constants.prefixCommand + "job `*`métier niveau`* : vous ajoute à l'annuaire du métier correspondant." +
                " Si vous indiquez 0, vous êtes supprimé de l'annuaire pour ce métier."
                + "\n`" + Constants.prefixCommand + "job -all `*`niveau`* : vous ajoute à l'annuaire pour tous les métiers correspondants." +
                " Si vous indiquez 0, vous êtes supprimé de chaque annuaire.\n";
    }
}

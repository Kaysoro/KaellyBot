package commands;

import data.Constants;
import data.Guild;
import data.Job;
import data.User;
import discord.Message;
import exceptions.InDeveloppmentException;
import exceptions.JobNotFoundException;
import exceptions.TooMuchPossibilitiesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class JobCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(JobCommand.class);

    public JobCommand(){
        super(Pattern.compile("job"),
        Pattern.compile("^(" + Constants.prefixCommand + "job)\\s+(\\w+)(\\s+\\d{1,3})?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){

            List<String> jobs = getJob(m.group(2));

            if (jobs.size() == 1) {
                if (m.group(3) != null) { // setting data
                    User author = User.getUsers().get(message.getGuild().getID())
                            .get(message.getAuthor().getID());
                    int level = Integer.parseInt(m.group(3).replaceAll("\\W+", ""));
                    if (! author.getJobs().containsKey(jobs.get(0)))
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
                }
                else { // Consultation
                    //TODO
                }

                Message.send(message.getChannel(), jobs.get(0).toString());
            }
            else if(jobs.size() > 1)
                new TooMuchPossibilitiesException().throwException(message, this);
            else
                new JobNotFoundException().throwException(message, this);
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
    public String help() {
        return "**" + Constants.prefixCommand + "job** renvoit l'annuaire des artisans pour ce métier.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "job` : renvoit l'annuaire des artisans pour ce métier."
                + "\n`" + Constants.prefixCommand + "job `*`niveau`* : vous ajoute à l'annuaire du métier correspondant." +
                "Si vous indiquez 0, vous êtes supprimé de l'annuaire pour ce métier.\n";
    }
}

package commands.classic;

import commands.model.AbstractCommand;
import data.Constants;
import data.JobUser;
import enums.Job;
import enums.Language;
import exceptions.BasicDiscordException;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import util.Message;
import exceptions.TooMuchDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class JobCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(JobCommand.class);
    private DiscordException tooMuchJobs;
    private DiscordException notFoundJob;
    private DiscordException notFoundLevel;

    public JobCommand(){
        super("job", "(\\s+([\\p{L}|\\W]+|-all)(\\s+\\d{1,3})?)?");
        setUsableInMP(false);
        tooMuchJobs = new TooMuchDiscordException("job", true);
        notFoundJob = new NotFoundDiscordException("job");
        notFoundLevel = new NotFoundDiscordException("level");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Language lg = Translator.getLanguageFrom(message.getChannel());
            Matcher m = getMatcher(message);
            m.find();
            // TODO
            new BasicDiscordException("exception.basic.in_developpment");
        }
        return false;
    }

    private List<String> getJob(Language lg, String nameProposed){
        nameProposed = Normalizer.normalize(nameProposed, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        nameProposed = nameProposed.replaceAll("\\W+", "");
        List<String> jobs = new ArrayList<>();

        for(Job job : Job.values())
            if (Normalizer.normalize(job.getLabel(lg), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase().replaceAll("\\W+", "").startsWith(nameProposed))
                jobs.add(job.getName());
        return jobs;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "job.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + "` : " + Translator.getLabel(lg, "job.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " `*`job`* : " + Translator.getLabel(lg, "job.help.detailed.2")
                + "\n" + prefixe + "`"  + name + " `*`job level`* : " + Translator.getLabel(lg, "job.help.detailed.3")
                + "\n" + prefixe + "`"  + name + " -all `*`level`* : " + Translator.getLabel(lg, "job.help.detailed.4") + "\n";
    }
}

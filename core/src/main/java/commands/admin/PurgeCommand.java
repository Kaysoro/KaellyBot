package commands.admin;

import commands.model.AbstractCommand;
import enums.Language;
import stats.CommandStatistics;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;
import util.Translator;
import java.time.Instant;
import java.util.regex.Matcher;

/**
 * Created by steve on 23/12/2017.
 */
public class PurgeCommand extends AbstractCommand {

    public PurgeCommand(){
        super("purge","(\\s+-cmd)");
        setAdmin(true);
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        if (m.group(1).matches("\\s+-cmd")){
            Message.sendFile(message.getChannel(), CommandStatistics.purge(),
                    "purge -cmd : " + Instant.now() + ".sql");
        }
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "purge.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " -cmd` : " + Translator.getLabel(lg, "stat.help.detailed.1") + "\n";
    }
}

package commands.classic;

import commands.model.AbstractCommand;
import enums.Language;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;
import util.Translator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;

/**
 * Created by Kaysoro on 24/05/2019.
 */
public class PingCommand extends AbstractCommand {

    public PingCommand() {
        super("ping", "");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        Message.sendText(message.getChannel(), ChronoUnit.MILLIS.between(Instant.now(), message.getTimestamp()) + "ms!");
    }

    @Override
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "ping.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return help(lg, prefix);
    }
}

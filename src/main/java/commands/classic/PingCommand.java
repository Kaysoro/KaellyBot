package commands.classic;

import commands.model.AbstractCommand;
import discord4j.core.object.entity.Message;
import enums.Language;
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
    public void request(Message message, Matcher m, Language lg) {
        message.getChannel().flatMap(chan -> chan
                .createMessage(Math.abs(ChronoUnit.MILLIS.between(Instant.now(), message.getTimestamp())) + "ms!"))
                .subscribe();
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

package commands.config;

import commands.model.AbstractLegacyCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import enums.Language;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class CommandCommand extends AbstractLegacyCommand {

    public CommandCommand(){
        super("cmd","\\s+([\\w|-]+)\\s+(on|off|0|1|true|false)");
        setUsableInMP(false);
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        message.getChannel().flatMap(chan -> chan
                        .createMessage(Translator.getLabel(lg, "command.request")))
                .subscribe();
    }

    @Override
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "command.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return Translator.getLabel(lg, "command.request");
    }
}

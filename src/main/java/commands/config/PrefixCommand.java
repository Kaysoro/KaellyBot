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
public class PrefixCommand extends AbstractLegacyCommand {

    public PrefixCommand(){
        super("prefix","\\s+(.+)");
        setUsableInMP(false);
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        message.getChannel().flatMap(chan -> chan
                        .createMessage(Translator.getLabel(lg, "prefix.request")))
                .subscribe();
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "prefix.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return Translator.getLabel(lg, "prefix.request");
    }
}

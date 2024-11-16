package commands.classic;

import commands.model.AbstractLegacyCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import enums.Language;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 08/02/2018
 */
public class AlignmentCommand extends AbstractLegacyCommand {

    public AlignmentCommand(){
        super("align", "(.*)");
        setUsableInMP(false);
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        message.getChannel().flatMap(chan -> chan
                        .createMessage(Translator.getLabel(lg, "align.request")))
                .subscribe();
    }

    @Override
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "align.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return Translator.getLabel(lg, "align.request");
    }
}
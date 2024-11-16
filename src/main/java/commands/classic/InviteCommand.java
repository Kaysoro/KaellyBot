package commands.classic;

import commands.model.AbstractLegacyCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import enums.Language;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by Kaysoro on 20/05/2019.
 */
public class InviteCommand extends AbstractLegacyCommand {

    public InviteCommand() {
        super("invite", "");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        message.getChannel().flatMap(chan -> chan
                        .createMessage(Translator.getLabel(lg, "invite.request")))
                .subscribe();
    }

    @Override
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "invite.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return Translator.getLabel(lg, "invite.request");
    }
}

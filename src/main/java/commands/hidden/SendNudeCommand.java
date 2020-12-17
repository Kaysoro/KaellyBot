package commands.hidden;

import commands.model.AbstractCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import enums.Language;
import enums.Nude;
import exceptions.BasicDiscordException;
import util.Translator;

import java.awt.*;
import java.util.regex.Matcher;

/**
 * Created by Kaysoro on 07/01/2019.
 */
public class SendNudeCommand extends AbstractCommand {

    private final static Color PINK_COLOR = new Color(255, 32, 128);

    public SendNudeCommand() {
        super("sendnude", "");
        setHidden(true);
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        if (message.getChannel().blockOptional().map(chan -> chan instanceof PrivateChannel).orElse(false)
                || message.getChannel().blockOptional().map(chan -> ((TextChannel) chan).isNsfw()).orElse(false)){
            message.getChannel().flatMap(chan -> chan.createEmbed(spec -> {
                spec.setTitle(Translator.getLabel(lg, "sendnude.title"))
                        .setFooter(Translator.getLabel(lg, "sendnude.author")
                                .replace("{author}", Nude.MOAM.getAuthor())
                                .replace("{position}", "1")
                                .replace("{number}", "1"), null)
                        .setImage(Nude.MOAM.getImage());
            })).subscribe();
        }
        else // Exception NSFW
            BasicDiscordException.NO_NSFW_CHANNEL.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "sendnude.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe);
    }
}

package commands.hidden;

import commands.model.AbstractCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Color;
import enums.Language;
import enums.Nude;
import exceptions.BasicDiscordException;
import util.Translator;

import java.util.Random;
import java.util.regex.Matcher;

/**
 * Created by Kaysoro on 07/01/2019.
 */
public class SendNudeCommand extends AbstractCommand {

    private static final Random RANDOM = new Random();

    public SendNudeCommand() {
        super("sendnude", "");
        setHidden(true);
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        if (message.getChannel().blockOptional().map(chan -> chan instanceof PrivateChannel).orElse(false)
                || message.getChannel().blockOptional().map(chan -> ((TextChannel) chan).isNsfw()).orElse(false)){
            message.getChannel().flatMap(chan -> chan.createEmbed(spec -> {
                int position = RANDOM.nextInt(Nude.values().length);
                Nude nude = Nude.values()[position];

                spec.setTitle(Translator.getLabel(lg, "sendnude.title"))
                        .setColor(Color.PINK)
                        .setFooter(Translator.getLabel(lg, "sendnude.author")
                                .replace("{author}", nude.getAuthor())
                                .replace("{position}", String.valueOf(position + 1))
                                .replace("{number}", String.valueOf(Nude.values().length)), null)
                        .setImage(nude.getImage());
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

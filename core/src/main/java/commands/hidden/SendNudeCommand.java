package commands.hidden;

import commands.model.AbstractCommand;
import enums.Language;
import enums.Nude;
import exceptions.BasicDiscordException;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import util.Message;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by Kaysoro on 07/01/2019.
 */
public class SendNudeCommand extends AbstractCommand {

    private final static int PINK_COLOR = 16720000;

    public SendNudeCommand() {
        super("sendnude", "");
        setHidden(true);
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        if (message.getChannel().isPrivate() || message.getChannel().isNSFW()) {
            EmbedBuilder builder = new EmbedBuilder();

            builder.withTitle(Translator.getLabel(lg, "sendnude.title"))
                    .withColor(PINK_COLOR)
                    .withFooterText(Translator.getLabel(lg, "sendnude.author")
                            .replace("{author}", Nude.MOAM.getAuthor())
                            .replace("{position}", "1")
                            .replace("{number}", "1"))
                    .withImage(Nude.MOAM.getImage());
            Message.sendEmbed(message.getChannel(), builder.build());
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

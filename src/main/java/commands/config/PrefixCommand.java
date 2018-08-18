package commands.config;

import commands.model.AbstractCommand;
import data.Constants;
import data.Guild;
import enums.Language;
import exceptions.AdvancedDiscordException;
import exceptions.BasicDiscordException;
import sx.blah.discord.util.DiscordException;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class PrefixCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(PrefixCommand.class);
    private exceptions.DiscordException prefixeOutOfBounds;

    public PrefixCommand(){
        super("prefix","\\s+(.+)");
        setUsableInMP(false);
        prefixeOutOfBounds = new AdvancedDiscordException("exception.advanced.prefix_out_of_bound",
                new String[]{String.valueOf(Constants.prefixeLimit)}, new Boolean[]{false});
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        if (isUserHasEnoughRights(message)) {
            String newPrefix = m.group(1).trim();

            if (newPrefix.length() >= 1 && newPrefix.length() <= Constants.prefixeLimit) {
                Guild.getGuild(message.getGuild()).setPrefix(newPrefix);
                Message.sendText(message.getChannel(), Translator.getLabel(lg, "prefix.request.1")
                    .replace("{prefix}", getPrefixMdEscaped(message)));

                try {
                    Message.sendText(message.getGuild().getOwner().getOrCreatePMChannel(),
                            Translator.getLabel(lg, "prefix.request.2")
                                    .replace("{prefix}", getPrefixMdEscaped(message))
                                    .replace("{guild.name}", message.getGuild().getName()));
                } catch (DiscordException e){
                    LOG.warn("request", "Impossible de contacter l'administrateur de la guilde ["
                            + message.getGuild().getName() + "].");
                }
            }
            else
                prefixeOutOfBounds.throwException(message, this, lg);
        }
        else
            BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "prefix.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`prefix`* : " + Translator.getLabel(lg, "prefix.help.detailed")
                .replace("{prefixeLimit}", String.valueOf(Constants.prefixeLimit)) + "\n";
    }
}

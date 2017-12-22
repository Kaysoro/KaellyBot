package commands.config;

import commands.model.AbstractCommand;
import data.Constants;
import data.Guild;
import data.User;
import enums.Language;
import exceptions.AdvancedDiscordException;
import exceptions.BasicDiscordException;
import exceptions.DiscordException;
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
    private DiscordException noEnoughRights;
    private DiscordException prefixeOutOfBounds;

    public PrefixCommand(){
        super("prefix","\\s+(.+)");
        setUsableInMP(false);
        noEnoughRights = new BasicDiscordException("exception.basic.no_enough_rights");
        prefixeOutOfBounds = new AdvancedDiscordException("exception.advanced.prefix_out_of_bound",
                new String[]{String.valueOf(Constants.prefixeLimit)}, new Boolean[]{false});
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Language lg = Translator.getLanguageFrom(message.getChannel());
            User author = User.getUser(message.getGuild(), message.getAuthor());

            if (author.getRights() >= User.RIGHT_MODERATOR) {
                Matcher m = getMatcher(message);
                m.find();
                String newPrefix = m.group(1).trim();

                if (newPrefix.length() >= 1 && newPrefix.length() <= Constants.prefixeLimit) {
                    Guild.getGuild(message.getGuild()).setPrefixe(newPrefix);
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "prefix.request.1")
                        .replace("{prefix}", getPrefixMdEscaped(message)));
                    Message.sendText(message.getGuild().getOwner().getOrCreatePMChannel(),
                            Translator.getLabel(lg, "prefix.request.2")
                                    .replace("{prefix}", getPrefixMdEscaped(message))
                                    .replace("{guild.name}", message.getGuild().getName()));
                    return true;
                }
                else
                    prefixeOutOfBounds.throwException(message, this, lg);
            }
            else
                noEnoughRights.throwException(message, this, lg);
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "prefix.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " `*`prefix`* : " + Translator.getLabel(lg, "prefix.help.detailed")
                .replace("{prefixeLimit}", String.valueOf(Constants.prefixeLimit)) + "\n";
    }
}

package commands;

import data.Constants;
import data.Guild;
import data.User;
import discord.Message;
import exceptions.NotEnoughRightsDiscordException;
import exceptions.PrefixeOutOfBoundsDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class PrefixCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(PrefixCommand.class);

    public PrefixCommand(){
        super("prefix","\\s+(.+)");
        setUsableInMP(false);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            User author = User.getUsers().get(message.getGuild().getStringID()).get(message.getAuthor().getStringID());

            if (author.getRights() >= User.RIGHT_MODERATOR) {
                Matcher m = getMatcher(message);
                m.find();
                String newPrefix = m.group(1).trim();

                if (newPrefix.length() >= 1 && newPrefix.length() <= Constants.prefixeLimit) {
                    Guild.getGuilds().get(message.getGuild().getStringID()).setPrefixe(newPrefix);
                    Message.sendText(message.getChannel(), "Changement réussi. Pour invoquer une commande, "
                            + "il faudra désormais utiliser le préfixe *" + getPrefixMdEscaped(message) + "*.");
                    Message.sendText(message.getGuild().getOwner().getOrCreatePMChannel(), "RÉCAPITULATIF : "
                            + "pour invoquer une commande, il faudra désormais utiliser le préfixe *"
                            + getPrefixMdEscaped(message) + "* sur *" + message.getGuild().getName() + "*.");
                    return true;
                }
                else
                    new PrefixeOutOfBoundsDiscordException().throwException(message, this);
            }
            else
                new NotEnoughRightsDiscordException().throwException(message, this);
        }
        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** change le préfixe utilisé pour invoquer une commande. Niveau modérateur minimum requis.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n`" + prefixe + name + " `*`prefixe`* : change le préfixe par celui passé en paramètre. "
                + Constants.prefixeLimit + " maximum.\n";
    }
}

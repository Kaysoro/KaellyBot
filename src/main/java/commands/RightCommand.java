package commands;

import data.ClientConfig;
import data.Constants;
import data.User;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class RightCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(RightCommand.class);

    public RightCommand(){
        super(Pattern.compile("right"),
        Pattern.compile("^(!right)\\s+<@[!|&](\\d+)>\\s+(\\d)$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            //On check si la personne a bien les droits pour exécuter cette commande
            if (User.getUsers().get(message.getGuild().getID())
                    .get(message.getAuthor().getID()).getRights() >= User.RIGHT_MODERATOR) {
                try {
                    if (!m.group(2).equals(message.getAuthor().getID()))
                        User.getUsers().get(message.getGuild().getID())
                            .get(m.group(2)).changeRight(Integer.parseInt(m.group(3)));
                } catch(NullPointerException e){
                    LOG.warn("L'utilisateur <@!" + m.group(2) + "> n'existe pas.");
                }
            } else {
                RequestBuffer.request(() -> {
                    try {
                        new MessageBuilder(ClientConfig.CLIENT())
                                .withChannel(message.getChannel())
                                .withContent("Vous ne pouvez pas changer les droits des autres : il faut être " +
                                        "modérateur ou administrateur pour cela.")
                                .build();
                    } catch (DiscordException e) {
                        LOG.error(e.getErrorMessage());
                    } catch (MissingPermissionsException e) {
                        LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                    }
                    return null;
                });
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String help() {
        return "**!right** permet de changer les droits de quelqu'un d'autre tant qu'il n'est pas plus"
                + " haut que le sien. Nécessite un niveau d'administration 2 (Modérateur) minimum."
                + " Les niveaux sont 0 : Invité, 1 : Membre, 2 : Modérateur, 3 : Administrateur.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`!right `*`@pseudo niveau`* : change le niveau d'administration d'une personne.\n";
    }
}

package commands;

import data.ClientConfig;
import data.Constants;
import data.User;
import exceptions.AutoChangeRightsException;
import exceptions.NotEnoughRightsException;
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
        Pattern.compile("^(" + Constants.prefixCommand + "right)(\\s+<@[!|&]\\d+>)?(\\s+\\d)?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            if (m.group(3) != null) { // Level precised : editing

                if (User.getUsers().get(message.getGuild().getID())
                        .get(message.getAuthor().getID()).getRights() >= User.RIGHT_MODERATOR) {

                    String id = m.group(2).replaceAll("\\s", "");
                    String level = m.group(3).replaceAll("\\s", "");

                    try {
                        if (!m.group(2).equals(message.getAuthor().getID()))
                            User.getUsers().get(message.getGuild().getID())
                                    .get(id).changeRight(Integer.parseInt(level));
                        else {
                            new AutoChangeRightsException().throwException(message, this);
                        }
                    } catch (NullPointerException e) {
                        //TODO add user
                        LOG.warn("L'utilisateur <@!" + id + "> n'existe pas.");
                    }
                } else {
                    new NotEnoughRightsException().throwException(message, this);
                    return false;
                }
                return true;
            } else { // Level is not precised : consulting

            }
        }

        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "right** permet de changer les droits de quelqu'un d'autre tant qu'il n'est pas plus"
                + " haut que le sien. Nécessite un niveau d'administration 2 (Modérateur) minimum."
                + " Les niveaux sont 0 : Invité, 1 : Membre, 2 : Modérateur, 3 : Administrateur.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "right` : donne le niveau d'administration de l'auteur de la requête."
                + "\n`" + Constants.prefixCommand + "right `*`@pseudo`* : donne le niveau d'administration de l'utilisateur ou d'un groupe spécifié."
                + "\n`" + Constants.prefixCommand + "right `*`@pseudo niveau`* : change le niveau d'administration d'un utilisateur ou d'un groupe spécifié.\n";
    }
}

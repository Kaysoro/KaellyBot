package commands;

import data.Constants;
import data.User;
import discord.Message;
import exceptions.AutoChangeRightsException;
import exceptions.BadUseCommandException;
import exceptions.NoRightsForRolesException;
import exceptions.NotEnoughRightsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class RightCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(RightCommand.class);

    public RightCommand(){
        super(Pattern.compile("right"),
        Pattern.compile("^(" + Constants.prefixCommand + "right)(\\s+<@[!|&]?\\d+>)?(\\s+\\d)?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            User author = User.getUsers().get(message.getGuild().getID()).get(message.getAuthor().getID());

            if (m.group(3) != null) { // Level precised : editing

                if (m.group(2) == null){
                    new BadUseCommandException().throwException(message, this);
                    return false;
                }

                String idDecorated = m.group(2).replaceAll("\\s", "");
                String id = idDecorated.replaceAll("\\W", "");
                int level = Integer.parseInt(m.group(3).replaceAll("\\s", ""));

                if (author.getRights() >= User.RIGHT_MODERATOR) {
                    if (idDecorated.matches("<@&\\d+>")){ // Manage Groups
                        IRole role = message.getGuild().getRoleByID(id);

                        for(IUser user : message.getGuild().getUsers()){

                            User target = User.getUsers().get(message.getGuild().getID()).get(user.getID());

                            if (user.getRolesForGuild(message.getGuild()).contains(role)){
                                if ((! target.getId().equals(author.getId())) &&
                                    author.getRights() >= target.getRights())
                                    target.changeRight(level);
                                else
                                    Message.send(message.getChannel(), "Les droits d'administration de "
                                    + target.getName() + " n'ont pu être changés.\n");
                            }
                        }

                        Message.send(message.getChannel(), "Droits d'administration des membres du groupe *"
                                + role.getName() + "* mis au niveau " + level + ".");
                    }
                    else if (idDecorated.matches("<@!?\\d+>")){ // Manage users
                        User target = User.getUsers().get(message.getGuild().getID()).get(id);

                        if (id.equals(message.getAuthor().getID())) {
                            new AutoChangeRightsException().throwException(message, this);
                            return false;
                        }

                        if (author.getRights() < target.getRights()) {
                            new NotEnoughRightsException().throwException(message, this);
                            return false;
                        }

                        target.changeRight(level);
                        Message.send(message.getChannel(), "Droits d'administration de *"
                                + target.getName() + "* mis au niveau " + target.getRights() + ".");
                    }
                } // Not an admin or a moderator
                else {
                    new NotEnoughRightsException().throwException(message, this);
                    return false;
                }
            } else { // Level is not precised : consulting

                if (m.group(2) != null){ // Author want to know specific user's rights.
                    String idDecorated = m.group(2).replaceAll("\\s", "");
                    String id = idDecorated.replaceAll("\\W", "");

                    if (idDecorated.matches("<@!?\\d+>")){
                        User target = User.getUsers().get(message.getGuild().getID()).get(id);
                        Message.send(message.getChannel(), "*" + target.getName()
                                + "* a des droits d'administration de niveau "
                                + target.getRights() + ".");
                    }
                    else
                        new NoRightsForRolesException().throwException(message, this);
                }
                else
                    Message.send(message.getChannel(), "Tu as les droits d'administration de niveau "
                            + author.getRights() + ".");
            }
        }

        return true;
    }

    @Override
    public boolean isUsableInMP() {
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

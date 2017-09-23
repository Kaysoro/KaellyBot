package commands;

import data.User;
import discord.Message;
import exceptions.AutoChangeRightsDiscordException;
import exceptions.BadUseCommandDiscordException;
import exceptions.NoRightsForRolesDiscordException;
import exceptions.NotEnoughRightsDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class RightCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(RightCommand.class);

    public RightCommand(){
        super("right", "(\\s+<@[!|&]?\\d+>)?(\\s+\\d)?");
        setUsableInMP(false);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            User author = User.getUsers().get(message.getGuild().getStringID()).get(message.getAuthor().getStringID());
            Matcher m = getMatcher(message);
            m.find();

            if (m.group(2) != null) { // Level precised : editing

                if (m.group(1) == null){
                    new BadUseCommandDiscordException().throwException(message, this);
                    return false;
                }

                String idDecorated = m.group(1).replaceAll("\\s", "");
                String id = idDecorated.replaceAll("\\W", "");
                int level = Integer.parseInt(m.group(2).replaceAll("\\s", ""));

                if (author.getRights() >= User.RIGHT_MODERATOR) {
                    if (idDecorated.matches("<@&\\d+>")){ // Manage Groups
                        IRole role = message.getGuild().getRoleByID(Long.parseLong(id));

                        for(IUser user : message.getGuild().getUsers()){

                            User target = User.getUsers().get(message.getGuild().getStringID()).get(user.getStringID());

                            if (user.getRolesForGuild(message.getGuild()).contains(role)){
                                if ((! target.getId().equals(author.getId())) &&
                                    author.getRights() >= target.getRights())
                                    target.changeRight(level);
                                else
                                    Message.sendText(message.getChannel(), "Les droits d'administration de "
                                    + target.getName() + " n'ont pu être changés.\n");
                            }
                        }

                        Message.sendText(message.getChannel(), "Droits d'administration des membres du groupe *"
                                + role.getName() + "* mis au niveau " + level + ".");
                    }
                    else if (idDecorated.matches("<@!?\\d+>")){ // Manage users
                        User target = User.getUsers().get(message.getGuild().getStringID()).get(id);

                        if (id.equals(message.getAuthor().getStringID())) {
                            new AutoChangeRightsDiscordException().throwException(message, this);
                            return false;
                        }

                        if (author.getRights() < target.getRights()) {
                            new NotEnoughRightsDiscordException().throwException(message, this);
                            return false;
                        }

                        target.changeRight(level);
                        Message.sendText(message.getChannel(), "Droits d'administration de *"
                                + target.getName() + "* mis au niveau " + target.getRights() + ".");
                    }
                } // Not an admin or a moderator
                else {
                    new NotEnoughRightsDiscordException().throwException(message, this);
                    return false;
                }
            } else { // Level is not precised : consulting

                if (m.group(1) != null){ // Author want to know specific user's rights.
                    String idDecorated = m.group(1).replaceAll("\\s", "");
                    String id = idDecorated.replaceAll("\\W", "");

                    if (idDecorated.matches("<@!?\\d+>")){
                        User target = User.getUsers().get(message.getGuild().getStringID()).get(id);
                        Message.sendText(message.getChannel(), "*" + target.getName()
                                + "* a des droits d'administration de niveau "
                                + target.getRights() + ".");
                    }
                    else
                        new NoRightsForRolesDiscordException().throwException(message, this);
                }
                else
                    Message.sendText(message.getChannel(), "Tu as les droits d'administration de niveau "
                            + author.getRights() + ".");
            }
        }

        return true;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** permet de changer les droits d'un utilisateur dont les droits sont inférieurs aux siens."
                + " Nécessite un niveau d'administration 2 (Modérateur) minimum."
                + " Les niveaux sont 0 : Invité, 1 : Membre, 2 : Modérateur, 3 : Administrateur.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + "` : donne le niveau d'administration de l'auteur de la requête."
                + "\n" + prefixe + "`"  + name + " `*`@pseudo`* : donne le niveau d'administration de l'utilisateur ou d'un groupe spécifié."
                + "\n" + prefixe + "`"  + name + " `*`@pseudo niveau`* : change le niveau d'administration d'un utilisateur ou d'un groupe spécifié.\n";
    }
}

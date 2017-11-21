package commands;

import data.User;
import enums.Language;
import util.ClientConfig;
import util.Message;
import exceptions.AutoChangeRightsDiscordException;
import exceptions.BadUseCommandDiscordException;
import exceptions.NoRightsForRolesDiscordException;
import exceptions.NotEnoughRightsDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import util.Translator;

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
            Language lg = Translator.getLanguageFrom(message.getChannel());
            User author = User.getUser(message.getGuild(), message.getAuthor());
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

                            User target = User.getUser(message.getGuild(), message.getAuthor());

                            if (user.getRolesForGuild(message.getGuild()).contains(role)){
                                if ((! target.getId().equals(author.getId())) &&
                                    author.getRights() >= target.getRights())
                                    target.changeRight(level);
                                else
                                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "right.request.1")
                                    .replace("{name}", target.getName()));
                            }
                        }

                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "right.request.2")
                                .replace("{name}", role.getName())
                                .replace("{right}", String.valueOf(level)));
                    }
                    else if (idDecorated.matches("<@!?\\d+>")){ // Manage users
                        User target = User.getUser(message.getGuild(), ClientConfig.DISCORD().getUserByID(Long.parseLong(id)));

                        if (id.equals(message.getAuthor().getStringID())) {
                            new AutoChangeRightsDiscordException().throwException(message, this);
                            return false;
                        }

                        if (author.getRights() < target.getRights()) {
                            new NotEnoughRightsDiscordException().throwException(message, this);
                            return false;
                        }

                        target.changeRight(level);
                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "right.request.3")
                                .replace("{name}", target.getName())
                                .replace("{right}", String.valueOf(target.getRights())));
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
                        User target = User.getUser(message.getGuild(),
                                ClientConfig.DISCORD().getUserByID(Long.parseLong(id)));
                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "right.request.4")
                                .replace("{name}", target.getName())
                                .replace("{right}", String.valueOf(target.getRights())));
                    }
                    else
                        new NoRightsForRolesDiscordException().throwException(message, this);
                }
                else
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "right.request.5")
                            .replace("{right}", String.valueOf(author.getRights())));
            }
        }

        return true;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "right.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + "` : " + Translator.getLabel(lg, "right.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " `*`@pseudo`* : " + Translator.getLabel(lg, "right.help.detailed.2")
                + "\n" + prefixe + "`"  + name + " `*`@pseudo niveau`* : " + Translator.getLabel(lg, "right.help.detailed.3") + "\n";
    }
}

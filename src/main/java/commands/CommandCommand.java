package commands;

import data.CommandForbidden;
import data.Guild;
import data.User;
import discord.Message;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class CommandCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(CommandCommand.class);

    public CommandCommand(){
        super("cmd","\\s+(\\w+)\\s+(on|off|0|1|true|false)");
        setUsableInMP(false);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            User author = User.getUsers().get(message.getGuild().getStringID()).get(message.getAuthor().getStringID());

            if (author.getRights() >= User.RIGHT_MODERATOR) {
                Guild guild = Guild.getGuilds().get(message.getGuild().getStringID());
                Matcher m = getMatcher(message);
                m.find();
                List<Command> potentialCmds = new ArrayList<>();
                String commandName = m.group(1).trim();
                for (Command command : CommandManager.getCommands())
                    if (command.isPublic() && !command.isAdmin() && command.getName().contains(commandName))
                        potentialCmds.add(command);

                if (potentialCmds.size() == 1){
                    Command command = potentialCmds.get(0);
                    String value = m.group(2);

                    if (command instanceof CommandCommand){
                        Message.sendText(message.getChannel(), "Impossible de désactiver cette commande.");
                        return false;
                    }
                    if (value.matches("false") || value.matches("1") || value.matches("off")){
                        if (! guild.getForbiddenCommands().containsKey(command.getName())) {
                            new CommandForbidden(command, guild).addToDatabase();
                            Message.sendText(message.getChannel(), "La commande *" + commandName
                                    + "* est à présent désactivée.");
                        }
                        else
                            new ForbiddenCommandFoundDiscordException().throwException(message, this);
                    }
                    else if (value.matches("true") || value.matches("0") || value.matches("on")){
                        if (guild.getForbiddenCommands().containsKey(command.getName())) {
                            guild.getForbiddenCommands().get(command.getName()).removeToDatabase();
                            Message.sendText(message.getChannel(), "La commande *" + commandName
                                    + "* est à présent autorisée.");
                        }
                        else
                            new ForbiddenCommandNotFoundDiscordException().throwException(message, this);
                    }
                    else
                        new BadUseCommandDiscordException().throwException(message, this);
                }
                else if (potentialCmds.isEmpty())
                    new CommandNotFoundDiscordException().throwException(message, this);
                else
                    new TooMuchPossibilitiesDiscordException().throwException(message, this);
            }
            else
                new NotEnoughRightsDiscordException().throwException(message, this);
        }
        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** permet d'autoriser ou d'interdire une commande; "
                + "nécessite un niveau d'administration 2 (Modérateur) minimum.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + "*CommandForbidden* true` : autorise l'utilisation de *CommandForbidden*. Fonctionne aussi avec \"on\" et \"0\"."
                + "\n" + prefixe + "`"  + name + "*CommandForbidden* false` : masque *CommandForbidden* et la rend inutilisable. Fonctionne aussi avec \"off\" et \"1\".\n";
    }
}

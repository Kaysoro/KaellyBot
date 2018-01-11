package commands.config;

import commands.CommandManager;
import commands.model.AbstractCommand;
import commands.model.Command;
import data.CommandForbidden;
import data.Guild;
import enums.Language;
import util.Message;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class CommandCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(CommandCommand.class);

    private DiscordException tooMuchCmds;
    private DiscordException notFoundCmd;
    private DiscordException forbiddenCmdFound;
    private DiscordException forbiddenCmdNotFound;
    private DiscordException noEnoughRights;

    public CommandCommand(){
        super("cmd","\\s+(\\w+)\\s+(on|off|0|1|true|false)");
        setUsableInMP(false);
        tooMuchCmds = new TooMuchDiscordException("exception.toomuch.cmds", "exception.toomuch.cmds_found");
        notFoundCmd = new NotFoundDiscordException("exception.notfound.cmd", "exception.notfound.cmd_found");
        forbiddenCmdFound = new BasicDiscordException("exception.basic.forbidden_command_found");
        forbiddenCmdNotFound = new BasicDiscordException("exception.basic.forbidden_command_notfound");
        noEnoughRights = new BasicDiscordException("exception.basic.no_enough_rights");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Language lg = Translator.getLanguageFrom(message.getChannel());

            if (isUserHasEnoughRights(message)) {
                Guild guild = Guild.getGuild(message.getGuild());
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
                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "command.request.1"));
                        return false;
                    }
                    if (value.matches("false") || value.matches("1") || value.matches("off")){
                        if (! guild.getForbiddenCommands().containsKey(command.getName())) {
                            new CommandForbidden(command, guild).addToDatabase();
                            Message.sendText(message.getChannel(), Translator.getLabel(lg, "command.request.2") + " *" + commandName
                                    + "* " + Translator.getLabel(lg, "command.request.3"));
                        }
                        else
                            forbiddenCmdFound.throwException(message, this, lg);
                    }
                    else if (value.matches("true") || value.matches("0") || value.matches("on")){
                        if (guild.getForbiddenCommands().containsKey(command.getName())) {
                            guild.getForbiddenCommands().get(command.getName()).removeToDatabase();
                            Message.sendText(message.getChannel(), Translator.getLabel(lg, "command.request.2") + " *" + commandName
                                    + "* " + Translator.getLabel(lg, "command.request.4"));
                        }
                        else
                            forbiddenCmdNotFound.throwException(message, this, lg);
                    }
                    else
                        new BadUseCommandDiscordException().throwException(message, this, lg);
                }
                else if (potentialCmds.isEmpty())
                    notFoundCmd.throwException(message, this, lg);
                else
                    tooMuchCmds.throwException(message, this, lg);
            }
            else
                noEnoughRights.throwException(message, this, lg);
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "command.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " *CommandForbidden* true` : " + Translator.getLabel(lg, "command.detailed.1")
                + "\n" + prefixe + "`"  + name + " *CommandForbidden* false` : " + Translator.getLabel(lg, "command.detailed.2") + "\n";
    }
}

package commands.admin;

import commands.classic.AboutCommand;
import commands.CommandManager;
import commands.model.AbstractCommand;
import commands.model.Command;
import enums.Language;
import util.Message;
import exceptions.*;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class AvailableCommand extends AbstractCommand {

    private DiscordException tooMuchCmds;
    private DiscordException notFoundCmd;

    public AvailableCommand(){
        super("available","\\s+([\\w|-]+)\\s+(on|off|0|1|true|false)");
        setAdmin(true);
        tooMuchCmds = new TooMuchDiscordException("cmd");
        notFoundCmd = new NotFoundDiscordException("cmd");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        List<Command> potentialCmds = new ArrayList<>();
        String commandName = m.group(1).trim();
        for (Command command : CommandManager.getCommands())
            if (!command.isAdmin() && command.getName().contains(commandName))
                potentialCmds.add(command);

        if (potentialCmds.size() == 1){
            Command command = potentialCmds.get(0);
            String value = m.group(2);

            if (command instanceof AvailableCommand || command instanceof AboutCommand){
                Message.sendText(message.getChannel(), Translator.getLabel(lg, "available.request.1"));
                return;
            }
            if (value.matches("false") || value.matches("1") || value.matches("off")){
                if (command.isPublic()) {
                    command.setPublic(false);
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "available.request.2") + " *" + commandName
                            + "* " + Translator.getLabel(lg, "available.request.3"));
                }
                else
                    BasicDiscordException.FORBIDDEN_COMMAND_FOUND.throwException(message, this, lg);
            }
            else if (value.matches("true") || value.matches("0") || value.matches("on")){
                if (! command.isPublic()) {
                    command.setPublic(true);
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "available.request.2") + "*" + commandName
                            + "* " + Translator.getLabel(lg, "available.request.4"));
                }
                else
                    BasicDiscordException.FORBIDDEN_COMMAND_NOTFOUND.throwException(message, this, lg);
            }
            else
                badUse.throwException(message, this, lg);
        }
        else if (potentialCmds.isEmpty())
            notFoundCmd.throwException(message, this, lg);
        else
            tooMuchCmds.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "available.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + "`*`CommandForbidden`*` true` : " + Translator.getLabel(lg, "available.help.detailed.1")
                + "\n`" + prefixe + name + "`*` CommandForbidden`*` false` : " + Translator.getLabel(lg, "available.help.detailed.2") + "\n";
    }
}

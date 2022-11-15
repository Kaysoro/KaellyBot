package commands.config;

import commands.CommandManager;
import commands.model.AbstractLegacyCommand;
import commands.model.LegacyCommand;
import data.CommandForbidden;
import data.Guild;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import enums.Language;
import exceptions.*;
import util.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class CommandCommand extends AbstractLegacyCommand {

    private DiscordException tooMuchCmds;
    private DiscordException notFoundCmd;

    public CommandCommand(){
        super("cmd","\\s+([\\w|-]+)\\s+(on|off|0|1|true|false)");
        setUsableInMP(false);
        tooMuchCmds = new TooMuchDiscordException("cmd");
        notFoundCmd = new NotFoundDiscordException("cmd");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        if (isUserHasEnoughRights(message)) {
            Guild guild = Guild.getGuild(message.getGuild().block());
            List<LegacyCommand> potentialCmds = new ArrayList<>();
            String commandName = m.group(1).trim();
            for (LegacyCommand command : CommandManager.getCommands())
                if (command.isPublic() && !command.isAdmin() && command.getName().contains(commandName))
                    potentialCmds.add(command);

            if (potentialCmds.size() == 1){
                LegacyCommand command = potentialCmds.get(0);
                String value = m.group(2);

                if (command instanceof CommandCommand){
                    message.getChannel().flatMap(chan -> chan
                            .createMessage(Translator.getLabel(lg, "command.request.1")))
                            .subscribe();
                    return;
                }
                if (value.matches("false") || value.matches("1") || value.matches("off")){
                    if (! guild.getForbiddenCommands().containsKey(command.getName())) {
                        new CommandForbidden(command, guild).addToDatabase();
                        message.getChannel().flatMap(chan -> chan
                                .createMessage(Translator.getLabel(lg, "command.request.2") + " *" + commandName
                                        + "* " + Translator.getLabel(lg, "command.request.3")))
                                .subscribe();
                    }
                    else
                        BasicDiscordException.FORBIDDEN_COMMAND_FOUND.throwException(message, this, lg);
                }
                else if (value.matches("true") || value.matches("0") || value.matches("on")){
                    if (guild.getForbiddenCommands().containsKey(command.getName())) {
                        guild.getForbiddenCommands().get(command.getName()).removeToDatabase();
                        message.getChannel().flatMap(chan -> chan
                                .createMessage(Translator.getLabel(lg, "command.request.2") + " *" + commandName
                                        + "* " + Translator.getLabel(lg, "command.request.4")))
                                .subscribe();
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
        else
            BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "command.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`CommandForbidden`*` true` : " + Translator.getLabel(lg, "command.help.detailed.1")
                + "\n`" + prefixe + name + " `*`CommandForbidden`*` false` : " + Translator.getLabel(lg, "command.help.detailed.2") + "\n";
    }
}

package exceptions;

import commands.model.Command;
import commands.classic.HelpCommand;
import enums.Language;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

/**
 * Created by steve on 14/11/2016.
 */
public class BadUseCommandDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(BadUseCommandDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {

        Message.sendText(message.getChannel(), Translator.getLabel(lg, "exception.bad_use_command")
                .replace("{author}", message.getAuthor().toString())
                .replaceAll("\\{prefix\\}", command.getPrefix(message))
                .replaceAll("\\{cmd.name\\}", command.getName())
                .replace("{HelpCmd.name}", HelpCommand.NAME));
    }
}

package exceptions;

import commands.model.AbstractCommand;
import commands.model.Command;
import commands.classic.HelpCommand;
import enums.Language;
import util.Message;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/11/2016.
 */
public class BadUseCommandDiscordException implements DiscordException {

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        Message.sendText(message.getChannel(), Translator.getLabel(lg, "exception.bad_use_command")
                .replace("{author}", Matcher.quoteReplacement(message.getAuthor().toString()))
                .replaceAll("\\{prefix}", Matcher.quoteReplacement(AbstractCommand.getPrefix(message)))
                .replaceAll("\\{cmd.name}", command.getName())
                .replace("{HelpCmd.name}", HelpCommand.NAME));
    }
}

package exceptions;

import commands.model.AbstractCommand;
import commands.model.Command;
import commands.classic.HelpCommand;
import discord4j.core.object.entity.Message;
import enums.Language;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/11/2016.
 */
public class BadUseCommandDiscordException implements DiscordException {

    @Override
    public void throwException(Message message, Command command, Language lg, Object... arguments) {
        message.getAuthor().ifPresent(author -> message.getChannel().flatMap(chan -> chan
                .createMessage(Translator.getLabel(lg, "exception.bad_use_command")
                        .replace("{author}", Matcher.quoteReplacement(author.getMention()))
                        .replaceAll("\\{prefix}", Matcher.quoteReplacement(AbstractCommand.getPrefix(message)))
                        .replaceAll("\\{cmd.name}", command.getName())
                        .replace("{HelpCmd.name}", HelpCommand.NAME)))
                .subscribe());
    }
}

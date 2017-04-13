package exceptions;

import commands.Command;
import commands.HelpCommand;
import data.Constants;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class BadUseCommandException implements Exception {

    private final static Logger LOG = LoggerFactory.getLogger(BadUseCommandException.class);

    @Override
    public void throwException(IMessage message, Command command) {
        Message.sendText(message.getChannel(), message.getAuthor() + ", " + Constants.prefixCommand
                + command.getName() + " ne s'utilise pas comme ça. Tape `"
                + Constants.prefixCommand + new HelpCommand().getName()
                + " " + command.getName() + "` pour en savoir plus.");
    }
}

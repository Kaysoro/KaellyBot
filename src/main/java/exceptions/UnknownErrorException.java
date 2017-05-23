package exceptions;

import commands.Command;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class UnknownErrorException implements Exception {

    private final static Logger LOG = LoggerFactory.getLogger(UnknownErrorException.class);

    @Override
    public void throwException(IMessage message, Command command) {
        Message.sendText(message.getChannel(), "J'ai rencontré une erreur.");
    }
}

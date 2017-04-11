package exceptions;

import commands.Command;
import data.Constants;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class TwitterFoundException implements Exception {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterFoundException.class);

    @Override
    public void throwException(IMessage message, Command command) {
        Message.send(message.getChannel(), "Les tweets de " + Constants.dofusTwitter + " sont déjà postées ici.");
    }
}

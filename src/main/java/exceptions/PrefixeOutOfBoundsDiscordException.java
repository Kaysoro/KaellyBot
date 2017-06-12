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
public class PrefixeOutOfBoundsDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(PrefixeOutOfBoundsDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        Message.sendText(message.getChannel(), "Le préfixe est inchangé : celui-ci doit faire entre 1 et "
                + Constants.prefixeLimit + " caractères maximum.");
    }
}

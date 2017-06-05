package exceptions;

import commands.Command;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class NotEnoughRightsDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(NotEnoughRightsDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        Message.sendText(message.getChannel(), "Vous ne possédez pas les droits suffisants pour cette action : il faut être " +
                "modérateur ou administrateur pour cela.");
    }
}

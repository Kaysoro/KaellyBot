package exceptions;

import commands.Command;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class ForbiddenCommandNotFoundDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(ForbiddenCommandNotFoundDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        Message.sendText(message.getChannel(), "Cette commande n'est pas désactivée.");
    }
}

package exceptions;

import commands.Command;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class AlmanaxNotFoundDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(AlmanaxNotFoundDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        Message.sendText(message.getChannel(), "Impossible de trouver des informations sur l'almanax désiré.");
    }
}

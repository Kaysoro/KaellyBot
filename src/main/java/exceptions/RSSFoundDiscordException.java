package exceptions;

import commands.Command;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class RSSFoundDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(RSSFoundDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        Message.sendText(message.getChannel(), "Les news de dofus.com sont déjà postées ici.");
    }
}

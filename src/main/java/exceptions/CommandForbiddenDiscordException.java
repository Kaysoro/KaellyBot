package exceptions;

import commands.Command;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class CommandForbiddenDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(CommandForbiddenDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        Message.sendText(message.getChannel(), "Cette commande est désactivée par vos administrateurs. "
                + "Contactez-le(s) pour plus d'informations.");
    }
}

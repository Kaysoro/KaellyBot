package exceptions;

import commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;

/**
 * Created by steve on 14/11/2016.
 */
public class NoExternalEmojiPermissionDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(NoExternalEmojiPermissionDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        Message.sendText(message.getChannel(), "Je n'ai pas le droit d'utiliser des émojis externes sur ce salon.");
    }
}

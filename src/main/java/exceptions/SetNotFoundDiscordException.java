package exceptions;

import commands.Command;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;

/**
 * Created by steve on 14/11/2016.
 */
public class SetNotFoundDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(SetNotFoundDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        Message.sendText(message.getChannel(), "Aucune panoplie trouvée, recommencez en étant plus précis.");
    }
}

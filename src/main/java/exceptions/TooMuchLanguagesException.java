package exceptions;

import commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;

import java.util.List;

/**
 * Created by steve on 14/11/2016.
 */
public class TooMuchLanguagesException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(TooMuchLanguagesException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        Message.sendText(message.getChannel(), "Plusieurs langues trouvées. Recommencez en étant plus précis !");
    }
}

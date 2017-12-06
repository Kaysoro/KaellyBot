package exceptions;

import commands.Command;
import enums.Language;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class InDeveloppmentDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(InDeveloppmentDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        Message.sendText(message.getChannel(), "Cette commande est en cours de développement et n'est donc "
                + "pas disponible pour le moment.");
    }
}

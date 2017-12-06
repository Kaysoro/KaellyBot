package exceptions;

import commands.Command;
import enums.Language;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

/**
 * Created by steve on 14/11/2016.
 */
public class DofusWebsiteInaccessibleDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(DofusWebsiteInaccessibleDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        Message.sendText(message.getChannel(), Translator.getLabel(lg, "game.url") + " est inaccessible pour le moment.");
    }
}

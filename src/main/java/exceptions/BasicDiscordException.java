package exceptions;

import commands.Command;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;
import util.Translator;

/**
 * Created by steve on 14/11/2016.
 */
public class BasicDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(BasicDiscordException.class);

    private String messageKey;

    public BasicDiscordException(String message){
        this.messageKey = message;
    }
    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        Message.sendText(message.getChannel(), Translator.getLabel(lg, messageKey));
    }
}

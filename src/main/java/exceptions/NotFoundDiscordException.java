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
public class NotFoundDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(NotFoundDiscordException.class);
    private String object;
    private String found;

    public NotFoundDiscordException(String object, String found){
        this.object = object;
        this.found = found;
    }

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        Message.sendText(message.getChannel(), Translator.getLabel(lg, "exception.notfound")
                .replace("{object}", Translator.getLabel(lg, object))
                .replace("{found}", Translator.getLabel(lg, found)));
    }
}

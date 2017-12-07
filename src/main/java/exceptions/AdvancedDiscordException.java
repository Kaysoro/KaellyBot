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
public class AdvancedDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(AdvancedDiscordException.class);

    private String messageKey;
    private String[] parameters;

    public AdvancedDiscordException(String message, String[] parameters){
        this.messageKey = message;
        this.parameters = parameters;
    }
    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        String content = Translator.getLabel(lg, messageKey);
        for(String parameter : parameters)
            content = content.replace("{" + parameter + "}", Translator.getLabel(lg, parameter));
        Message.sendText(message.getChannel(), content);
    }
}

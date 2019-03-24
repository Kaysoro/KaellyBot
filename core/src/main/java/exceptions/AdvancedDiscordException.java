package exceptions;

import commands.model.Command;
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
    private Boolean[] translatable;

    public AdvancedDiscordException(String message, String[] parameters, Boolean[] translatable){
        this.messageKey = message;
        this.parameters = parameters;
        this.translatable = translatable;
        if (parameters == null)
            parameters = new String[0];
        if (translatable == null)
            translatable = new Boolean[0];
        if (parameters.length != translatable.length)
            LOG.warn("parameters & translatable have not the same length.");
    }

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        String content = Translator.getLabel(lg, messageKey);
        for(int i = 0; i < parameters.length; i++)
            if (translatable.length > i && translatable[i])
                content = content.replace("{" + i + "}", Translator.getLabel(lg, parameters[i]));
            else
                content = content.replace("{" + i + "}", parameters[i]);
        Message.sendText(message.getChannel(), content);
    }
}

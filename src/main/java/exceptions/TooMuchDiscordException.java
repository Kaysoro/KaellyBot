package exceptions;

import commands.model.Command;
import enums.Language;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.List;

/**
 * Created by steve on 14/11/2016.
 */
public class TooMuchDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(TooMuchDiscordException.class);

    private String objectKey;
    private String foundKey;
    private boolean isTranslatable;

    public TooMuchDiscordException(String objectKey, String foundKey){
        this.objectKey = objectKey;
        this.foundKey = foundKey;
        this.isTranslatable = false;
    }

    public TooMuchDiscordException(String objectKey, String foundKey, boolean isTranslatable){
        this.objectKey = objectKey;
        this.foundKey = foundKey;
        this.isTranslatable = isTranslatable;
    }

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        StringBuilder st = new StringBuilder(Translator.getLabel(lg, "exception.toomuch")
                            .replace("{object}", Translator.getLabel(lg, objectKey))
                            .replace("{found}", Translator.getLabel(lg, foundKey)));

        if (arguments.length > 0) {
            st.append(": ");
            List<Object> objects = (List<Object>) arguments[0];
            for (Object object : objects)
                if (isTranslatable)
                    st.append(Translator.getLabel(lg, object.toString())).append(", ");
                else
                    st.append(object.toString()).append(", ");
            st.delete(st.length() - 2, st.length()).append(".");
        }
        else {
            if (st.substring(st.length() - 1, st.length()).matches("\\s+"))
                st.delete(st.length() - 1, st.length());
            st.append(".");
        }

        Message.sendText(message.getChannel(), st.toString());
    }
}

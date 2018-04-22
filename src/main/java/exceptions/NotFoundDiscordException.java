package exceptions;

import commands.model.Command;
import enums.Language;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;
import util.Translator;

/**
 * Created by steve on 14/11/2016.
 */
public class NotFoundDiscordException implements DiscordException {

    private String objectKey;

    public NotFoundDiscordException(String objectKey){
        this.objectKey = objectKey;
    }

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        String gender = Translator.getLabel(lg, "exception.object." + objectKey + ".gender");
        Message.sendText(message.getChannel(),
                Translator.getLabel(lg, "exception.notfound.not." + gender)
                + " " + Translator.getLabel(lg, "exception.object." + objectKey + ".singular")
                + " " + Translator.getLabel(lg, "exception.notfound.found." + gender) + ".");
    }
}

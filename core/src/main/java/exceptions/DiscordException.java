package exceptions;

import commands.model.Command;
import enums.Language;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public interface DiscordException {

    void throwException(IMessage message, Command command, Language lg, Object... arguments);
}

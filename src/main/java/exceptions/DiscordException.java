package exceptions;

import commands.Command;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public interface DiscordException {

    public void throwException(IMessage message, Command command);
}

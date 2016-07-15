package commands;

import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/07/2016.
 */
public interface Command {
    public boolean request(IMessage message);
}

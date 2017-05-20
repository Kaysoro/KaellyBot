package commands;

import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public interface Command {
    Pattern getName();
    Pattern getPattern();
    boolean request(IMessage message);

    /**
     * Is the command usable in MP?
     * @return True if it can be used in MP, else false.
     */
    boolean isUsableInMP();

    /**
     * Is the command available by the admin ?
     * @return true is the command is available, else false.
     */
    boolean isPublic();

    /**
     * Change the command scope
     * @param isPublic is command available or not
     */
    void setPublic(boolean isPublic);
    /**
     * @return Short description of the command
     */
    String help();

    /**
     * @return Detailed description of the command
     */
    String helpDetailed();
}

package commands;

import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public interface Command {
    public static List<Command> commands = new ArrayList<Command>();
    public Pattern getName();
    public Pattern getPattern();
    public boolean request(IMessage message);
    public boolean isUsableInMP();
    public boolean isPublic();
    public String help();
    public String helpDetailed();
}

package commands;

import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public interface Command {
    List<Command> commands = new ArrayList<>();
    Pattern getName();
    Pattern getPattern();
    boolean request(IMessage message);
    boolean isUsableInMP();
    boolean isPublic();
    String help();
    String helpDetailed();
}

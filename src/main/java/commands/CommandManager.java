package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve on 20/05/2017.
 */
public class CommandManager {

    private static CommandManager instance;

    private List<Command> commands;
    private Map<String, Command> mapCommands;

    private CommandManager(){
        super();
        mapCommands = new HashMap<>();
        commands = new ArrayList<>();

        // Basics commands
        addCommand(new AlmanaxCommand());
        addCommand(new AboutCommand());
        addCommand(new HelpCommand());
        addCommand(new ItemCommand());
        addCommand(new JobCommand());
        addCommand(new MapCommand());
        addCommand(new MusicCommand());
        addCommand(new PortalCommand());
        addCommand(new PrefixCommand());
        addCommand(new RandomCommand());
        addCommand(new RightCommand());
        addCommand(new RSSCommand());
        addCommand(new Rule34Command());
        addCommand(new SoundCommand());
        addCommand(new TwitterCommand());
        addCommand(new WhoisCommand());

        // Admin commands
        addCommand(new AdminCommand());
        addCommand(new AnnounceCommand());
        addCommand(new TalkCommand());
    }

    public static CommandManager getInstance(){
        if (instance == null)
            instance = new CommandManager();
        return instance;
    }

    public static List<Command> getCommands(){
        return getInstance().commands;
    }

    public Command getCommand(String name){
        return mapCommands.get(name);
    }

    private void addCommand(Command command){
        commands.add(command);
        mapCommands.put(command.getName(), command);
    }
}

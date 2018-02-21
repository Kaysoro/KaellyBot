package commands;

import commands.admin.*;
import commands.classic.*;
import commands.config.*;
import commands.model.Command;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by steve on 20/05/2017.
 */
public class CommandManager {

    private static CommandManager instance;

    private List<Command> commands;
    private Map<String, Command> mapCommands;

    private CommandManager(){
        super();
        mapCommands = new ConcurrentHashMap<>();
        commands = new CopyOnWriteArrayList<>();

        // Basics commands
        addCommand(new AlmanaxCommand());
        addCommand(new AboutCommand());
        addCommand(new CommandCommand());
        addCommand(new DistanceCommand());
        addCommand(new HelpCommand());
        addCommand(new ItemCommand());
        addCommand(new JobCommand());
        addCommand(new LanguageCommand());
        addCommand(new MonsterCommand());
        addCommand(new PrefixCommand());
        addCommand(new RandomCommand());
        addCommand(new ResourceCommand());
        addCommand(new RSSCommand());
        addCommand(new SetCommand());
        addCommand(new SoundCommand());
        addCommand(new TutorialCommand());
        addCommand(new TwitterCommand());
        addCommand(new WhoisCommand());

        // Admin commands
        addCommand(new AdminCommand());
        addCommand(new AvailableCommand());
        addCommand(new AnnounceCommand());
        addCommand(new StatCommand());
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

    public static Command getCommand(String name){
        return getInstance().mapCommands.get(name);
    }

    private void addCommand(Command command){
        commands.add(command);
        mapCommands.put(command.getName(), command);
    }
}

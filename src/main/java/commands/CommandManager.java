package commands;

import commands.admin.StatCommand;
import commands.classic.*;
import commands.config.*;
import commands.model.LegacyCommand;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by steve on 20/05/2017.
 */
public class CommandManager {

    private static CommandManager instance;

    private final List<LegacyCommand> commands;
    private final Map<String, LegacyCommand> mapCommands;

    private CommandManager(){
        super();
        mapCommands = new ConcurrentHashMap<>();
        commands = new CopyOnWriteArrayList<>();

        // Basics commands
        addCommand(new AboutCommand());
        addCommand(new AlignmentCommand());
        addCommand(new AllianceCommand());
        addCommand(new AlmanaxCommand());
        addCommand(new AlmanaxAutoCommand());
        addCommand(new CommandCommand());
        addCommand(new DistanceCommand());
        addCommand(new DonateCommand());
        addCommand(new GuildCommand());
        addCommand(new HelpCommand());
        addCommand(new InviteCommand());
        addCommand(new ItemCommand());
        addCommand(new JobCommand());
        addCommand(new LanguageCommand());
        addCommand(new MapCommand());
        addCommand(new MonsterCommand());
        addCommand(new PingCommand());
        addCommand(new PortalCommand());
        addCommand(new PrefixCommand());
        addCommand(new RandomCommand());
        addCommand(new ResourceCommand());
        addCommand(new RSSCommand());
        addCommand(new ServerCommand());
        addCommand(new SetCommand());
        addCommand(new TutorialCommand());
        addCommand(new TwitterCommand());
        addCommand(new WhoisCommand());

        // Admin commands
        addCommand(new StatCommand());
    }

    public static CommandManager getInstance(){
        if (instance == null)
            instance = new CommandManager();
        return instance;
    }

    public static List<LegacyCommand> getCommands(){
        return getInstance().commands;
    }

    private void addCommand(LegacyCommand command){
        commands.add(command);
        mapCommands.put(command.getName(), command);
    }
}

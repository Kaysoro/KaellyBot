package commands;

import commands.admin.*;
import commands.classic.*;
import commands.config.*;
import commands.hidden.SendNudeCommand;
import commands.model.LegacyCommand;
import commands.model.SlashCommand;
import discord4j.discordjson.json.ApplicationCommandRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by steve on 20/05/2017.
 */
public class CommandManager {

    private static CommandManager instance;

    private final List<SlashCommand> slashCommands;
    private final List<LegacyCommand> commands;
    private final Map<String, LegacyCommand> mapCommands;

    private CommandManager(){
        super();
        mapCommands = new ConcurrentHashMap<>();
        commands = new CopyOnWriteArrayList<>();
        slashCommands = new CopyOnWriteArrayList<>();

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

        // Hidden commands
        addCommand(new SendNudeCommand());

        // Admin commands
        addCommand(new StatCommand());
    }

    public static CommandManager getInstance(){
        if (instance == null)
            instance = new CommandManager();
        return instance;
    }

    public static List<SlashCommand> getSlashCommands(){
        return getInstance().slashCommands;
    }

    public static List<ApplicationCommandRequest> getSlashCommandRequests(){
        return getInstance().slashCommands.stream()
                .map(SlashCommand::getCommandRequest)
                .collect(Collectors.toList());
    }

    public static List<LegacyCommand> getCommands(){
        return getInstance().commands;
    }

    public static LegacyCommand getCommand(String name){
        return getInstance().mapCommands.get(name);
    }

    private void addCommand(LegacyCommand command){
        commands.add(command);
        mapCommands.put(command.getName(), command);

        if (command instanceof SlashCommand) {
            slashCommands.add((SlashCommand) command);
        }
    }
}

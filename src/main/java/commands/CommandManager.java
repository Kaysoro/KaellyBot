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
        Command cmd;
        mapCommands = new HashMap<>();
        commands = new ArrayList<>();

        cmd = new AlmanaxCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new GitCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new HelpCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new ItemCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new JobCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new MapCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new MusicCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new NSFWCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new ParrotCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new PortalCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new RandomCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new RightCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new RSSCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new Rule34Command();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new SoundCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new TwitterCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
        cmd = new WhoisCommand();
        mapCommands.put(cmd.getName().pattern(), cmd);
        commands.add(cmd);
    }

    public static CommandManager getInstance(){
        if (instance == null){
            instance = new CommandManager();
        }

        return instance;
    }

    public static List<Command> getCommands(){
        return getInstance().commands;
    }

    public Command getCommand(String name){
        return mapCommands.get(name);
    }
}

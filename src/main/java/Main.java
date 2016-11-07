import commands.*;
import controler.ReadyListener;
import data.ClientConfig;
import sx.blah.discord.api.IDiscordClient;

/**
 * Created by steve on 14/07/2016.
 */
public class Main {

    public static void initCommands(){
        Command.commands.add(new AddRSSCommand());
        Command.commands.add(new HelpCommand());
        Command.commands.add(new ItemCommand());
        Command.commands.add(new MapCommand());
        Command.commands.add(new ParrotCommand());
        Command.commands.add(new RightCommand());
        Command.commands.add(new AlmanaxCommand());
    }

    public static void main(String[] args) {
        initCommands();
        IDiscordClient client = ClientConfig.CLIENT();
        client.getDispatcher().registerListener(new ReadyListener());
    }
}

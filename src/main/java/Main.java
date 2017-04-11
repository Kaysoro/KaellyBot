import commands.*;
import controler.ReadyListener;
import data.ClientConfig;
import sx.blah.discord.api.IDiscordClient;

/**
 * Created by steve on 14/07/2016.
 */
public class Main {

    public static void initCommands(){

        Command.commands.add(new AlmanaxCommand());
        Command.commands.add(new GitCommand());
        Command.commands.add(new HelpCommand());
        Command.commands.add(new ItemCommand());
        Command.commands.add(new JobCommand());
        Command.commands.add(new MapCommand());
        //Command.commands.add(new MusicCommand()); Commande instable pour le moment
        Command.commands.add(new NSFWCommand());
        Command.commands.add(new ParrotCommand());
        Command.commands.add(new PortalCommand());
        Command.commands.add(new RandomCommand());
        Command.commands.add(new RightCommand());
        Command.commands.add(new RSSCommand());
        Command.commands.add(new Rule34Command());
        Command.commands.add(new TwitterCommand());
    }

    public static void main(String[] args) {
        initCommands();
        IDiscordClient client = ClientConfig.DISCORD();
        client.getDispatcher().registerListener(new ReadyListener());
    }
}

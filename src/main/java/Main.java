import commands.*;
import controler.ReadyListener;
import data.ClientConfig;
import data.Connexion;
import data.Constants;
import data.Guild;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import sx.blah.discord.api.IDiscordClient;

import java.io.IOException;
import java.util.regex.Pattern;

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

    public static void main(String[] args) throws IOException {
        initCommands();
        IDiscordClient client = ClientConfig.CLIENT();
        client.getDispatcher().registerListener(new ReadyListener());
    }
}

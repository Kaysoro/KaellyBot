import data.Constants;
import listeners.ReadyListener;
import util.ClientConfig;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.IDiscordClient;

/**
 * Created by steve on 14/07/2016.
 */
public class Main {

    public static void main(String[] args) {
        LoggerFactory.getLogger(Main.class).info("=======================================================");
        LoggerFactory.getLogger(Main.class).info("               " + Constants.name + " v" + Constants.version
                + " for " + Constants.game);
        LoggerFactory.getLogger(Main.class).info("=======================================================");
      IDiscordClient client = ClientConfig.DISCORD();
      client.getDispatcher().registerListener(new ReadyListener());
    }
}

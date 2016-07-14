import controler.MessageListener;
import controler.ReadyListener;
import data.ClientConfig;
import sx.blah.discord.api.IDiscordClient;

/**
 * Created by steve on 14/07/2016.
 */
public class Main {

    public static void main(String[] args) {
        IDiscordClient client = ClientConfig.getIDiscordClient();
        client.getDispatcher().registerListener(new ReadyListener());
        client.getDispatcher().registerListener(new MessageListener());
    }
}

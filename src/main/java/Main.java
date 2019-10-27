import data.Constants;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import listeners.ReadyListener;
import util.ClientConfig;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by steve on 14/07/2016.
 */
public class Main {

    public static void main(String[] args) {
        LoggerFactory.getLogger(Main.class).info("=======================================================");
        LoggerFactory.getLogger(Main.class).info("               " + Constants.name + " v" + Constants.version
                + " for " + Constants.game);
        LoggerFactory.getLogger(Main.class).info("=======================================================");
        List<DiscordClient> clients = ClientConfig.DISCORD();

        ReadyListener readyListener = new ReadyListener();

        clients.forEach(client -> client.getEventDispatcher().on(ReadyEvent.class)
                .flatMap(event -> readyListener.onReady(client))
                .subscribe());
    }
}

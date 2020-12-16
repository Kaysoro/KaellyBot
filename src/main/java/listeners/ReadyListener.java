package listeners;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import finders.AlmanaxCalendar;
import finders.RSSFinder;
import finders.TwitterFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 14/07/2016.
 */
public class ReadyListener {
    private final static Logger LOG = LoggerFactory.getLogger(ReadyListener.class);

    private Map<DiscordClient, Boolean> isReadyOnce;

    public ReadyListener(){
        isReadyOnce = new ConcurrentHashMap<>();
    }

    public Mono<Void> onReady(ReadyEvent event) {

        if (!isReadyOnce.containsKey(event.getClient().rest())) {
            LOG.info("Ecoute des flux RSS du site Dofus...");
            RSSFinder.start();

            LOG.info("Lancement du calendrier Almanax...");
            AlmanaxCalendar.start();

            LOG.info("Connexion à l'API Twitter...");
            TwitterFinder.start();
        }
        return Mono.empty();
    }
}

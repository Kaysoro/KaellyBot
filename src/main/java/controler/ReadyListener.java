package controler;

import commands.ItemCommand;
import data.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class ReadyListener {
    private final static Logger LOG = LoggerFactory.getLogger(ReadyListener.class);

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        LOG.info("KaellyBot connecté !");

        LOG.info("Ajout d'un Message Listener");
        ClientConfig.CLIENT().getDispatcher().registerListener(new MessageListener());
    }
}

package listeners;

import discord4j.core.event.domain.channel.TextChannelDeleteEvent;
import finders.RSSFinder;
import finders.TwitterFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import util.Reporter;

/**
 * Created by steve on 14/07/2016.
 */
public class ChannelDeleteListener {

    private final static Logger LOG = LoggerFactory.getLogger(ChannelDeleteListener.class);

    public ChannelDeleteListener(){
        super();
    }

    public Mono<Void> onReady(TextChannelDeleteEvent event) {
        try {
            if (RSSFinder.getRSSFinders().containsKey(event.getChannel().getId().asString()))
                RSSFinder.getRSSFinders().get(event.getChannel().getId().asString()).removeToDatabase();

            if (TwitterFinder.getTwitterChannels().containsKey(event.getChannel().getId().asLong()))
                TwitterFinder.getTwitterChannels().get(event.getChannel().getId().asLong()).removeToDatabase();


            LOG.info("Salon \"" + event.getChannel().getName() + "\" supprimé");
        } catch(Exception e){
            Reporter.report(e, event.getChannel());
            LOG.error("onReady", e);
        }
        return Mono.empty();
    }
}

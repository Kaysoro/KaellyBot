package listeners;

import data.Guild;
import discord4j.core.event.domain.guild.GuildUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import util.Reporter;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildUpdateListener {
    private final static Logger LOG = LoggerFactory.getLogger(GuildUpdateListener.class);

    public Mono<Void> onReady(GuildUpdateEvent event) {
        try {
            if (! event.getOld().map(guild -> guild.getName().equals(event.getCurrent().getName())).orElse(false)){
                Guild.getGuild(event.getCurrent()).setName(event.getCurrent().getName());
                LOG.info("'" + event.getOld().map(discord4j.core.object.entity.Guild::getName).orElse("")
                        + "' renommé en '" + event.getCurrent().getName() + "'");
            }
        } catch(Exception e){
            Reporter.report(e, event.getCurrent());
            LOG.error("onReady", e);
        }
        return Mono.empty();
    }
}

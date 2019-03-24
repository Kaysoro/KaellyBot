package listeners;

import data.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent;
import util.Reporter;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildUpdateListener {
    private final static Logger LOG = LoggerFactory.getLogger(GuildUpdateListener.class);

    @EventSubscriber
    public void onReady(GuildUpdateEvent event) {
        try {
            if (! event.getOldGuild().getName().equals(event.getNewGuild().getName())){
                Guild.getGuild(event.getNewGuild()).setName(event.getNewGuild().getName());
                LOG.info("'" + event.getOldGuild().getName() + "' renommé en '" + event.getNewGuild().getName() + "'");
            }
        } catch(Exception e){
            Reporter.report(e, event.getGuild());
            LOG.error("onReady", e);
        }
    }
}

package listeners;

import data.ClientConfig;
import data.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildUpdateListener {
    private final static Logger LOG = LoggerFactory.getLogger(GuildUpdateListener.class);

    @EventSubscriber
    public void onReady(GuildUpdateEvent event) {
        ClientConfig.setSentryContext(event.getGuild(), null, null, null);
        if (! event.getOldGuild().getName().equals(event.getNewGuild().getName())){
            Guild.getGuilds().get(event.getNewGuild().getStringID()).setName(event.getNewGuild().getName());
            LOG.info("'" + event.getOldGuild().getName() + "' renommé en '" + event.getNewGuild().getName() + "'");
        }
    }
}

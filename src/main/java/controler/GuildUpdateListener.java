package controler;

import data.ClientConfig;
import data.Constants;
import data.Guild;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.GuildUpdateEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildUpdateListener {
    private final static Logger LOG = LoggerFactory.getLogger(GuildUpdateListener.class);

    @EventSubscriber
    public void onReady(GuildUpdateEvent event) {
        if (! event.getOldGuild().getName().equals(event.getNewGuild().getName())){
            Guild.getGuilds().get(event.getNewGuild().getID()).setName(event.getNewGuild().getName());
        }
    }
}

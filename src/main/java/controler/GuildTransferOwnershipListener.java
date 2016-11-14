package controler;

import data.Guild;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.GuildTransferOwnershipEvent;
import sx.blah.discord.handle.impl.events.GuildUpdateEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildTransferOwnershipListener {
    private final static Logger LOG = LoggerFactory.getLogger(GuildTransferOwnershipListener.class);

    @EventSubscriber
    public void onReady(GuildTransferOwnershipEvent event) {
        User.getUsers().get(event.getGuild().getID()).get(event.getNewOwner().getID()).changeRight(User.RIGHT_ADMIN);
        User.getUsers().get(event.getGuild().getID()).get(event.getOldOwner().getID()).changeRight(User.RIGHT_INVITE);
    }
}

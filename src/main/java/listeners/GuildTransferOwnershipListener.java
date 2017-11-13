package listeners;

import util.ClientConfig;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildTransferOwnershipEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildTransferOwnershipListener {
    private final static Logger LOG = LoggerFactory.getLogger(GuildTransferOwnershipListener.class);

    @EventSubscriber
    public void onReady(GuildTransferOwnershipEvent event) {
        ClientConfig.setSentryContext(event.getGuild(), event.getNewOwner(), null, null);

        User newOwner = User.getUser(event.getGuild(), event.getNewOwner());
        newOwner.changeRight(User.RIGHT_ADMIN);
        User oldOwner = User.getUser(event.getGuild(), event.getOldOwner());
        oldOwner.changeRight(User.RIGHT_INVITE);

        LOG.info(event.getGuild().getName() + " : Passation de " + oldOwner.getName() + " à " + newOwner.getName());
    }
}

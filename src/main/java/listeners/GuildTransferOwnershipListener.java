package listeners;

import data.ClientConfig;
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

        User oldOwner = User.getUsers().get(event.getGuild().getStringID()).get(event.getNewOwner().getStringID());
        oldOwner.changeRight(User.RIGHT_ADMIN);
        User newOwner = User.getUsers().get(event.getGuild().getStringID()).get(event.getOldOwner().getStringID());
        newOwner.changeRight(User.RIGHT_INVITE);

        LOG.info(event.getGuild().getName() + " : Passation de " + oldOwner.getName() + " à " + newOwner.getName());
    }
}

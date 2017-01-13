package controler;

import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.GuildTransferOwnershipEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildTransferOwnershipListener {
    private final static Logger LOG = LoggerFactory.getLogger(GuildTransferOwnershipListener.class);

    @EventSubscriber
    public void onReady(GuildTransferOwnershipEvent event) {

        User oldOwner = User.getUsers().get(event.getGuild().getID()).get(event.getNewOwner().getID());
        oldOwner.changeRight(User.RIGHT_ADMIN);
        User newOwner = User.getUsers().get(event.getGuild().getID()).get(event.getOldOwner().getID());
        newOwner.changeRight(User.RIGHT_INVITE);

        LOG.info(event.getGuild().getName() + " : Passation de " + oldOwner.getName() + " à " + newOwner.getName());
    }
}

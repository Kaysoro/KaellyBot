package listeners;

import data.ClientConfig;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class UserLeaveListener {

    private final static Logger LOG = LoggerFactory.getLogger(UserLeaveListener.class);

    public UserLeaveListener(){
        super();
    }

        @EventSubscriber
        public void onReady(UserLeaveEvent event) {
            ClientConfig.setSentryContext(event.getGuild(), event.getUser(), null, null);
            User user = User.getUsers().get(event.getGuild().getStringID()).get(event.getUser().getStringID());
            user.removeToDatabase();

            LOG.info("L'utilisateur " + user.getId() + " - " + user.getName() + " a quitté "
                    + event.getGuild().getName());
        }
}

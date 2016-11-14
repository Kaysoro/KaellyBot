package controler;

import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.UserLeaveEvent;

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
            User.getUsers().get(event.getGuild().getID()).get(event.getUser().getID()).removeToDatabase();
        }
}

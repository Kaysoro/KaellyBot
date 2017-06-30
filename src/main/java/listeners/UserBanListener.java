package listeners;

import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class UserBanListener {

    private final static Logger LOG = LoggerFactory.getLogger(UserBanListener.class);

    public UserBanListener(){
        super();
    }

        @EventSubscriber
        public void onReady(UserBanEvent event) {
            User user = User.getUsers().get(event.getGuild().getStringID()).get(event.getUser().getStringID());
            user.removeToDatabase();
            LOG.info("L'utilisateur " + user.getId() + " - " + user.getName() + " a été bannis de "
                    + event.getGuild().getName());
        }
}

package controler;

import data.Guild;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.NickNameChangeEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class NickNameChangeListener {

    private final static Logger LOG = LoggerFactory.getLogger(NickNameChangeListener.class);

    public NickNameChangeListener(){
        super();
    }

        @EventSubscriber
        public void onReady(NickNameChangeEvent event) {
            if (! event.getOldNickname().get().equals(event.getNewNickname().get())) {
                User user = User.getUsers().get(event.getGuild().getID()).get(event.getUser().getID());
                user.setName(event.getNewNickname().get());
            }
        }
}

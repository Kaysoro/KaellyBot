package listeners;

import data.ClientConfig;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.member.NicknameChangedEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class NickNameChangeListener {

    private final static Logger LOG = LoggerFactory.getLogger(NickNameChangeListener.class);

    public NickNameChangeListener(){
        super();
    }

        @EventSubscriber
        public void onReady(NicknameChangedEvent event) {
            ClientConfig.setSentryContext(event.getGuild(), event.getUser(), null, null);

            if (event.getOldNickname().isPresent() && event.getNewNickname().isPresent()
                    && ! event.getOldNickname().get().equals(event.getNewNickname().get())) {
                User user = User.getUsers().get(event.getGuild().getStringID()).get(event.getUser().getStringID());
                user.setName(event.getNewNickname().get());
                LOG.info("L'utilisateur " + user.getId() + " - " + event.getOldNickname().get() + " s'est renommé en "
                        + event.getNewNickname().get() + " sur " + event.getGuild().getName());
            }
        }
}

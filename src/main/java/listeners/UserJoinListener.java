package listeners;

import data.ClientConfig;
import data.Guild;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class UserJoinListener {

    private final static Logger LOG = LoggerFactory.getLogger(UserJoinListener.class);

    public UserJoinListener(){
        super();
    }

        @EventSubscriber
        public void onReady(UserJoinEvent event) {
            ClientConfig.setSentryContext(event.getGuild(), event.getUser(), null, null);
            String id = event.getUser().getStringID();
            String name = event.getUser().getDisplayName(event.getGuild());
            String guildId = event.getGuild().getStringID();

            User user = new User(id, name, Guild.getGuilds().get(guildId));
            user.addToDatabase();
            LOG.info("L'utilisateur " + user.getId() + " - " + user.getName() + " a rejoint "
                    + event.getGuild().getName());
        }
}

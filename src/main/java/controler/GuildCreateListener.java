package controler;

import data.Constants;
import data.Guild;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IUser;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildCreateListener {

    private final static Logger LOG = LoggerFactory.getLogger(GuildCreateListener.class);

    public GuildCreateListener(){
        super();
    }

        @EventSubscriber
        public void onReady(GuildCreateEvent event) {

            if(Guild.getGuilds().containsKey(event.getGuild().getStringID())) {
                Guild guild = new Guild(event.getGuild().getStringID(), event.getGuild().getName());
                guild.addToDatabase();

                for (IUser user : event.getGuild().getUsers()) {
                    int level;
                    if (user.getStringID().equals(event.getGuild().getOwnerID()))
                        level = User.RIGHT_ADMIN;
                    else
                        level = User.RIGHT_INVITE;

                    new User(user.getStringID(), user.getDisplayName(event.getGuild()), level, guild)
                            .addToDatabase();
                }

                LOG.info("La guilde " + guild.getId() + " - " + guild.getName() + " a ajouté " + Constants.name);
            }
        }
}

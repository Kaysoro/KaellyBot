package controler;

import data.ClientConfig;
import data.Constants;
import data.Guild;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

/**
 * Created by steve on 14/07/2016.
 */
public class ReadyListener {
    private final static Logger LOG = LoggerFactory.getLogger(ReadyListener.class);

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        LOG.info(Constants.name + "Bot connecté !");

        LOG.info("Check des guildes");
        for(IGuild guild : ClientConfig.CLIENT().getGuilds())
            new Guild(guild.getID(), guild.getName()).addToDatabase();

        LOG.info("Check des utilisateurs");
        for(IGuild guild : ClientConfig.CLIENT().getGuilds())
            for(IUser user : guild.getUsers()){
                    int level;
                    if (user.getID().equals(guild.getOwnerID()))
                        level = User.RIGHT_ADMIN;
                    else
                        level = User.RIGHT_INVITE;

                    new User(user.getID(), user.getDisplayName(guild), level, Guild.getGuilds().get(guild.getID()))
                            .addToDatabase();
                }

        LOG.info("Ecoute des messages...");
        ClientConfig.CLIENT().getDispatcher().registerListener(new MessageListener());
    }
}

package listeners;

import data.ClientConfig;
import data.Constants;
import data.Guild;
import data.User;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

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
            ClientConfig.setSentryContext(event.getGuild(), null, null, null);

            if(!Guild.getGuilds().containsKey(event.getGuild().getStringID())) {
                Guild guild = new Guild(event.getGuild().getStringID(), event.getGuild().getName());
                guild.addToDatabase();

                for (IUser user : event.getGuild().getUsers())
                    new User(user.getStringID(), user.getDisplayName(event.getGuild()), User.RIGHT_INVITE, guild)
                            .addToDatabase();

                User.getUsers().get(guild.getId()).get(event.getGuild().getOwner().getStringID())
                        .changeRight(User.RIGHT_ADMIN);

                LOG.info("La guilde " + guild.getId() + " - " + guild.getName() + " a ajouté " + Constants.name);

                String customMessage = Constants.msgJoinGuild;
                customMessage = customMessage.replaceAll("\\{0\\}", event.getGuild().getOwner().mention());
                customMessage = customMessage.replaceAll("\\{1\\}", event.getGuild().getName());

                if(event.getGuild().getDefaultChannel() != null && event.getGuild().getDefaultChannel()
                        .getModifiedPermissions(ClientConfig.DISCORD().getOurUser())
                        .contains(Permissions.SEND_MESSAGES))
                    Message.sendText(event.getGuild().getDefaultChannel(), customMessage);
                else
                    Message.sendText(event.getGuild().getOwner().getOrCreatePMChannel(), customMessage);

                Message.sendText(ClientConfig.DISCORD().getChannelByID(Constants.chanReportID),
                        "[NEW] **" + guild.getName() + "**, +" + event.getGuild().getUsers().size()
                                +  " utilisateurs");

            }
        }
}

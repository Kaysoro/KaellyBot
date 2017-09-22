package listeners;

import data.*;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildLeaveListener {

    private final static Logger LOG = LoggerFactory.getLogger(GuildLeaveListener.class);

    public GuildLeaveListener(){
        super();
    }

        @EventSubscriber
        public void onReady(GuildLeaveEvent event) {
            ClientConfig.setSentryContext(event.getGuild(), null, null, null);
            Guild.getGuilds().get(event.getGuild().getStringID()).removeToDatabase();

            LOG.info("La guilde " + event.getGuild().getStringID() + " - " + event.getGuild().getName()
                    + " a supprimé " + Constants.name);

            for (RSSFinder finder : RSSFinder.getRSSFinders().values()) {
                IChannel chan = event.getGuild().getChannelByID(Long.parseLong(finder.getChan()));
                if (chan != null && chan.isDeleted()) {
                    finder.removeToDatabase();
                    LOG.info("RSS Chan \"" + chan.getName() + "\"");
                }
            }

            for (TwitterFinder finder : TwitterFinder.getTwitterChannels().values()) {
                IChannel chan = event.getGuild().getChannelByID(finder.getChannelId());
                if (chan != null && chan.isDeleted()) {
                    finder.removeToDatabase();
                    LOG.info("Twitter Chan \"" + chan.getName() + "\"");
                }
            }

            for (AlmanaxCalendar finder : AlmanaxCalendar.getAlmanaxCalendars().values()) {
                IChannel chan = event.getGuild().getChannelByID(Long.parseLong(finder.getChan()));
                if (chan != null && chan.isDeleted()) {
                    finder.removeToDatabase();
                    LOG.info("Almanax Chan \"" + chan.getName() + "\"");
                }
            }

            Message.sendText(ClientConfig.DISCORD().getChannelByID(Constants.chanReportID),
                    "[LOSE] **" + event.getGuild().getName() + "**, -" + event.getGuild().getUsers().size()
                            +  " utilisateurs");
        }
}

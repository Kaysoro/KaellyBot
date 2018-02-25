package listeners;

import data.*;
import util.Message;
import finders.AlmanaxCalendar;
import finders.RSSFinder;
import finders.TwitterFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import util.ClientConfig;
import util.Reporter;

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
        try {
            Guild guild = Guild.getGuild(event.getGuild(), false);
            if (guild != null) {
                guild.removeToDatabase();

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
            }

            Message.sendText(ClientConfig.DISCORD().getChannelByID(Constants.chanReportID),
                    "[LOSE] **" + event.getGuild().getName() + "**, -" + event.getGuild().getUsers().size()
                            +  " utilisateurs");
        } catch(Exception e){
            Reporter.report(e, event.getGuild());
            LOG.error("onReady", e);
        }
    }
}

package controler;

import data.RSSFinder;
import data.TwitterFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelDeleteEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class ChannelDeleteListener {

    private final static Logger LOG = LoggerFactory.getLogger(ChannelDeleteListener.class);

    public ChannelDeleteListener(){
        super();
    }

        @EventSubscriber
        public void onReady(ChannelDeleteEvent event) {
                if (RSSFinder.getRSSFinders().containsKey(event.getChannel().getLongID()))
                    RSSFinder.getRSSFinders().get(event.getChannel().getLongID()).removeToDatabase();

                if (TwitterFinder.getTwitterChannels().containsKey(event.getChannel().getLongID()))
                    TwitterFinder.getTwitterChannels().get(event.getChannel().getLongID()).removeToDatabase();

            LOG.info(event.getGuild().getStringID() + " - Salon \"" + event.getChannel().getName()
                    + "\" supprimé");
        }
}

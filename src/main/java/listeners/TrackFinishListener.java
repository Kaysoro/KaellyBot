package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientConfig;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.events.TrackFinishEvent;
import util.Reporter;

/**
 * Created by steve on 15/01/2017.
 */
public class TrackFinishListener {

    private final static Logger LOG = LoggerFactory.getLogger(TrackFinishListener.class);

    @EventSubscriber
    public void onTrackFinish(TrackFinishEvent event) {
        try {
            IGuild guild = event.getPlayer().getGuild();

            if (!event.getNewTrack().isPresent())
                ClientConfig.DISCORD().getOurUser()
                        .getVoiceStateForGuild(guild).getChannel().leave();
        } catch(Exception e){
            Reporter.report(e, event.getPlayer().getGuild());
            LOG.error("onTrackFinish", e);
        }
    }
}

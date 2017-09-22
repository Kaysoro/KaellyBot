package listeners;

import data.ClientConfig;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.events.TrackFinishEvent;

/**
 * Created by steve on 15/01/2017.
 */
public class TrackFinishListener {

    @EventSubscriber
    public void onTrackFinish(TrackFinishEvent event) {
        ClientConfig.setSentryContext(event.getPlayer().getGuild(), null, null, null);
        IGuild guild = event.getPlayer().getGuild();

        if (! event.getNewTrack().isPresent())
                ClientConfig.DISCORD().getOurUser()
                        .getVoiceStateForGuild(guild).getChannel().leave();
    }
}

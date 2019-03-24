package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.AudioPlayer;
import util.ClientConfig;
import util.Reporter;

/**
 * Created by steve on 15/01/2017.
 */
public class UserVoiceChannelLeaveListener {

    private final static Logger LOG = LoggerFactory.getLogger(UserVoiceChannelLeaveListener.class);

    @EventSubscriber
    public void onUserVoiceChannelLeave(UserVoiceChannelLeaveEvent event) {
        try {
            if (event.getVoiceChannel().isConnected() && event.getVoiceChannel().getConnectedUsers().size() == 1) {
                AudioPlayer.getAudioPlayerForGuild(event.getGuild()).clear();
                event.getVoiceChannel().leave();
            }
        } catch(Exception e){
            Reporter.report(e, event.getGuild(), event.getVoiceChannel());
            LOG.error("onUserVoiceChannelLeave", e);
        }
    }
}

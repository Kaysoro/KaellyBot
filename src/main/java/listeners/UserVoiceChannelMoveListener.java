package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelMoveEvent;
import sx.blah.discord.util.audio.AudioPlayer;
import util.ClientConfig;
import util.Reporter;

/**
 * Created by steve on 15/01/2017.
 */
public class UserVoiceChannelMoveListener {

    private final static Logger LOG = LoggerFactory.getLogger(UserVoiceChannelMoveListener.class);

    @EventSubscriber
    public void onUserVoiceChannelMove(UserVoiceChannelMoveEvent event) {
        try {
            if (!event.getUser().equals(ClientConfig.DISCORD().getOurUser())
                    && event.getOldChannel().isConnected()
                    && event.getOldChannel().getConnectedUsers().size() == 1) {
                AudioPlayer.getAudioPlayerForGuild(event.getGuild()).clear();
                event.getOldChannel().leave();
            }
            else if (event.getUser().equals(ClientConfig.DISCORD().getOurUser())
                    && event.getNewChannel().getConnectedUsers().size() == 1){
                AudioPlayer.getAudioPlayerForGuild(event.getGuild()).clear();
                event.getNewChannel().leave();
            }
        } catch(Exception e){
            Reporter.report(e, event.getGuild(), event.getOldChannel());
            LOG.error("onUserVoiceChannelLeave", e);
        }
    }
}

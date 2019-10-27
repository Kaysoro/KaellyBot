package listeners;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import util.Reporter;

/**
 * Created by steve on 15/01/2017.
 */
public class VoiceStateUpdateListener {

    private final static Logger LOG = LoggerFactory.getLogger(VoiceStateUpdateListener.class);

    public Mono<Void> onUserVoiceChannelLeave(DiscordClient client, VoiceStateUpdateEvent event) {
        try {
            if (event.getVoiceChannel().isConnected() && event.getVoiceChannel().getConnectedUsers().size() == 1) {
                AudioPlayer.getAudioPlayerForGuild(event.getGuild()).clear();
                event.getVoiceChannel().leave();
            }
        } catch(Exception e){
            Reporter.report(e, event.getGuild(), event.getVoiceChannel());
            LOG.error("onUserVoiceChannelLeave", e);
        }

        return Mono.empty();
    }
}

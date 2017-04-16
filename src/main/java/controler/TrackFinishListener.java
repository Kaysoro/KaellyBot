package controler;

import data.ClientConfig;
import data.Guild;
import data.VoiceManager;
import discord.Message;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.events.TrackFinishEvent;

/**
 * Created by steve on 15/01/2017.
 */
public class TrackFinishListener {

    @EventSubscriber
    public void onTrackFinish(TrackFinishEvent event) {
        IGuild guild = event.getPlayer().getGuild();

        if (! event.getNewTrack().isPresent()) {
            if (VoiceManager.getLastChannel().containsKey(guild)) {

                Message.sendText(VoiceManager.getLastChannel().get(guild), "La liste de lecture est vide.");
                Guild.getGuilds().get(guild.getStringID()).setPlayingMusic(false);
            }
            else
                ClientConfig.DISCORD().getOurUser()
                        .getVoiceStateForGuild(guild).getChannel().leave();
        }
    }
}

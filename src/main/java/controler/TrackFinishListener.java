package controler;

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

        if (event.getNewTrack() == null)
            Message.send(VoiceManager.getLastChannel().get(guild), "La liste de lecture est vide.");
    }
}

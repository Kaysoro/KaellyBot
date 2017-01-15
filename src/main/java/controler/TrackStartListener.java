package controler;

import data.VoiceManager;
import discord.Message;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.events.TrackStartEvent;

import static data.VoiceManager.getTrackTitle;

/**
 * Created by steve on 15/01/2017.
 */
public class TrackStartListener {

    @EventSubscriber
    public void onTrackStart(TrackStartEvent event) {
        IGuild guild = event.getPlayer().getGuild();
        Message.send(VoiceManager.getLastChannel().get(guild), "Lecture de **" + getTrackTitle(event.getTrack()) + "**");
    }
}

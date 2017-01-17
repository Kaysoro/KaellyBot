package controler;

import data.Guild;
import data.VoiceManager;
import discord.Message;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.events.TrackQueueEvent;

import static data.VoiceManager.getTrackTitle;

/**
 * Created by steve on 15/01/2017.
 */
public class TrackQueueListener {

    @EventSubscriber
    public void onTrackQueue(TrackQueueEvent event) {
        IGuild guild = event.getPlayer().getGuild();
        Message.send(VoiceManager.getLastChannel().get(guild),
                "**" + getTrackTitle(event.getTrack()) + "** est ajouté à la liste de lecture.");
    }
}

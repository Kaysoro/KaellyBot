package data;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.AudioPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve on 15/01/2017.
 */
public class VoiceManager {

    private static VoiceManager instance = null;

    private Map<IGuild, IChannel> lastChannel;
    private VoiceManager(){
        super();
        lastChannel = new HashMap<>();
    }

    public static VoiceManager getInstance() {
        if (instance == null){
            instance = new VoiceManager();
        }
        return instance;
    }

    public static Map<IGuild, IChannel> getLastChannel() {
        return getInstance().lastChannel;
    }

    public static String getTrackTitle(AudioPlayer.Track track) {
        return track.getMetadata().containsKey("title") ?
                String.valueOf(track.getMetadata().get("title")) : "Unknown Track";
    }

    public static void setTrackTitle(AudioPlayer.Track track, String title) {
        track.getMetadata().put("title", title);
    }
}

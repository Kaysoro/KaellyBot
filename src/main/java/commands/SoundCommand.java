package commands;

import data.ClientConfig;
import data.Constants;
import data.Guild;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import static data.VoiceManager.setTrackTitle;

/**
 * Created by steve on 14/07/2016.
 */
public class SoundCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(SoundCommand.class);
    private static List<File> sounds;

    public SoundCommand(){
        super(Pattern.compile("sound"),
        Pattern.compile("^(" + Constants.prefixCommand
                + "sound)(\\s+.+)?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            IVoiceChannel voice = message.getAuthor().getVoiceStateForGuild(message.getGuild()).getChannel();

            if (voice == null)
                new NotInVocalChannelException().throwException(message, this);
            else if (Guild.getGuilds().get(message.getGuild().getID()).isPlayingMusic())
                new PlayingMusicException().throwException(message, this);

            else {
                if (!voice.getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.VOICE_CONNECT))
                    new NoVoiceConnectPermissionException().throwException(message, this);
                else if (voice.getConnectedUsers().size() >= voice.getUserLimit() && voice.getUserLimit() != 0)
                    new VoiceChannelLimitException().throwException(message, this);
                else {
                    try {
                        voice.join();

                        if(m.group(2) != null){ // Specific sound
                            //TODO
                        }
                        else { // random sound

                            File file = getSounds().get(new Random().nextInt(getSounds().size()));
                            try {
                                setTrackTitle(AudioPlayer.
                                        getAudioPlayerForGuild(message.getGuild()).queue(file), file.toString());
                            } catch (IOException | UnsupportedAudioFileException e) {
                                Reporter.report(e);
                                LOG.error(e.getMessage());
                            }
                        }

                    } catch (MissingPermissionsException e) {
                        new NoVoiceConnectPermissionException().throwException(message, this);
                    }
                }
            }
        }

        return true;
    }

    private List<File> getSounds(){
        if (sounds == null) {
            File file = new File(getClass().getResource("../sounds").getPath());
            sounds = Arrays.asList(file.listFiles());
        }

        return sounds;
    }

    @Override
    public boolean isUsableInMP() {
        return false;
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "sound** joue un son en vous rejoignant succintement dans votre canal vocal.";
    }

    @Override
    public String helpDetailed() {
        StringBuilder st = new StringBuilder();

        for(File f : getSounds())
            st.append("*").append(f.getName().replaceFirst("[.][^.]+$", "")).append("*, ");
        st.delete(st.length() - 2, st.length()).append(".");
        return help()
                + "\n`" + Constants.prefixCommand + "sound` : joue un son au hasard parmi la liste suivante : " + st.toString()
                + "\n`" + Constants.prefixCommand + "sound *sound`* : joue le son passé en paramètre.\n";
    }
}

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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
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
            else if (Guild.getGuilds().get(message.getGuild().getStringID()).isPlayingMusic())
                new PlayingMusicException().throwException(message, this);

            else {
                if (!voice.getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.VOICE_CONNECT))
                    new NoVoiceConnectPermissionException().throwException(message, this);
                else if (voice.getConnectedUsers().size() >= voice.getUserLimit() && voice.getUserLimit() != 0)
                    new VoiceChannelLimitException().throwException(message, this);
                else {
                    try {
                        if(m.group(2) != null){ // Specific sound
                            String value = m.group(2).trim().toLowerCase();
                            List<File> files = new ArrayList<>();
                            for(File file : getSounds())
                                if (file.getName().toLowerCase().startsWith(value))
                                    files.add(file);

                            if (! files.isEmpty()){
                                File file = files.get(new Random().nextInt(files.size()));
                                try {
                                    voice.join();
                                    setTrackTitle(AudioPlayer.
                                            getAudioPlayerForGuild(message.getGuild()).queue(file), file.toString());
                                } catch (IOException | UnsupportedAudioFileException e) {
                                    Reporter.report(e);
                                    LOG.error(e.getMessage());
                                }
                            }
                            else
                                new SoundNotFoundException().throwException(message, this);
                        }
                        else { // random sound

                            File file = getSounds().get(new Random().nextInt(getSounds().size()));
                            try {
                                voice.join();
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
            File file = new File(System.getProperty("user.dir") + File.separator + "sounds");
            FilenameFilter filter = (File dir, String name) -> name.toLowerCase().endsWith(".mp3");

            if (file.listFiles(filter) == null)
                sounds = new ArrayList<>();
            else
                sounds = Arrays.asList(file.listFiles(filter));
        }

        return sounds;
    }

    @Override
    public boolean isUsableInMP() {
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "sound** joue un son en vous rejoignant succintement dans votre canal vocal.";
    }

    @Override
    public String helpDetailed() {
        StringBuilder st = new StringBuilder();

        for(File f : getSounds())
            st.append("*").append(f.getName().toLowerCase().replaceFirst("[.][^.]+$", "")).append("*, ");
        st.delete(st.length() - 2, st.length()).append(".");
        return help()
                + "\n`" + Constants.prefixCommand + "sound` : joue un son au hasard parmi la liste suivante : " + st.toString()
                + "\n`" + Constants.prefixCommand + "sound *sound`* : joue le son passé en paramètre.\n";
    }
}

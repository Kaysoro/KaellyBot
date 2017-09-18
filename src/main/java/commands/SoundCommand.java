package commands;

import data.ClientConfig;
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
import java.util.*;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class SoundCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(SoundCommand.class);
    private static List<File> sounds;

    public SoundCommand(){
        super("sound","(\\s+.+)?");
        setUsableInMP(false);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            IVoiceChannel voice = message.getAuthor().getVoiceStateForGuild(message.getGuild()).getChannel();

            if (voice == null)
                new NotInVocalChannelDiscordException().throwException(message, this);
            else {
                if (!voice.getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.VOICE_CONNECT)
                        || !ClientConfig.DISCORD().getOurUser().getPermissionsForGuild(message.getGuild())
                        .contains(Permissions.VOICE_CONNECT))
                    new NoVoiceConnectPermissionDiscordException().throwException(message, this);
                else if (voice.getConnectedUsers().size() >= voice.getUserLimit() && voice.getUserLimit() != 0)
                    new VoiceChannelLimitDiscordException().throwException(message, this);
                else {
                    try {
                        Matcher m = getMatcher(message);
                        m.find();
                        if(m.group(1) != null){ // Specific sound
                            String value = m.group(1).trim().toLowerCase();
                            List<File> files = new ArrayList<>();
                            for(File file : getSounds())
                                if (file.getName().toLowerCase().startsWith(value))
                                    files.add(file);

                            if (! files.isEmpty()){
                                File file = files.get(new Random().nextInt(files.size()));
                                playSound(voice, message, file);
                            }
                            else
                                new SoundNotFoundDiscordException().throwException(message, this);
                        }
                        else { // random sound

                            File file = getSounds().get(new Random().nextInt(getSounds().size()));
                            playSound(voice, message, file);
                        }

                    } catch (MissingPermissionsException e) {
                        new NoVoiceConnectPermissionDiscordException().throwException(message, this);
                    }
                }
            }
        }

        return true;
    }

    private void playSound(IVoiceChannel voice, IMessage message, File file) {
        try {
            voice.join();
            AudioPlayer.getAudioPlayerForGuild(message.getGuild()).queue(file).getMetadata()
                    .put(file.getName(), file.toString());
        } catch (IOException | UnsupportedAudioFileException e) {
            ClientConfig.setSentryContext(message.getGuild(),
                    message.getAuthor(), message.getChannel(),
                    message);
            LOG.error(e.getMessage());
        }
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
        Collections.sort(sounds);
        return sounds;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** joue un son en vous rejoignant succintement dans votre canal vocal.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        StringBuilder st = new StringBuilder("\n```");

        List<File> sounds = getSounds();
        long sizemax = 0;

        for(File f : sounds)
            if (f.getName().replaceFirst("[.][^.]+$", "").length() > sizemax)
                sizemax = f.getName().replaceFirst("[.][^.]+$", "").length();

        for(File f : sounds) {
            st.append(f.getName().replaceFirst("[.][^.]+$", ""));
            for(int i = 0 ; i < sizemax-f.getName().replaceFirst("[.][^.]+$", "").length() ; i++)
                st.append(" ");
            st.append("\t");
        }
        st.append("```");
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + "` : joue un son au hasard parmi la liste suivante : " + st.toString()
                + "\n" + prefixe + "`"  + name + " `*`sound`* : joue le son passé en paramètre.\n";
    }
}

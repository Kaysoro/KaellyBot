package commands;

import data.ClientConfig;
import data.Constants;
import data.VoiceManager;
import discord.Message;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import static data.VoiceManager.setTrackTitle;

/**
 * Created by steve on 14/07/2016.
 */
public class MusicCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(MusicCommand.class);

    public MusicCommand(){
        super(Pattern.compile("music"),
        Pattern.compile("^(" + Constants.prefixCommand
                + "music)(\\s+-join|\\s+-play|\\s+-pause|\\s+-skip|\\s+-shuffle|\\s+-leave)?(\\s+http.+)?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            if (m.group(2) == null && m.group(3) != null){ // Add music in the queue
                Message.send(message.getChannel(), "Musique ajouté à la liste de lecture.");
                queueUrl(message, m.group(3).replaceAll("\\s+", ""));
            }
            else if(m.group(2) != null && m.group(3) == null){ // Music command
                if (m.group(2).matches("\\s+-join")){
                    join(message);
                }
                else if (m.group(2).matches("\\s+-play")){
                    AudioPlayer.getAudioPlayerForGuild(message.getGuild()).setPaused(false);
                }
                else if (m.group(2).matches("\\s+-pause")){
                    AudioPlayer.getAudioPlayerForGuild(message.getGuild()).setPaused(true);
                }
                else if (m.group(2).matches("\\s+-skip")){
                    AudioPlayer.getAudioPlayerForGuild(message.getGuild()).skip();
                }
                else if (m.group(2).matches("\\s+-shuffle")){
                    AudioPlayer.getAudioPlayerForGuild(message.getGuild()).shuffle();
                }
                else if (m.group(2).matches("\\s+-leave")){
                    leave(message);
                }
            }
            else
                new BadUseCommandException().throwException(message, this);
        }

        return true;
    }

    private void join(IMessage message) {
        if (message.getAuthor().getVoiceStateForGuild(message.getGuild()).getChannel() == null)
            new NotInVocalChannelException().throwException(message, this);
        else {
            IVoiceChannel voice = message.getAuthor().getVoiceStateForGuild(message.getGuild()).getChannel();
            if (!voice.getModifiedPermissions(ClientConfig.CLIENT().getOurUser()).contains(Permissions.VOICE_CONNECT))
                new NoVoiceConnectPermissionException().throwException(message, this);
            else if (voice.getConnectedUsers().size() >= voice.getUserLimit() && voice.getUserLimit() != 0)
                new VoiceChannelLimitException().throwException(message, this);
            else {
                try {
                    voice.join();
                    VoiceManager.getLastChannel().put(message.getGuild(), message.getChannel());
                    Message.send(message.getChannel(), "Connecté à **" + voice.getName() + "**.");
                } catch (MissingPermissionsException e) {
                    new NoVoiceConnectPermissionException().throwException(message, this);
                }
            }
        }
    }

    private void leave(IMessage message) {
        IVoiceChannel voice = ClientConfig.CLIENT().getOurUser().getVoiceStateForGuild(message.getGuild()).getChannel();
        if (voice != null) {
            voice.leave();
            Message.send(VoiceManager.getLastChannel().get(message.getGuild()), "Déconnecté de **" + voice.getName() + "**.");
            VoiceManager.getLastChannel().remove(message.getGuild());
        }
    }

    private void queueUrl(IMessage message, String url) {
        try {
            URL u = new URL(url);
            setTrackTitle(AudioPlayer.getAudioPlayerForGuild(message.getGuild()).queue(u), u.getFile());
        } catch (MalformedURLException e) {
            new MalformedMusicURLException().throwException(message, this);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            Reporter.report(e);
            new UnknownErrorException().throwException(message, this);
        } catch (UnsupportedAudioFileException e) {
            new UnsupportedMusicFileException().throwException(message, this);
        }
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
        return "**" + Constants.prefixCommand + "music** permet d'écouter de la musique provenant de Youtube dans un canal vocal.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "music -join` : rejoint le canal vocal dans lequel vous êtes."
                + "\n`" + Constants.prefixCommand + "music `*`" + Constants.youtubeURL + "yourVideo`* : ajoute la vidéo youtube à la liste de lecture."
                + "\n`" + Constants.prefixCommand + "music -play` : joue la musique actuelle."
                + "\n`" + Constants.prefixCommand + "music -pause` : arrête la musique actuelle."
                + "\n`" + Constants.prefixCommand + "music -skip` : passe à la musique suivante."
                + "\n`" + Constants.prefixCommand + "music -shuffle` : mélange la liste de lecture."
                + "\n`" + Constants.prefixCommand + "music -leave` : quitte le canal vocal.\n";
    }
}

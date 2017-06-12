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
import java.util.regex.Matcher;

import static data.VoiceManager.setTrackTitle;

/**
 * Created by steve on 14/07/2016.
 */
public class MusicCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(MusicCommand.class);

    public MusicCommand(){
        super("music","(\\s+-join|\\s+-play|\\s+-pause|\\s+-skip|\\s+-shuffle|\\s+-leave)?(\\s+http.+)?");
        setPublic(false);
        setUsableInMP(false);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Matcher m = getMatcher(message);
            m.find();
            if (m.group(1) == null && m.group(2) != null){ // Add music in the queue
                Message.sendText(message.getChannel(), "Musique ajouté à la liste de lecture.");
                queueUrl(message, m.group(2).replaceAll("\\s+", ""));
            }
            else if(m.group(1) != null && m.group(2) == null){ // Music command
                if (m.group(1).matches("\\s+-join")){
                    join(message);
                }
                else if (m.group(1).matches("\\s+-play")){
                    AudioPlayer.getAudioPlayerForGuild(message.getGuild()).setPaused(false);
                }
                else if (m.group(1).matches("\\s+-pause")){
                    AudioPlayer.getAudioPlayerForGuild(message.getGuild()).setPaused(true);
                }
                else if (m.group(1).matches("\\s+-skip")){
                    AudioPlayer.getAudioPlayerForGuild(message.getGuild()).skip();
                }
                else if (m.group(1).matches("\\s+-shuffle")){
                    AudioPlayer.getAudioPlayerForGuild(message.getGuild()).shuffle();
                }
                else if (m.group(1).matches("\\s+-leave")){
                    leave(message);
                }
            }
            else
                new BadUseCommandDiscordException().throwException(message, this);
        }

        return true;
    }

    private void join(IMessage message) {
        if (message.getAuthor().getVoiceStateForGuild(message.getGuild()).getChannel() == null)
            new NotInVocalChannelDiscordException().throwException(message, this);
        else {
            IVoiceChannel voice = message.getAuthor().getVoiceStateForGuild(message.getGuild()).getChannel();
            if (!voice.getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.VOICE_CONNECT)
                    && ! ClientConfig.DISCORD().getOurUser().getPermissionsForGuild(message.getGuild())
                    .contains(Permissions.VOICE_CONNECT))
                new NoVoiceConnectPermissionDiscordException().throwException(message, this);
            else if (voice.getConnectedUsers().size() >= voice.getUserLimit() && voice.getUserLimit() != 0)
                new VoiceChannelLimitDiscordException().throwException(message, this);
            else {
                try {
                    voice.join();
                    VoiceManager.getLastChannel().put(message.getGuild(), message.getChannel());
                    Message.sendText(message.getChannel(), "Connecté à **" + voice.getName() + "**.");
                } catch (MissingPermissionsException e) {
                    new NoVoiceConnectPermissionDiscordException().throwException(message, this);
                }
            }
        }
    }

    private void leave(IMessage message) {
        IVoiceChannel voice = ClientConfig.DISCORD().getOurUser().getVoiceStateForGuild(message.getGuild()).getChannel();
        if (voice != null) {
            voice.leave();
            Message.sendText(VoiceManager.getLastChannel().get(message.getGuild()), "Déconnecté de **" + voice.getName() + "**.");
            VoiceManager.getLastChannel().remove(message.getGuild());
        }
    }

    private void queueUrl(IMessage message, String url) {
        try {
            URL u = new URL(url);
            setTrackTitle(AudioPlayer.getAudioPlayerForGuild(message.getGuild()).queue(u), u.getFile());
        } catch (MalformedURLException e) {
            new MalformedMusicURLDiscordException().throwException(message, this);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            Reporter.report(e, message.getContent());
            new UnknownErrorDiscordException().throwException(message, this);
        } catch (UnsupportedAudioFileException e) {
            new UnsupportedMusicFileDiscordException().throwException(message, this);
        }
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** permet d'écouter de la musique provenant de Youtube dans un canal vocal.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n`" + prefixe + name + " -join` : rejoint le canal vocal dans lequel vous êtes."
                + "\n`" + prefixe + name + " `*`" + Constants.youtubeURL + "yourVideo`* : ajoute la vidéo youtube à la liste de lecture."
                + "\n`" + prefixe + name + " -play` : joue la musique actuelle."
                + "\n`" + prefixe + name + " -pause` : arrête la musique actuelle."
                + "\n`" + prefixe + name + " -skip` : passe à la musique suivante."
                + "\n`" + prefixe + name + " -shuffle` : mélange la liste de lecture."
                + "\n`" + prefixe + name + " -leave` : quitte le canal vocal.\n";
    }
}

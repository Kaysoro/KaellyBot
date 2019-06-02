package commands.classic;

import commands.model.AbstractCommand;
import enums.Language;
import util.ClientConfig;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.audio.AudioPlayer;
import util.Reporter;
import util.Translator;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by steve on 14/07/2016.
 */
public class SoundCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(SoundCommand.class);
    private static List<File> sounds;
    private DiscordException notFoundSound;

    public SoundCommand(){
        super("sound","(\\s+.+)?");
        setUsableInMP(false);
        notFoundSound = new NotFoundDiscordException("sound");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        try {
            IVoiceChannel voice = message.getAuthor().getVoiceStateForGuild(message.getGuild()).getChannel();

            if (voice == null)
                BasicDiscordException.NOT_IN_VOCAL_CHANNEL.throwException(message, this, lg);
            else {
                if (voice.isConnected() && m.group(1) != null && m.group(1).matches("\\s+-leave")){
                    AudioPlayer.getAudioPlayerForGuild(message.getGuild()).clear();
                    voice.leave();
                }
                else if (!voice.getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.VOICE_CONNECT)
                        || !ClientConfig.DISCORD().getOurUser().getPermissionsForGuild(message.getGuild())
                        .contains(Permissions.VOICE_CONNECT))
                    BasicDiscordException.NO_VOICE_PERMISSION.throwException(message, this, lg);
                else if (voice.getConnectedUsers().size() >= voice.getUserLimit()
                        && voice.getUserLimit() != 0
                        && ! voice.isConnected())
                    BasicDiscordException.VOICE_CHANNEL_LIMIT.throwException(message, this, lg);
                else {
                    try {
                        if (m.group(1) != null) { // Specific sound
                            String value = m.group(1).trim().toLowerCase();
                            List<File> files = new ArrayList<>();
                            for (File file : getSounds())
                                if (file.getName().toLowerCase().startsWith(value))
                                    files.add(file);

                            if (!files.isEmpty()) {
                                File file = files.get(new Random().nextInt(files.size()));
                                playSound(voice, message, file);
                            } else
                                notFoundSound.throwException(message, this, lg);
                        } else { // random sound

                            File file = getSounds().get(new Random().nextInt(getSounds().size()));
                            playSound(voice, message, file);
                        }

                    } catch (MissingPermissionsException e) {
                        BasicDiscordException.NO_VOICE_PERMISSION.throwException(message, this, lg);
                    }
                }
            }
        } catch(Exception e){
            Reporter.report(e, message.getGuild(), message.getChannel(), message.getAuthor(), message);
            LOG.error("request", e);
        }
    }

    private void playSound(IVoiceChannel voice, IMessage message, File file) {
        try {
            voice.join();
            AudioPlayer.getAudioPlayerForGuild(message.getGuild()).queue(file).getMetadata()
                    .put(file.getName(), file.toString());
        } catch (IOException | UnsupportedAudioFileException e) {
            Reporter.report(e, message.getGuild(), message.getChannel(), message.getAuthor(), message);
            LOG.error("playSound", e);
        }
    }

    private List<File> getSounds(){
        if (sounds == null) {
            File file = new File(System.getProperty("user.dir") + File.separator + "sounds");
            FilenameFilter filter = (File dir, String name) -> name.toLowerCase().endsWith(".mp3");

            File[] files = file.listFiles(filter);
            if (files != null)
                sounds = Arrays.asList(files);
            else
                sounds = new ArrayList<>();
        }
        Collections.sort(sounds);
        return sounds;
    }

    private String getSoundsNameList() {
        final List<String> SOUNDS = getSounds().stream()
                .map(file -> file.getName().replaceFirst("[.][^.]+$", ""))
                .collect(Collectors.toList());
        final long SIZE_MAX = SOUNDS.stream()
                .map(String::length)
                .max(Integer::compare)
                .orElse(20);

        StringBuilder sb = new StringBuilder("```");
        SOUNDS.forEach(soundsName -> sb.append(String.format("%-" + SIZE_MAX + "s", soundsName)).append("\t"));
        sb.append("```");
        return sb.toString();
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "sound.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + "` : " + Translator.getLabel(lg, "sound.help.detailed.1") + " " + getSoundsNameList()
                + "\n`" + prefixe + name + " `*`sound`* : " + Translator.getLabel(lg, "sound.help.detailed.2")
                + "\n`" + prefixe + name + " -leave` : " + Translator.getLabel(lg, "sound.help.detailed.3") + "\n";
    }
}
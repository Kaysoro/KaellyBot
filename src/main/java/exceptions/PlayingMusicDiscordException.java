package exceptions;

import commands.Command;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class PlayingMusicDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(PlayingMusicDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command) {
        Message.sendText(message.getChannel(), "Je suis déjà occupé à jouer de la musique, désolé.");
    }
}

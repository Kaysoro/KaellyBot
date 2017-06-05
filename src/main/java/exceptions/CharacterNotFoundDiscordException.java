package exceptions;

import commands.Command;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class CharacterNotFoundDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(CharacterNotFoundDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        Message.sendText(message.getChannel(), "Aucun personnage trouvé, recommencez en étant plus précis.");
    }
}

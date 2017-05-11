package exceptions;

import commands.Command;
import data.Constants;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/11/2016.
 */
public class DofusWebsiteInaccessibleException implements Exception {

    private final static Logger LOG = LoggerFactory.getLogger(DofusWebsiteInaccessibleException.class);

    @Override
    public void throwException(IMessage message, Command command) {
        Message.sendText(message.getChannel(), Constants.officialURL + " est inaccessible pour le moment.");
    }
}

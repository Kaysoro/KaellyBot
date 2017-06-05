package exceptions;

import commands.Command;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;

/**
 * Created by steve on 14/11/2016.
 */
public class TooMuchCharactersDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(TooMuchCharactersDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        StringBuilder st = new StringBuilder("Plusieurs personnages de même nom trouvés sur les serveurs suivants : ");

        if (arguments.length > 0) {
            List<String> servers = (List<String>) arguments[0];
            for (String server : servers)
                st.append(server).append(", ");
            st.delete(st.length() - 2, st.length()).append(".");
        }
        else
            st.delete(st.length() - 3, st.length()).append(".");

        Message.sendText(message.getChannel(), st.toString());
    }
}

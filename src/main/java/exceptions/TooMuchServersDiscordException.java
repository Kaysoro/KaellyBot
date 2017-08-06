package exceptions;

import commands.Command;
import data.ServerDofus;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;

/**
 * Created by steve on 14/11/2016.
 */
public class TooMuchServersDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(TooMuchServersDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        StringBuilder st = new StringBuilder("Plusieurs serveurs trouvés, recommencez en étant plus précis : ");
        if (arguments.length > 0) {
            List<ServerDofus> servers = (List<ServerDofus>) arguments[0];
            for (ServerDofus server : servers)
                st.append(server.getName()).append(", ");
            st.delete(st.length() - 2, st.length()).append(".");
        }
        else
            st.delete(st.length() - 3, st.length()).append(".");
        Message.sendText(message.getChannel(), st.toString());
    }
}

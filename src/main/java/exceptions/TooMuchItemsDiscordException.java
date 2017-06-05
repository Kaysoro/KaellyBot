package exceptions;

import commands.Command;
import discord.Message;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;

/**
 * Created by steve on 14/11/2016.
 */
public class TooMuchItemsDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(TooMuchItemsDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command) {
        Message.sendText(message.getChannel(), "Plusieurs items de nom similaires trouvés.");
    }

    public void throwException(IMessage message, Command command, List<Pair<String, String>> items) {
        StringBuilder st = new StringBuilder("Plusieurs items de nom similaires trouvés : ");
        for(Pair<String, String> item : items)
            st.append(item.getLeft()).append(", ");
        st.delete(st.length() - 2, st.length()).append(".");
        Message.sendText(message.getChannel(), st.toString());
    }
}

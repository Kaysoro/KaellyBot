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
public class TooMuchTutosDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(TooMuchTutosDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        StringBuilder st = new StringBuilder("Plusieurs tutoriels de nom similaires trouvés : ");

        if (arguments.length > 0) {
            List<Pair<String, String>> items = (List<Pair<String, String>>) arguments[0];
            for (Pair<String, String> item : items)
                st.append(item.getLeft()).append(", ");
            st.delete(st.length() - 2, st.length()).append(".");
        }
        else
            st.delete(st.length() - 3, st.length()).append(".");
        Message.sendText(message.getChannel(), st.toString());
    }
}

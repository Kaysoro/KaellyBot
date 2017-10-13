package exceptions;

import commands.Command;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;

import java.util.List;

/**
 * Created by steve on 14/11/2016.
 */
public class TooMuchSetsDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(TooMuchSetsDiscordException.class);

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        StringBuilder st = new StringBuilder("Plusieurs panoplies de même nom trouvées : ");

        if (arguments.length > 0) {
            List<Pair<String,String>> panoplies = ( List<Pair<String,String>>) arguments[0];
            for (Pair<String, String> pano : panoplies)
                st.append(pano.getKey()).append(", ");
            st.delete(st.length() - 2, st.length()).append(".");
        }
        else
            st.delete(st.length() - 3, st.length()).append(".");

        Message.sendText(message.getChannel(), st.toString());
    }
}

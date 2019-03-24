package exceptions;

import commands.classic.AllianceCommand;
import commands.classic.GuildCommand;
import commands.classic.WhoisCommand;
import commands.model.Command;
import enums.AnkamaBug;
import enums.Language;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by steve on 14/11/2016.
 */
public class TooMuchDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(TooMuchDiscordException.class);
    private final static int ITEM_LIMIT = 25;
    private String objectKey;
    private boolean isTranslatable;

    public TooMuchDiscordException(String objectKey){
        this.objectKey = objectKey;
        this.isTranslatable = false;
    }

    public TooMuchDiscordException(String objectKey, boolean isTranslatable){
        this.objectKey = objectKey;
        this.isTranslatable = isTranslatable;
    }

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        AnkamaBug bug = null;

        String gender = Translator.getLabel(lg, "exception.object." + objectKey + ".gender");
        StringBuilder st = new StringBuilder(Translator.getLabel(lg, "exception.toomuch.toomuch." + gender))
                .append(" ").append(Translator.getLabel(lg, "exception.object." + objectKey + ".plural"))
                .append(" ").append(Translator.getLabel(lg, "exception.toomuch.found." + gender));

        if (arguments.length > 0) {
            List<Object> objects = (List<Object>) arguments[0];

            long similarOcc = objects.stream()
                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                    .values().stream()
                    .max(Long::compareTo)
                    .orElse(0L);

            if (similarOcc > 1){
                if (command instanceof WhoisCommand)
                    bug = AnkamaBug.GHOST_CHARACTER;
                else if (command instanceof GuildCommand)
                    bug = AnkamaBug.GHOST_GUILD;
                else if (command instanceof AllianceCommand)
                    bug = AnkamaBug.GHOST_ALLY;
            }

            if (objects.size() <= ITEM_LIMIT){
                st.append(": ");

                for (Object object : objects)
                    if (isTranslatable)
                        st.append(Translator.getLabel(lg, object.toString())).append(", ");
                    else
                        st.append(object.toString()).append(", ");
                st.delete(st.length() - 2, st.length()).append(".");
            }
            else
                st.append(". ").append(Translator.getLabel(lg, "exception.toomuch.items"));
        }
        else {
            if (st.substring(st.length() - 1, st.length()).matches("\\s+"))
                st.delete(st.length() - 1, st.length());
            st.append(".");
        }

        if (bug != null)
            Message.sendEmbed(message.getChannel(), bug.getEmbed(st.toString(), lg));
        else
            Message.sendText(message.getChannel(), st.toString());
    }
}

package exceptions;

import commands.model.Command;
import enums.Language;
import util.ClientConfig;
import util.Translator;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MissingPermissionsException;

/**
 * Created by steve on 14/11/2016.
 */
public class MissingPermissionDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(MissingPermissionDiscordException.class);
    private final static String PERMISSION_PREFIX = "permission.";

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        StringBuilder st = new StringBuilder(Translator.getLabel(lg, "exception.missing_permission")
                .replace("{channel.name}", message.getChannel().getName()));
        if (arguments.length > 0){
            st.delete(st.length() - 1, st.length()).append(" : ");
            MissingPermissionsException e = (MissingPermissionsException) arguments[0];

            for(Permissions p : e.getMissingPermissions())
                st.append(Translator.getLabel(lg, PERMISSION_PREFIX + p.name().toLowerCase())).append(", ");
            st.delete(st.length() - 2, st.length()).append(".");
        }

        if (message.getChannel().getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.SEND_MESSAGES))
            Message.sendText(message.getChannel(), st.toString());
        else
            Message.sendText(message.getGuild().getOwner().getOrCreatePMChannel(), st.toString());
    }
}

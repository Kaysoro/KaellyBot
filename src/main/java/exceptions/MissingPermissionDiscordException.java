package exceptions;

import commands.model.Command;
import enums.Language;
import sx.blah.discord.handle.obj.IChannel;
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
        throwException(message.getChannel(), lg, (MissingPermissionsException) arguments[0]);
    }

    public void throwException(IChannel channel, Language lg, MissingPermissionsException e) {
        StringBuilder st = new StringBuilder(Translator.getLabel(lg, "exception.missing_permission")
                .replace("{channel.name}", channel.getName()));

        for(Permissions p : e.getMissingPermissions())
            st.append(Translator.getLabel(lg, PERMISSION_PREFIX + p.name().toLowerCase())).append(", ");
        st.delete(st.length() - 2, st.length()).append(".");

        if (channel.getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.SEND_MESSAGES))
            Message.sendText(channel, st.toString());
        else
            try {
                Message.sendText(channel.getGuild().getOwner().getOrCreatePMChannel(), st.toString());
            } catch (sx.blah.discord.util.DiscordException de) {
                LOG.warn("throwExpcetion", "Impossible de contacter l'administrateur de la guilde ["
                        + channel.getGuild().getName() + "].");
            }
    }
}
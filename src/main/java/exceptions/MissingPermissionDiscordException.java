package exceptions;

import commands.Command;
import data.Translator;
import discord.Message;
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

    @Override
    public void throwException(IMessage message, Command command, Object... arguments) {
        StringBuilder st = new StringBuilder("Je ne peux pas écrire dans le salon *")
                .append(message.getChannel().getName()).append("* puisqu'il me manque une ou plusieurs permissions.");
        if (arguments.length > 0){
            st.delete(st.length() - 1, st.length()).append(" : ");
            MissingPermissionsException e = (MissingPermissionsException) arguments[0];

            for(Permissions p : e.getMissingPermissions()){
                st.append(Translator.getFrenchNameFor(p).toLowerCase()).append(", ");
            }
            st.delete(st.length() - 2, st.length()).append(".");
        }

        Message.sendText(message.getGuild().getOwner().getOrCreatePMChannel(), st.toString());
    }
}

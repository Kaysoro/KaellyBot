package commands;

import data.ClientConfig;
import data.Constants;
import discord.Message;
import exceptions.ChannelNotFoundDiscordException;
import exceptions.NoSendTextPermissionDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class TalkCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(TalkCommand.class);
    private IChannel lastChan;

    public TalkCommand(){
        super(Pattern.compile("talk"),
                Pattern.compile("^(" + Constants.prefixCommand + "talk)(\\s+\\d+)?(\\s+.+)$"));
        setAdmin(true);

        this.lastChan = null;
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            String text = m.group(3).trim();

            if (m.group(2) != null){
                long value = Long.parseLong(m.group(2).trim());
                lastChan = ClientConfig.DISCORD().getChannelByID(value);
            }

            if (lastChan == null){
                new ChannelNotFoundDiscordException().throwException(message, this);
                return false;
            }

            if (lastChan.getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.SEND_MESSAGES))
                Message.sendText(lastChan, text);
            else
                new NoSendTextPermissionDiscordException().throwException(message, this);

            return true;
        }
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "talk** envoie un message à destination d'un canal spécifique.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "talk `*`text`* : envoie *text* dans le dernier canal utilisé."
                + "\n`" + Constants.prefixCommand + "talk `*canal `text`* : envoie *text* au canal spécifié (identifiant).\n";
    }
}

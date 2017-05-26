package commands;

import data.ClientConfig;
import data.Constants;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class AnnounceCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(AnnounceCommand.class);

    public AnnounceCommand(){
        super(Pattern.compile("announce"),
                Pattern.compile("^(" + Constants.prefixCommand + "announce)(\\s+-confirm)?(\\s+.+)$"));
        setAdmin(true);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            String text = m.group(3).trim();

            if (m.group(2) != null) {
                for (IGuild guild : ClientConfig.DISCORD().getGuilds())
                    if (guild.getGeneralChannel().getModifiedPermissions(ClientConfig.DISCORD().getOurUser())
                            .contains(Permissions.SEND_MESSAGES))
                        Message.sendText(guild.getGeneralChannel(), text);
                    else
                        Message.sendText(guild.getOwner().getOrCreatePMChannel(), text);

                Message.sendText(message.getChannel(), "Annonce envoyé à "
                        + ClientConfig.DISCORD().getGuilds().size() + " guilde(s).");
            }
            else
                Message.sendText(message.getChannel(), text);

            return true;
        }
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "announce** envoie une annonce à l'ensemble des guildes de Kaelly.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "announce `*`text`* : envoie *text* dans le canal."
                + "\n`" + Constants.prefixCommand + "announce `*-confirm `text`* : envoie *text* à l'ensemble des guildes.\n";
    }
}

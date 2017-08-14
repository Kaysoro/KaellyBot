package commands;

import data.ClientConfig;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class AnnounceCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(AnnounceCommand.class);

    public AnnounceCommand(){
        super("announce","(\\s+-confirm)?(\\s+.+)");
        setAdmin(true);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Matcher m = getMatcher(message);
            m.find();
            String text = m.group(2).trim();

            if (m.group(1) != null) {
                for (IGuild guild : ClientConfig.DISCORD().getGuilds())
                    if (guild.getDefaultChannel().getModifiedPermissions(ClientConfig.DISCORD().getOurUser())
                            .contains(Permissions.SEND_MESSAGES))
                        Message.sendText(guild.getDefaultChannel(), text);
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
    public String help(String prefixe) {
        return "**" + prefixe + name + "** envoie une annonce à l'ensemble des guildes de Kaelly.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + " `*`text`* : envoie *text* dans le salon."
                + "\n" + prefixe + "`"  + name + " `*-confirm `text`* : envoie *text* à l'ensemble des guildes.\n";
    }
}

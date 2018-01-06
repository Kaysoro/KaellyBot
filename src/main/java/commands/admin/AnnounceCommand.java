package commands.admin;

import commands.model.AbstractCommand;
import data.Constants;
import enums.Language;
import sx.blah.discord.util.DiscordException;
import util.ClientConfig;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class AnnounceCommand extends AbstractCommand {

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
            Language lg = Translator.getLanguageFrom(message.getChannel());
            String text = m.group(2).trim();

            if (m.group(1) != null) {
                for (IGuild guild : ClientConfig.DISCORD().getGuilds())
                    try {
                        if (guild.getDefaultChannel().getModifiedPermissions(ClientConfig.DISCORD().getOurUser())
                                .contains(Permissions.SEND_MESSAGES))
                            Message.sendText(guild.getDefaultChannel(), text);
                        else
                            Message.sendText(guild.getOwner().getOrCreatePMChannel(), text);
                    } catch (DiscordException e) {
                        LOG.warn("onReady", "Impossible de contacter l'administrateur de la guilde ["
                                + guild.getName() + "].");
                    } catch (Exception e2) {
                        LOG.warn("onReady", e2);
                    }

                Message.sendText(message.getChannel(), Translator.getLabel(lg, "announce.request.1") + " "
                        + ClientConfig.DISCORD().getGuilds().size() + " " + Translator.getLabel(lg, "announce.request.2")
                        + (ClientConfig.DISCORD().getGuilds().size() > 1?"s":"") + "."
                );
            }
            else
                Message.sendText(message.getChannel(), text);

            return true;
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "announce.help").replace("{name}", Constants.name);
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " `*`text`* : " + Translator.getLabel(lg, "announce.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " `*-confirm `text`* : " + Translator.getLabel(lg, "announce.help.detailed.2") + "\n";
    }
}

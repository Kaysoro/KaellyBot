package commands.classic;

import commands.model.AbstractCommand;
import data.Guild;
import data.Portal;
import data.Position;
import enums.Language;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import util.Message;
import exceptions.TooMuchDiscordException;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class PortalCommand extends AbstractCommand {

    private DiscordException tooMuchPortals;
    private DiscordException notFoundPortal;

    public PortalCommand(){
        super("pos", "(\\s+\\p{L}+)?(\\s+\\[?(-?\\d{1,2})\\s*[,|\\s]\\s*(-?\\d{1,2})\\]?)?(\\s+\\d{1,3})?");
        setUsableInMP(false);
        tooMuchPortals = new TooMuchDiscordException("portal");
        notFoundPortal = new NotFoundDiscordException("portal");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        if (m.group(1) == null && m.group(5) == null) { // No dimension precised
            for(Portal pos : Guild.getGuild(message.getGuild()).getPortals())
                    Message.sendEmbed(message.getChannel(), pos.getEmbedObject(lg));
        }
        else {
            List<Portal> portals = new ArrayList<>();
            if (m.group(1) != null)
                portals = getPortal(m.group(1), Guild.getGuild(message.getGuild()));
            if (portals.size() == 1) {
                if (m.group(2) != null)
                    portals.get(0).setCoordonate(Position.parse("[" + m.group(3) + "," + m.group(4) + "]"),
                            message.getAuthor().getDisplayName(message.getGuild()));
                if (m.group(5) != null)
                    portals.get(0).setUtilisation(Integer.parseInt(m.group(5).replaceAll("\\s", "")),
                            message.getAuthor().getDisplayName(message.getGuild()));

                Message.sendEmbed(message.getChannel(), portals.get(0).getEmbedObject(lg));
            }
            else if(portals.size() > 1)
                tooMuchPortals.throwException(message, this, lg);
            else
                notFoundPortal.throwException(message, this, lg);
        }
    }

    private List<Portal> getPortal(String nameProposed, Guild guild){
        nameProposed = Normalizer.normalize(nameProposed, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        nameProposed = nameProposed.replaceAll("\\W+", "");
        List<Portal> portals = new ArrayList<>();

        for(Portal portal : guild.getPortals())
            if (Normalizer.normalize(portal.getName(), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase().startsWith(nameProposed))
                    portals.add(portal);
        return portals;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "portal.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + "` : " + Translator.getLabel(lg, "portal.help.detailed.1")
                + "\n`" + prefixe + name + " `*`dimension`* : " + Translator.getLabel(lg, "portal.help.detailed.2")
                + "\n`" + prefixe + name + " `*`dimension`*` [POS, POS]` : "
                        + Translator.getLabel(lg, "portal.help.detailed.3")
                + "\n`" + prefixe + name + " `*`dimension`*` [POS, POS] `*`utilisation`* : "
                        + Translator.getLabel(lg, "portal.help.detailed.4")
                + "\n`" + prefixe + name + " `*`dimension` `utilisation`* : "
                        + Translator.getLabel(lg, "portal.help.detailed.5") + "\n";
    }
}

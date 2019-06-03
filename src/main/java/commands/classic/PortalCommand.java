package commands.classic;

import commands.model.AbstractCommand;
import data.*;
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
    private DiscordException notFoundServer;

    public PortalCommand(){
        super("pos", "(\\s+\\p{L}+)?(\\s+\\[?(-?\\d{1,2})\\s*[,|\\s]\\s*(-?\\d{1,2})\\]?)?(\\s+\\d{1,3})?");
        setUsableInMP(false);
        tooMuchPortals = new TooMuchDiscordException("portal");
        notFoundPortal = new NotFoundDiscordException("portal");
        notFoundServer = new NotFoundDiscordException("server");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        ServerDofus server = Guild.getGuild(message.getGuild()).getServerDofus();

        if (server != null){
            if (m.group(1) == null && m.group(5) == null) { // No dimension precised
                for(Position pos : server.getPositions())
                    Message.sendEmbed(message.getChannel(), pos.getEmbedObject(lg));
            }
            else {
                List<Position> positions = new ArrayList<>();
                if (m.group(1) != null)
                    positions = getPosition(m.group(1), server);
                if (positions.size() == 1) {
                    if (m.group(2) != null)
                        positions.get(0).setCoordinate(Coordinate.parse("[" + m.group(3) + "," + m.group(4) + "]"),
                                message.getAuthor().getDisplayName(message.getGuild()));
                    if (m.group(5) != null)
                        positions.get(0).setUtilisation(Integer.parseInt(m.group(5).replaceAll("\\s", "")),
                                message.getAuthor().getDisplayName(message.getGuild()));

                    Message.sendEmbed(message.getChannel(), positions.get(0).getEmbedObject(lg));
                }
                else if(positions.size() > 1)
                    tooMuchPortals.throwException(message, this, lg);
                else
                    notFoundPortal.throwException(message, this, lg);
            }
        }
        else
            notFoundServer.throwException(message, this, lg);
    }

    private List<Position> getPosition(String nameProposed, ServerDofus server){
        nameProposed = Normalizer.normalize(nameProposed, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        nameProposed = nameProposed.replaceAll("\\W+", "");
        List<Position> positions = new ArrayList<>();

        for(Position position : server.getPositions())
            if (Normalizer.normalize(position.getPortal().getName(), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase().startsWith(nameProposed))
                positions.add(position);
        return positions;
    }

    @Override
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "portal.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return help(lg, prefix)
                + "\n`" + prefix + name + "` : " + Translator.getLabel(lg, "portal.help.detailed.1")
                + "\n`" + prefix + name + " `*`dimension`* : " + Translator.getLabel(lg, "portal.help.detailed.2")
                + "\n`" + prefix + name + " `*`dimension`*` [POS, POS]` : "
                        + Translator.getLabel(lg, "portal.help.detailed.3")
                + "\n`" + prefix + name + " `*`dimension`*` [POS, POS] `*`utilisation`* : "
                        + Translator.getLabel(lg, "portal.help.detailed.4")
                + "\n`" + prefix + name + " `*`dimension` `utilisation`* : "
                        + Translator.getLabel(lg, "portal.help.detailed.5")
                + "\n";
    }
}
package commands.classic;

import commands.model.AbstractLegacyCommand;
import data.Coordinate;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import enums.Transport;
import enums.Language;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class DistanceCommand extends AbstractLegacyCommand {

    public DistanceCommand(){
        super("dist", "\\s+\\[?(-?\\d{1,2})\\s*[,|\\s]\\s*(-?\\d{1,2})\\]?");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        Coordinate coordinate = new Coordinate(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
        StringBuilder st = new StringBuilder();
        Transport zaap = null;
        Transport transportLimited = null;

        if(! coordinate.isNull()) {
            double minDist = Double.MAX_VALUE;
            double minDistLimited = Double.MAX_VALUE;
            for (Transport transport : Transport.values()) {
                double tmp = transport.getCoordinate().getDistance(coordinate);
                if (transport.isFreeAccess() && (zaap == null || minDist > tmp)){
                    zaap = transport;
                    minDist = tmp;
                }
                if (! transport.isFreeAccess() && (transportLimited == null || minDistLimited > tmp)){
                    transportLimited = transport;
                    minDistLimited = tmp;
                }
            }

            st.append(Translator.getLabel(lg, "distance.request.1")).append(" ").append(zaap.toDiscordString(lg));
            if (minDist > minDistLimited)
                st.append("\n").append(Translator.getLabel(lg, "distance.request.2")).append(" ").append(transportLimited.toDiscordString(lg));
        }
        else
            st.append(Translator.getLabel(lg, "distance.request.3"));

        message.getChannel().flatMap(chan -> chan.createMessage(st.toString())).subscribe();
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "distance.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " [POS, POS]` : " + Translator.getLabel(lg, "distance.help") + "\n";
    }
}

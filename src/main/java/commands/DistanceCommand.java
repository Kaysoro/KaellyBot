package commands;

import data.Position;
import data.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class DistanceCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(DistanceCommand.class);

    public DistanceCommand(){
        super("dist", "\\s+\\[?(-?\\d{1,2})\\s*[,|\\s]\\s*(-?\\d{1,2})\\]?");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Matcher m = getMatcher(message);
            m.find();
            Position position = new Position(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            StringBuilder st = new StringBuilder();
            Transport zaap = null;
            Transport transportLimited = null;

            if(! position.isNull()) {
                double minDist = Double.MAX_VALUE;
                double minDistLimited = Double.MAX_VALUE;
                for (Transport transport : Transport.getTransports()) {
                    double tmp = transport.getPosition().getDistance(position);
                    if (transport.isFreeAccess() && (zaap == null || minDist > tmp)){
                        zaap = transport;
                        minDist = tmp;
                    }
                    if (! transport.isFreeAccess() && (transportLimited == null || minDistLimited > tmp)){
                        transportLimited = transport;
                        minDistLimited = tmp;
                    }
                }

                st.append("Zaap le plus proche (à vol d'oiseau) : ").append(zaap.toString());
                if (minDist < minDistLimited)
                    st.append("\nTransport privé le plus proche (à vol d'oiseau) : ").append(transportLimited.toString());
            }
            else {
                st.append("La position n'est pas correcte. Recommencez ! :)");
            }
            Message.sendText(message.getChannel(), st.toString());
        }
        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** donne le transport le plus proche de la position indiquée.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + "` [POS, POS]` : donne le transport le plus proche (à vol d'oiseau) de la position indiquée.\n";
    }
}

package commands;

import data.Constants;
import data.Guild;
import data.Portal;
import discord.Message;
import exceptions.InDeveloppmentException;
import exceptions.PortalNotFoundException;
import exceptions.TooMuchPossibilitiesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class PortalCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(PortalCommand.class);

    public PortalCommand(){
        super(Pattern.compile("pos"),
                Pattern.compile("^(" + Constants.prefixCommand + "pos)(\\s+-reset|\\s+-update)?(\\s+\\p{L}+)?(\\s+\\[?(-?\\d{1,2})\\s*[,|\\s]\\s*(-?\\d{1,2})\\]?)?(\\s+\\d{1,3})?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            if (m.group(2) != null && m.group(2).matches("\\s+-update")) { // Update
                LOG.info("Gathering data from websites ...");
                //TODO update from website

                new InDeveloppmentException().throwException(message, this);
                //Message.send(message.getChannel(), "Téléchargement des positions des dimensions divines terminé.");
            }
            else if (m.group(2) == null && m.group(3) == null && m.group(7) == null) { // No dimension precised
                StringBuilder st = new StringBuilder();
                for(Portal pos : Guild.getGuilds().get(message.getGuild().getID()).getPortals())
                        st.append(pos);

                Message.send(message.getChannel(), st.toString());
            }
            else {
                List<Portal> portals = new ArrayList<Portal>();
                if (m.group(3) != null)
                    portals = getPortal(m.group(3), Guild.getGuilds().get(message.getGuild().getID()));
                if (portals.size() == 1) {
                    if (m.group(2) != null && m.group(2).matches("\\s+-reset"))
                        portals.get(0).setCoordonate(null);
                    else {
                        if (m.group(4) != null)
                            portals.get(0).setCoordonate("[" + m.group(5) + "," + m.group(6) + "]");
                        if (m.group(7) != null)
                          portals.get(0).setUtilisation(Integer.parseInt(m.group(7).replaceAll("\\s", "")));
                    }

                    Message.send(message.getChannel(), portals.get(0).toString());
                }
                else if(portals.size() > 1)
                    new TooMuchPossibilitiesException().throwException(message, this);
                else
                    new PortalNotFoundException().throwException(message, this);
            }
        }
        return false;
    }

    public List<Portal> getPortal(String nameProposed, Guild guild){
        nameProposed = Normalizer.normalize(nameProposed, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        nameProposed = nameProposed.replaceAll("\\W+", "");
        List<Portal> portals = new ArrayList<Portal>();

        for(Portal portal : guild.getPortals())
            if (Normalizer.normalize(portal.getName(), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase().startsWith(nameProposed))
                    portals.add(portal);
        return portals;
    }

    @Override
    public boolean isUsableInMP() {
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "pos** donne la position des portails dimension.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "pos` : donne la position de tous les portails."
                + "\n`" + Constants.prefixCommand + "pos `*`dimension`* : donne la position du portail de la dimension désirée."
                + "\n`" + Constants.prefixCommand + "pos `*`dimension`*` [POS, POS]` : met à jour la position du portail de la dimension spécifiée."
                + "\n`" + Constants.prefixCommand + "pos `*`dimension`*` [POS, POS] `*`nombre d'uti.`* : met à jour la position et le nombre d'utilisation"
                + " de la dimension spécifiée."
                + "\n`" + Constants.prefixCommand + "pos -reset `*`dimension`* : supprime les informations de la dimension spécifiée."
                + "\n`" + Constants.prefixCommand + "pos -update` : télécharge les positions des portails de diverses sites web.\n";
    }
}

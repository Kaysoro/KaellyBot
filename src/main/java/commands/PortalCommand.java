package commands;

import data.Constants;
import data.Portal;
import discord.Message;
import exceptions.InDeveloppmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class PortalCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(PortalCommand.class);

    public PortalCommand(){
        super(Pattern.compile("pos"),
                Pattern.compile("^(" + Constants.prefixCommand + "pos)(\\s+\\w+)?(\\s+\\[?(-?\\d{1,2})\\s*[,|\\s]\\s*(-?\\d{1,2})\\]?)?(\\s+\\d{1,3})?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            if (m.group(2) != null && m.group(2).matches("\\W+update")) { // Update
                LOG.info("Gathering data from websites ...");
                //TODO update from website

                new InDeveloppmentException().throwException(message, this);
                //Message.send(message.getChannel(), "Téléchargement des positions des dimensions divines terminé.");

                return false;
            }
            else if (m.group(2) == null) { // No dimension precised
                StringBuilder st = new StringBuilder();
                for(Portal pos : Portal.getPortals())
                        st.append(pos);

                Message.send(message.getChannel(), st.toString());

                return false;
            }
            else {
                Portal portal = getPortal(m.group(2));
                if (portal != null) {
                    LOG.info(m.group(3));
                    if (m.group(3) != null) {
                        LOG.info(m.group(4) + ":" + m.group(5));
                        portal.setCoordonate("[" + m.group(4) + "," + m.group(5) + "]");
                    }
                    if (m.group(6) != null)
                      portal.setUtilisation(Integer.parseInt(m.group(6).replaceAll("\\s", "")));

                    Message.send(message.getChannel(), portal.toString());

                    return false;
                }
            }
        }
        return false;
    }

    public Portal getPortal(String nameProposed){
        nameProposed = Normalizer.normalize(nameProposed, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        nameProposed = nameProposed.replaceAll("\\W+", "");

        for(Portal portal : Portal.getPortals())
            if (Normalizer.normalize(portal.getName(), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase().startsWith(nameProposed))
                return portal;
        return null;
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
                + "\n`" + Constants.prefixCommand + "pos update` : télécharge les positions des portails de diverses sites web.\n";
    }
}

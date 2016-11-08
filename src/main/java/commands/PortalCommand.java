package commands;

import data.ClientConfig;
import data.Constants;
import data.Portal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class PortalCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(PortalCommand.class);

    public PortalCommand(){
        super(Pattern.compile("pos"),
                Pattern.compile("^(!pos)(\\W+\\w+)?[\\W+\\[?(-?\\d{1,2})\\W+(-?\\d{1,2})\\]?]?(\\W+\\d{1,3})?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            if (m.group(2) != null && m.group(2).matches("\\W+update")) { // Update
                LOG.info("Gathering data from websites ...");
                //TODO update from website

                RequestBuffer.request(() -> {
                    try {
                        new MessageBuilder(ClientConfig.CLIENT())
                                .withChannel(message.getChannel())
                                .withContent("Téléchargement des positions des dimensions divines terminé.")
                                .build();
                    } catch (DiscordException e1) {
                        LOG.error(e1.getErrorMessage());
                    } catch (MissingPermissionsException e1) {
                        LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                    }
                    return null;
                });

                return false;
            }
            else if (m.group(2) == null) { // No dimension precised
                StringBuilder st = new StringBuilder();
                for(Portal pos : Portal.getPortals())
                        st.append(pos);

                RequestBuffer.request(() -> {
                    try {
                        new MessageBuilder(ClientConfig.CLIENT())
                                .withChannel(message.getChannel())
                                .withContent(st.toString())
                                .build();
                    } catch (DiscordException e1) {
                        LOG.error(e1.getErrorMessage());
                    } catch (MissingPermissionsException e1) {
                        LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                    }
                    return null;
                });
                return false;
            }
            else {
                Portal portal = getPortal(m.group(2));
                if (portal != null) {
                    if (m.group(3) != null && m.group(4) != null)
                        portal.setCoordonate("[" + m.group(3) + "," + m.group(4) + "]");
                    //if (m.group(5) != null) TODO
                    //  portal.setUtilisation(Integer.parseInt(m.group(5)));

                    RequestBuffer.request(() -> {
                        try {
                            new MessageBuilder(ClientConfig.CLIENT())
                                    .withChannel(message.getChannel())
                                    .withContent(portal.toString())
                                    .build();
                        } catch (DiscordException e1) {
                            LOG.error(e1.getErrorMessage());
                        } catch (MissingPermissionsException e1) {
                            LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                        }
                        return null;
                    });
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
        return "**!pos** donne la position des portails dimension.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`!pos` : donne la position de tous les portails."
                + "\n`!pos `*`dimension`* : donne la position du portail de la dimension désirée."
                + "\n`!pos `*`dimension`*` [POS, POS]` : met à jour la position du portail de la dimension spécifiée."
                + "\n`!pos `*`dimension`*` [POS, POS] `*`nombre d'uti.`* : met à jour la position et le nombre d'utilisation"
                + " de la dimension spécifiée."
                + "\n`!pos update` : télécharge les positions des portails de diverses sites web.\n";
    }
}

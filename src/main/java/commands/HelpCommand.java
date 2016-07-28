package commands;

import data.ClientConfig;
import data.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class HelpCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand(){
        super(Pattern.compile("!help"),
                Pattern.compile("^(!help)\\W+([!?][.*])$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            StringBuilder st = new StringBuilder();
            boolean argumentFound = content != null || ! content.equals("");
            for(Command command : commands)
                if (! argumentFound)
                    st.append(command.help());
                else if (command.getName().matcher(content).find()){
                    st.append(command.help());
                    break;
                }

            if (argumentFound && st.length() == 0)
                st.append("Aucune commande ne répond au nom de " + content + ".");

            // Envoyer le message en privée
                RequestBuffer.request(() -> {
                    try {
                        new MessageBuilder(ClientConfig.CLIENT())
                                .withChannel(message.getChannel())
                                .withContent(st.toString())
                                .build();
                    } catch (DiscordException e) {
                        LOG.error(e.getErrorMessage());
                    } catch (MissingPermissionsException e) {
                        LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                    }
                    return null;
                });
                return true;
            }
            return false;
        }

    @Override
    public String help() {
        //TODO HTML ?
        return "Help est la commande permettant d'afficher les descriptions de chaque commande que possède "
                + Constants.name + "."
                + "`!help` : affiche chaque commande."
                + "`!help _command_` : affiche uniquement la commande spécifiée.";
    }
}

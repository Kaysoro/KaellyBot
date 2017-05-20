package commands;

import data.Constants;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class AdminCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(AdminCommand.class);

    public AdminCommand(){
        super(Pattern.compile("admin"),
                Pattern.compile("^(" + Constants.prefixCommand + "admin)(\\s+" + Constants.prefixCommand + "?(.+))?$"));
        setAdmin(true);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            if (message.getAuthor().getStringID().equals(Constants.author)) {
                StringBuilder st = new StringBuilder();
                boolean argumentFound = m.group(2) != null && m.group(2).replaceAll("^\\s+", "").length() > 0;
                for (Command command : CommandManager.getCommands())
                    if (command.isAdmin()) {
                        if (!argumentFound)
                            st.append(command.help()).append("\n");
                        else if (command.getName().matcher(m.group(2)).find()) {
                            st.append(command.helpDetailed());
                            break;
                        }
                    }

                if (argumentFound && st.length() == 0)
                    st.append("Aucune commande ne répond au nom de *")
                            .append(m.group(2).replaceAll("^\\W+", ""))
                            .append("*.");

                Message.sendText(message.getChannel(), st.toString());
                return true;
            }
        }
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "admin** explique le fonctionnement de chaque commande admin de " + Constants.name + ".";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "admin` : explique succintement chaque commande admin."
                + "\n`" + Constants.prefixCommand + "admin `*`command`* : explique de façon détaillée la commande admin spécifiée.\n";
    }
}

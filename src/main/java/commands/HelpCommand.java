package commands;

import data.Constants;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class HelpCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand(){
        super("help","(\\s+.+)?");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            String prefixe = getPrefixMdEscaped(message);
            Matcher m = getMatcher(message);
            m.find();
            StringBuilder st = new StringBuilder();
            boolean argumentFound = m.group(1) != null && m.group(1).replaceAll("^\\s+", "").length() > 0;
            for(Command command : CommandManager.getCommands())
                if (command.isPublic() && ! command.isAdmin()){
                    if (! argumentFound)
                        st.append(command.help(prefixe)).append("\n");
                    else if (command.getName().equals(m.group(1).trim())) {
                        st.append(command.helpDetailed(prefixe));
                        break;
                    }
                }

            if (argumentFound && st.length() == 0)
                st.append("Aucune commande ne répond au nom de *")
                        .append(m.group(1).trim())
                        .append("*.");

                Message.sendText(message.getChannel(), st.toString());
                return true;
            }
            return false;
        }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** explique le fonctionnement de chaque commande de " + Constants.name + ".";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + "` : explique succintement chaque commande."
                + "\n" + prefixe + "`"  + name + " `*`command`* : explique de façon détaillée la commande spécifiée.\n";
    }
}

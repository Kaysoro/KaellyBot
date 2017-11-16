package commands;

import data.Constants;
import enums.Language;
import exceptions.CommandNotFoundDiscordException;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class AdminCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(AdminCommand.class);

    public AdminCommand(){
        super("admin", "(\\s+.+)?");
        setAdmin(true);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            String prefixe = getPrefixMdEscaped(message);
            Language lg = Translator.getLanguageFrom(message.getChannel());
            Matcher m = getMatcher(message);
            m.find();
            StringBuilder st = new StringBuilder();
            boolean argumentFound = m.group(1) != null && m.group(1).replaceAll("^\\s+", "").length() > 0;
            for (Command command : CommandManager.getCommands())
                if (command.isAdmin()) {
                    if (!argumentFound)
                        st.append(command.help(lg, prefixe)).append("\n");
                    else if (command.getName().equals(m.group(1).trim())) {
                        st.append(command.helpDetailed(lg, prefixe));
                        break;
                    }
                }

            if (argumentFound && st.length() == 0)
                new CommandNotFoundDiscordException().throwException(message, this);
            else
                Message.sendText(message.getChannel(), st.toString());
            return true;
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "admin.help").replace("name", Constants.name);
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + "` : " + Translator.getLabel(lg, "admin.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " `*`command`* : " + Translator.getLabel(lg, "admin.help.detailed.2") + "\n";
    }
}

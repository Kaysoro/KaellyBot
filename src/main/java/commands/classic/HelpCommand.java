package commands.classic;

import commands.CommandManager;
import commands.model.AbstractCommand;
import commands.model.Command;
import data.Constants;
import data.Guild;
import enums.Language;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import util.Message;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class HelpCommand extends AbstractCommand {

    public final static String NAME = "help";

    private DiscordException notFoundCmd;

    public HelpCommand(){
        super(NAME,"(\\s+.+)?");
        notFoundCmd = new NotFoundDiscordException("cmd");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        String prefix = getPrefixMdEscaped(message);
        StringBuilder st = new StringBuilder();
        boolean argumentFound = m.group(1) != null && m.group(1).replaceAll("^\\s+", "").length() > 0;
        for(Command command : CommandManager.getCommands())
            if (command.isPublic() && ! command.isAdmin()
                    && (message.getChannel().isPrivate() || ! command.isForbidden(Guild.getGuild(message.getGuild())))){
                if (! argumentFound)
                    st.append(command.help(lg, prefix)).append("\n");
                else if (command.getName().equals(m.group(1).trim())) {
                    st.append(command.helpDetailed(lg, prefix));
                    break;
                }
            }

        if (argumentFound && st.length() == 0)
            notFoundCmd.throwException(message, this, lg);
        else
            Message.sendText(message.getChannel(), st.toString());
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "help.help").replace("{name}", Constants.name);
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + "` : " + Translator.getLabel(lg, "help.help.detailed.1")
                + "\n`" + prefixe + name + " `*`command`* : " + Translator.getLabel(lg, "help.help.detailed.2") + "\n";
    }
}

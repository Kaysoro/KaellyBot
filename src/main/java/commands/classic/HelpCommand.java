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

import java.util.ArrayList;
import java.util.List;
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
        List<String> messages = new ArrayList<>();

        boolean argumentFound = m.group(1) != null && m.group(1).replaceAll("^\\s+", "").length() > 0;
        for(Command command : CommandManager.getCommands())
            if (command.isPublic() && ! command.isAdmin() && (!command.isHidden() || argumentFound)
                    && (message.getChannel().isPrivate() || ! command.isForbidden(Guild.getGuild(message.getGuild())))){
                if (! argumentFound) {
                    String helpCmd = command.help(lg, prefix) + "\n";
                    if (st.length() + helpCmd.length() > IMessage.MAX_MESSAGE_LENGTH){
                        messages.add(st.toString());
                        st.setLength(0);
                    }
                    st.append(helpCmd);
                }
                else if (command.getName().equals(m.group(1).trim())) {
                    st.append(command.helpDetailed(lg, prefix));
                    break;
                }
            }

        if (st.length() > 0)
            messages.add(st.toString());

        if (argumentFound && messages.isEmpty())
            notFoundCmd.throwException(message, this, lg);
        else
            for(String msg : messages)
                Message.sendText(message.getChannel(), msg);
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

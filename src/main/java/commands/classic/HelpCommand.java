package commands.classic;

import commands.CommandManager;
import commands.model.AbstractLegacyCommand;
import commands.model.LegacyCommand;
import data.Guild;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.PrivateChannel;
import enums.Language;
import util.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class HelpCommand extends AbstractLegacyCommand {

    public final static String NAME = "help";

    public HelpCommand(){
        super(NAME,"(\\s+.+)?");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        String prefix = getPrefixMdEscaped(message);
        StringBuilder st = new StringBuilder();
        List<String> messages = new ArrayList<>();

        boolean argumentFound = m.group(1) != null && m.group(1).replaceAll("^\\s+", "").length() > 0;
        for(LegacyCommand command : CommandManager.getCommands())
            if (command.isPublic() && ! command.isAdmin() && (!command.isHidden() || argumentFound)
                    && (message.getChannel().block() instanceof PrivateChannel
                    || ! command.isForbidden(Guild.getGuild(message.getGuild().block())))){
                if (! argumentFound) {
                    String helpCmd = command.help(lg, prefix) + "\n";
                    if (st.length() + helpCmd.length() > Message.MAX_CONTENT_LENGTH){
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
            Translator.getLabel(lg, "help.request");
        else
            for(String msg : messages)
                message.getChannel().flatMap(chan -> chan.createMessage(msg)).subscribe();
    }

    @Override
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "help.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return Translator.getLabel(lg, "help.request");
    }
}

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
public class ParrotCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(ParrotCommand.class);

    public ParrotCommand(){
        super(Pattern.compile("parrot"),
        Pattern.compile("^(" + Constants.prefixCommand + "parrot)\\s+(.*)$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Message.sendText(message.getChannel(), m.group(2));
            return true;
        }
        return false;
    }

    @Override
    public boolean isUsableInMP() {
        return true;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "parrot** renvoit le message envoyé en argument, tel un perroquet.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "parrot `*`message`* : renvoit le message spécifié.\n";
    }
}

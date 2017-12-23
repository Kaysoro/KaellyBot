package commands.admin;

import commands.model.AbstractCommand;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.ClientConfig;
import util.Message;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 23/12/2017.
 */
public class StatCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(StatCommand.class);

    public StatCommand(){
        super("stat","");
        setAdmin(true);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Matcher m = getMatcher(message);
            m.find();
            Language lg = Translator.getLanguageFrom(message.getChannel());
            String answer = Translator.getLabel(lg, "stat.request")
                    .replace("{guilds.size}", String.valueOf(ClientConfig.DISCORD().getGuilds().size()))
                    .replace("{users.size}", String.valueOf(ClientConfig.DISCORD().getUsers().size()));
            Message.sendText(message.getChannel(), answer);
        }

        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "stat.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe);
    }
}

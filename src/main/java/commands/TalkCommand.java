package commands;

import enums.Language;
import util.ClientConfig;
import util.Message;
import exceptions.ChannelNotFoundDiscordException;
import exceptions.NoSendTextPermissionDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class TalkCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(TalkCommand.class);
    private IChannel lastChan;

    public TalkCommand(){
        super("talk", "(\\s+\\d+)?(\\s+.+)");
        setAdmin(true);

        this.lastChan = null;
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Language lg = Translator.getLanguageFrom(message.getChannel());
            Matcher m = getMatcher(message);
            m.find();
            String text = m.group(2).trim();

            if (m.group(1) != null){
                long value = Long.parseLong(m.group(1).trim());
                lastChan = ClientConfig.DISCORD().getChannelByID(value);
            }

            if (lastChan == null){
                new ChannelNotFoundDiscordException().throwException(message, this, lg);
                return false;
            }

            if (lastChan.getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.SEND_MESSAGES))
                Message.sendText(lastChan, text);
            else
                new NoSendTextPermissionDiscordException().throwException(message, this, lg);

            return true;
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "talk.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " `*`text`* : " + Translator.getLabel(lg, "talk.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " `*salon `text`* : " + Translator.getLabel(lg, "talk.help.detailed.2") + "\n";
    }
}

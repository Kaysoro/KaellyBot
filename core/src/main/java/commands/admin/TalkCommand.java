package commands.admin;

import commands.model.AbstractCommand;
import enums.Language;
import exceptions.BasicDiscordException;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import util.ClientConfig;
import util.Message;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class TalkCommand extends AbstractCommand {

    private IChannel lastChan;
    private DiscordException notFoundChan;

    public TalkCommand(){
        super("talk", "(\\s+\\d+)?(\\s+.+)");
        setAdmin(true);

        this.lastChan = null;
        notFoundChan = new NotFoundDiscordException("chan");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        String text = m.group(2).trim();

        if (m.group(1) != null){
            long value = Long.parseLong(m.group(1).trim());
            lastChan = ClientConfig.DISCORD().getChannelByID(value);
        }

        if (lastChan == null)
            notFoundChan.throwException(message, this, lg);
        else if (lastChan.getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.SEND_MESSAGES))
            Message.sendText(lastChan, text);
        else
            BasicDiscordException.NO_SEND_TEXT_PERMISSION.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "talk.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`text`* : " + Translator.getLabel(lg, "talk.help.detailed.1")
                + "\n`" + prefixe + name + " channel `*`text`* : " + Translator.getLabel(lg, "talk.help.detailed.2") + "\n";
    }
}

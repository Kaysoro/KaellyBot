package commands.config;

import commands.model.AbstractCommand;
import enums.Language;
import exceptions.BasicDiscordException;
import finders.AlmanaxCalendar;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;
import util.Translator;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class AlmanaxAutoCommand extends AbstractCommand {

    public AlmanaxAutoCommand(){
        super("almanax-auto", "(\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)");
        setUsableInMP(false);
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        if (isUserHasEnoughRights(message)) {
            if (m.group(1).matches("\\s+true") || m.group(1).matches("\\s+0") || m.group(1).matches("\\s+on"))
                if (!AlmanaxCalendar.getAlmanaxCalendars().containsKey(message.getChannel().getStringID())) {
                    new AlmanaxCalendar(message.getGuild().getStringID(), message.getChannel().getStringID()).addToDatabase();
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "almanax-auto.request.1"));
                } else
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "almanax-auto.request.2"));
            else if (m.group(1).matches("\\s+false") || m.group(1).matches("\\s+1") || m.group(1).matches("\\s+off"))
                if (AlmanaxCalendar.getAlmanaxCalendars().containsKey(message.getChannel().getStringID())) {
                    AlmanaxCalendar.getAlmanaxCalendars().get(message.getChannel().getStringID()).removeToDatabase();
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "almanax-auto.request.3"));
                } else
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "almanax-auto.request.4"));
        } else
            BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "almanax-auto.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " true` : " + Translator.getLabel(lg, "almanax-auto.help.detailed.1")
                + "\n`" + prefixe + name + " false` : " + Translator.getLabel(lg, "almanax-auto.help.detailed.2") + "\n";
    }
}

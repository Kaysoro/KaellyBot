package commands.classic;

import commands.model.AbstractCommand;
import data.Almanax;
import enums.Language;
import finders.AlmanaxCalendar;
import util.Message;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.io.IOException;
import java.lang.Exception;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class AlmanaxCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(AlmanaxCommand.class);
    private DiscordException notFound;
    private DiscordException incorrectDateFormat;
    private DiscordException noEnoughRights;

    public AlmanaxCommand(){
        super("almanax", "(\\s+\\d{2}/\\d{2}/\\d{4}|\\s+\\+\\d|\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)?");
        notFound = new BasicDiscordException("exception.basic.almanax");
        incorrectDateFormat = new BasicDiscordException("exception.basic.incorrect_date_format");
        noEnoughRights = new BasicDiscordException("exception.basic.no_enough_rights");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Language lg = Translator.getLanguageFrom(message.getChannel());
            try {
                Date date = new Date();
                Matcher m = getMatcher(message);
                m.find();

                if (m.group(1) != null && (m.group(1).matches("\\s+true") || m.group(1).matches("\\s+0") || m.group(1).matches("\\s+on")
                        || m.group(1).matches("\\s+false") || m.group(1).matches("\\s+1") || m.group(1).matches("\\s+off"))) {
                    if (! message.getChannel().isPrivate()){
                        if (isUserHasEnoughRights(message)) {
                            if (m.group(1).matches("\\s+true") || m.group(1).matches("\\s+0") || m.group(1).matches("\\s+on"))
                                if (!AlmanaxCalendar.getAlmanaxCalendars().containsKey(message.getChannel().getStringID())) {
                                    new AlmanaxCalendar(message.getGuild().getStringID(), message.getChannel().getStringID()).addToDatabase();
                                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "almanax.request.1"));
                                } else
                                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "almanax.request.2"));
                            else if (m.group(1).matches("\\s+false") || m.group(1).matches("\\s+1") || m.group(1).matches("\\s+off"))
                                if (AlmanaxCalendar.getAlmanaxCalendars().containsKey(message.getChannel().getStringID())) {
                                    AlmanaxCalendar.getAlmanaxCalendars().get(message.getChannel().getStringID()).removeToDatabase();
                                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "almanax.request.3"));
                                } else
                                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "almanax.request.4"));
                        } else
                            noEnoughRights.throwException(message, this, lg);
                    } else
                        notUsableInMp.throwException(message, this, lg);
                }

                else if (m.group(1) != null && m.group(1).matches("\\s+\\+\\d")) {
                    int number = Integer.parseInt(m.group(1).replaceAll("\\s+\\+", ""));
                    Message.sendEmbed(message.getChannel(), Almanax.getGroupedObject(lg, new Date(), number));
                } else {
                    if (m.group(1) != null && m.group(1).matches("\\s+\\d{2}/\\d{2}/\\d{4}"))
                        date = Almanax.discordToBot.parse(m.group(1));
                    Almanax almanax = Almanax.get(lg, date);
                    Message.sendEmbed(message.getChannel(), almanax.getMoreEmbedObject(lg));
                }

            } catch (ParseException e) {
               incorrectDateFormat.throwException(message, this, lg);
                return false;
            } catch (IOException e) {
                ExceptionManager.manageIOException(e, message, this, lg, notFound);
            } catch (Exception e) {
                ExceptionManager.manageException(e, message, this, lg);
            }
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "almanax.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`" + name + "` : " + Translator.getLabel(lg, "almanax.help.detailed.1")
                + "\n" + prefixe + "`" + name + " `*`dd/mm/yyyy`* : " + Translator.getLabel(lg, "almanax.help.detailed.2")
                + "\n" + prefixe + "`" + name + " `*`+days`* : " + Translator.getLabel(lg, "almanax.help.detailed.3")
                + "\n" + prefixe + "`"  + name + " true` : " + Translator.getLabel(lg, "almanax.help.detailed.4")
                + "\n" + prefixe + "`"  + name + " false` : " + Translator.getLabel(lg, "almanax.help.detailed.5") + "\n";
    }
}

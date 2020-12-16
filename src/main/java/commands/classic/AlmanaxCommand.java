package commands.classic;

import commands.model.AbstractCommand;
import data.Almanax;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import enums.Language;
import exceptions.*;
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

    public AlmanaxCommand(){
        super("almanax", "(\\s+\\d{2}/\\d{2}/\\d{4}|\\s+\\+\\d)?");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        try {
            Date date = new Date();

            if (m.group(1) != null && m.group(1).matches("\\s+\\+\\d")) {
                int number = Integer.parseInt(m.group(1).replaceAll("\\s+\\+", ""));
                final Date DATE = date;
                message.getChannel().flatMap(chan -> chan.createEmbed(spec -> {
                    try {
                        Almanax.decorateGroupedObject(spec, lg, DATE, number);
                    } catch (IOException e) {
                        ExceptionManager.manageIOException(e, message, this, lg, BasicDiscordException.ALMANAX);
                    }
                })).subscribe();
            } else {
                if (m.group(1) != null && m.group(1).matches("\\s+\\d{2}/\\d{2}/\\d{4}"))
                    date = Almanax.discordToBot.parse(m.group(1));
                Almanax almanax = Almanax.get(lg, date);
                message.getChannel().flatMap(chan -> chan.createEmbed(spec -> almanax.decorateMoreEmbedObject(spec, lg)))
                        .subscribe();
            }

        } catch (ParseException e) {
            BasicDiscordException.INCORRECT_DATE_FORMAT.throwException(message, this, lg);
        } catch (IOException e) {
            ExceptionManager.manageIOException(e, message, this, lg, BasicDiscordException.ALMANAX);
        } catch (Exception e) {
            ExceptionManager.manageException(e, message, this, lg);
        }
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "almanax.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + "` : " + Translator.getLabel(lg, "almanax.help.detailed.1")
                + "\n`" + prefixe + name + " `*`dd/mm/yyyy`* : " + Translator.getLabel(lg, "almanax.help.detailed.2")
                + "\n`" + prefixe + name + " `*`+days`* : " + Translator.getLabel(lg, "almanax.help.detailed.3") + "\n";
    }
}

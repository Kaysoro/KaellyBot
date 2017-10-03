package commands;

import data.Almanax;
import data.AlmanaxCalendar;
import data.User;
import discord.Message;
import exceptions.*;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import java.io.IOException;
import java.lang.Exception;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class AlmanaxCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(AlmanaxCommand.class);

    public AlmanaxCommand(){
        super("almanax", "(\\s+\\d{2}/\\d{2}/\\d{4}|\\s+\\+\\d|\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)?");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            try {
                Date date = new Date();
                Matcher m = getMatcher(message);
                m.find();

                if (m.group(1) != null && (m.group(1).matches("\\s+true") || m.group(1).matches("\\s+0") || m.group(1).matches("\\s+on")
                        || m.group(1).matches("\\s+false") || m.group(1).matches("\\s+1") || m.group(1).matches("\\s+off"))) {
                    if (! message.getChannel().isPrivate()){
                        User user = User.getUsers().get(message.getGuild().getStringID())
                                .get(message.getAuthor().getStringID());
                        if (user.getRights() >= User.RIGHT_MODERATOR) {
                            if (m.group(1).matches("\\s+true") || m.group(1).matches("\\s+0") || m.group(1).matches("\\s+on"))
                                if (!AlmanaxCalendar.getAlmanaxCalendars().containsKey(message.getChannel().getStringID())) {
                                    new AlmanaxCalendar(message.getGuild().getStringID(), message.getChannel().getStringID()).addToDatabase();
                                    Message.sendText(message.getChannel(), "L'almanax sera automatiquement posté ici.");
                                } else
                                    Message.sendText(message.getChannel(), "L'almanax est déjà posté ici.");
                            else if (m.group(1).matches("\\s+false") || m.group(1).matches("\\s+1") || m.group(1).matches("\\s+off"))
                                if (AlmanaxCalendar.getAlmanaxCalendars().containsKey(message.getChannel().getStringID())) {
                                    AlmanaxCalendar.getAlmanaxCalendars().get(message.getChannel().getStringID()).removeToDatabase();
                                    Message.sendText(message.getChannel(), "L'almanax ne sera plus posté ici.");
                                } else
                                    Message.sendText(message.getChannel(), "L'almanax n'est pas posté ici.");
                        } else
                            new NotEnoughRightsDiscordException().throwException(message, this);
                    } else
                        new NotUsableInMPDiscordException().throwException(message, this);
                }

                else if (m.group(1) != null && m.group(1).matches("\\s+\\+\\d")) {
                    int number = Integer.parseInt(m.group(1).replaceAll("\\s+\\+", ""));
                    Message.sendEmbed(message.getChannel(), Almanax.getGroupedObject(new Date(), number));
                } else {
                    if (m.group(1) != null && m.group(1).matches("\\s+\\d{2}/\\d{2}/\\d{4}"))
                        date = Almanax.discordToBot.parse(m.group(1));
                    Almanax almanax = Almanax.get(date);
                    Message.sendEmbed(message.getChannel(), almanax.getMoreEmbedObject());
                }

            } catch (ParseException e) {
                new IncorrectDateFormatDiscordException().throwException(message, this);
                return false;
            } catch (IOException e) {
                ExceptionManager.manageIOException(e, message, this, new AlmanaxNotFoundDiscordException());
            } catch (Exception e) {
                ExceptionManager.manageException(e, message, this);
            }
        }
        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** donne le bonus et l'offrande d'une date particulière.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`" + name + "` : donne le bonus et l'offrande du jour actuel."
                + "\n" + prefixe + "`" + name + " `*`jj/mm/aaaa`* : donne le bonus et l'offrande du jour spécifié."
                + "\n" + prefixe + "`" + name + " `*`+days`* : donne la liste des bonus et offrandes des jours à venir (jusqu'à 9 jours)."
                + "\n" + prefixe + "`"  + name + " true` : poste l'almanax quotidiennement. Fonctionne aussi avec \"on\" et \"0\"."
                + "\n" + prefixe + "`"  + name + " false` : ne poste plus l'almanax dans le salon. Fonctionne aussi avec \"off\" et \"1\".\n";
    }
}

package commands;

import data.Almanax;
import discord.Message;
import exceptions.*;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import java.io.IOException;
import java.lang.Exception;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class AlmanaxCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(AlmanaxCommand.class);
    private final static DateFormat discordToBot = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private final static DateFormat botToAlmanax = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

    public AlmanaxCommand(){
        super("almanax", "(\\s+\\d{2}/\\d{2}/\\d{4}|\\s+\\+\\d)?");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            try {
                Date date = new Date();
                Matcher m = getMatcher(message);
                m.find();
                if (m.group(1) != null && m.group(1).matches("\\s+\\+\\d")) {
                    int number = Integer.parseInt(m.group(1).replaceAll("\\s+\\+", ""));
                    StringBuilder st = new StringBuilder();
                    for (int i = 0; i < number; i++) {
                        date = DateUtils.addDays(new Date(), i);
                        Almanax almanax = Almanax.get(botToAlmanax.format(date));
                        st.append(discordToBot.format(date)).append(" : ");
                        if (almanax != null) {
                            st.append("**Bonus** : ").append(almanax.getBonus())
                                    .append("\t**Offrande** : ").append(almanax.getOffrande())
                                    .append("\n");
                        } else
                            st.append("Impossible d'accéder à cette date.\n");
                    }

                    Message.sendText(message.getChannel(), st.toString());
                } else {
                    if (m.group(1) != null && m.group(1).matches("\\s+\\d{2}/\\d{2}/\\d{4}"))
                        date = discordToBot.parse(m.group(1));
                    Almanax almanax = Almanax.get(botToAlmanax.format(date));

                    if (almanax != null) {
                        Message.sendEmbed(message.getChannel(), almanax.getEmbedObject());
                        return false;
                    } else {
                        new AlmanaxNotFoundDiscordException().throwException(message, this);
                        return false;
                    }
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
                + "\n" + prefixe + "`" + name + " `*+days`* : donne la liste des bonus et offrandes des jours à venir.\n";
    }
}

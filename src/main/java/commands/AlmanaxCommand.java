package commands;

import data.Almanax;
import data.Constants;
import discord.Message;
import exceptions.*;
import org.jsoup.HttpStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Exception;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class AlmanaxCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(AlmanaxCommand.class);
    private final static DateFormat discordToBot = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private final static DateFormat botToAlmanax = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

    public AlmanaxCommand(){
        super(Pattern.compile("almanax"),
                Pattern.compile("^(" + Constants.prefixCommand + "almanax)(\\s+\\d{2}/\\d{2}/\\d{4})?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            Date date = new Date();

            if (m.group(2) != null) {
                try {
                    date = discordToBot.parse(m.group(2));
                } catch (ParseException e) {
                    new IncorrectDateFormatDiscordException().throwException(message, this);
                    return false;
                }
            }

            try {
                Almanax almanax = Almanax.get(botToAlmanax.format(date));

                if (almanax != null) {
                    StringBuilder st = new StringBuilder("**Almanax du ")
                            .append(discordToBot.format(date)).append(" :**\n")
                            .append(almanax.getBonus()).append("\n")
                            .append(almanax.getOffrande()).append("\n");

                    Message.sendText(message.getChannel(), st.toString());
                    return false;
                } else {
                    new AlmanaxNotFoundDiscordException().throwException(message, this);
                    return false;
                }
            } catch (FileNotFoundException | HttpStatusException e){
                new CharacterPageNotFoundDiscordException().throwException(message, this);
            } catch(IOException e){
                // First we try parsing the exception message to see if it contains the response code
                Matcher exMsgStatusCodeMatcher = Pattern.compile("^Server returned HTTP response code: (\\d+)")
                        .matcher(e.getMessage());
                if(exMsgStatusCodeMatcher.find()) {
                    int statusCode = Integer.parseInt(exMsgStatusCodeMatcher.group(1));
                    if (statusCode >= 500 && statusCode < 600) {
                        LOG.warn(e.getMessage());
                        new DofusWebsiteInaccessibleDiscordException().throwException(message, this);
                    }
                    else {
                        Reporter.report(e);
                        LOG.error(e.getMessage());
                    }
                } else {
                    Reporter.report(e);
                    LOG.error(e.getMessage());
                }
            }  catch (Exception e) {
                LOG.error(e.getMessage());
                Reporter.report(e);
                new UnknownErrorDiscordException().throwException(message, this);
            }
        }
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "almanax** donne le bonus et l'offrande d'une date particulière.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "almanax` : donne le bonus et l'offrande du jour actuel."
                + "\n`" + Constants.prefixCommand + "almanax `*`jj/mm/aaaa`* : donne le bonus et l'offrande du jour spécifié.\n";
    }
}

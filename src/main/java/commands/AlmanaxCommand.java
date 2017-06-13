package commands;

import data.Almanax;
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

/**
 * Created by steve on 14/07/2016.
 */
public class AlmanaxCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(AlmanaxCommand.class);
    private final static DateFormat discordToBot = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private final static DateFormat botToAlmanax = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

    public AlmanaxCommand(){
        super("almanax", "(\\s+\\d{2}/\\d{2}/\\d{4})?");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            Date date = new Date();
            Matcher m = getMatcher(message);
            m.find();
            if (m.group(1) != null) {
                try {
                    date = discordToBot.parse(m.group(1));
                } catch (ParseException e) {
                    new IncorrectDateFormatDiscordException().throwException(message, this);
                    return false;
                }
            }

            try {
                Almanax almanax = Almanax.get(botToAlmanax.format(date));

                if (almanax != null) {
                    Message.sendEmbed(message.getChannel(), almanax.getEmbedObject());
                    return false;
                } else {
                    new AlmanaxNotFoundDiscordException().throwException(message, this);
                    return false;
                }
            } catch (FileNotFoundException | HttpStatusException e){
                new DofusWebsiteInaccessibleDiscordException().throwException(message, this);
            } catch(IOException e){
                ExceptionManager.manageIOException(e, message, this);
            }  catch (Exception e) {
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
                + "\n" + prefixe + "`"  + name + "` : donne le bonus et l'offrande du jour actuel."
                + "\n" + prefixe + "`"  + name + " `*`jj/mm/aaaa`* : donne le bonus et l'offrande du jour spécifié.\n";
    }
}

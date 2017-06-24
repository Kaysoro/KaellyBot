package exceptions;

import commands.Command;
import org.jsoup.HttpStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.io.FileNotFoundException;
import java.lang.*;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 15/05/2017.
 */
public abstract class ExceptionManager {

    private static Logger LOG = LoggerFactory.getLogger(ExceptionManager.class);


    public static void manageIOException(Exception e, IMessage message, Command command, DiscordException notFound){
        // First we try parsing the exception message to see if it contains the response code
        Matcher exMsgStatusCodeMatcher = Pattern.compile("^Server returned HTTP response code: (\\d+)")
                .matcher(e.getMessage());
        if(exMsgStatusCodeMatcher.find()) {
            int statusCode = Integer.parseInt(exMsgStatusCodeMatcher.group(1));
            if (statusCode >= 500 && statusCode < 600) {
                LOG.warn(e.getMessage());
                new DofusWebsiteInaccessibleDiscordException().throwException(message, command);
            }
            else {
                Reporter.report(e);
                LOG.error(e.getMessage());
                new UnknownErrorDiscordException().throwException(message, command);
            }
        } else if (e instanceof UnknownHostException) {
            new DofusWebsiteInaccessibleDiscordException().throwException(message, command);
        } else if (e instanceof FileNotFoundException || e instanceof HttpStatusException){
            notFound.throwException(message, command);
        }
        else {
            Reporter.report(e);
            LOG.error(e.getMessage(), message.getContent());
            new UnknownErrorDiscordException().throwException(message, command);
        }
    }

    public static void manageException(Exception e, IMessage message, Command command){
        LOG.error(e.getMessage());
        Reporter.report(e, message.getContent());
        new UnknownErrorDiscordException().throwException(message, command);
    }
}

package exceptions;

import commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.lang.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 15/05/2017.
 */
public abstract class ExceptionManager {

    private static Logger LOG = LoggerFactory.getLogger(ExceptionManager.class);

    public static void manageIOException(java.lang.Exception e, IMessage message, Command command){
        // First we try parsing the exception message to see if it contains the response code
        Matcher exMsgStatusCodeMatcher = Pattern.compile("^Server returned HTTP response code: (\\d+)")
                .matcher(e.getMessage());
        if(exMsgStatusCodeMatcher.find()) {
            int statusCode = Integer.parseInt(exMsgStatusCodeMatcher.group(1));
            if (statusCode >= 500 && statusCode < 600) {
                LOG.warn(e.getMessage());
                new DofusWebsiteInaccessibleException().throwException(message, command);
            }
            else {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        } else {
            Reporter.report(e);
            LOG.error(e.getMessage());
        }
    }

    public static void manageException(java.lang.Exception e, IMessage message, Command command){
        LOG.error(e.getMessage());
        Reporter.report(e);
        new UnknownErrorException().throwException(message, command);
    }
}

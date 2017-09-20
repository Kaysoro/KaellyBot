package exceptions;

import commands.Command;
import data.ClientConfig;
import org.jsoup.HttpStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.io.FileNotFoundException;
import java.lang.*;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
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
                LOG.warn("manageIOException", e);
                new DofusWebsiteInaccessibleDiscordException().throwException(message, command);
            }
            else {
                ClientConfig.setSentryContext(message.getGuild(),
                        message.getAuthor(), message.getChannel(), message);
                LOG.error("manageIOException", e);
                new UnknownErrorDiscordException().throwException(message, command);
            }
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            new DofusWebsiteInaccessibleDiscordException().throwException(message, command);
        } else if (e instanceof FileNotFoundException
                || e instanceof HttpStatusException
                || e instanceof NoRouteToHostException){
            notFound.throwException(message, command);
        }
        else {
            ClientConfig.setSentryContext(message.getGuild(),
                    message.getAuthor(), message.getChannel(), message);
            LOG.error("manageIOException", e);
            new UnknownErrorDiscordException().throwException(message, command);
        }
    }

    public static void manageSilentlyIOException(Exception e){
        ClientConfig.setSentryContext(null, null, null, null);
        // First we try parsing the exception message to see if it contains the response code
        Matcher exMsgStatusCodeMatcher = Pattern.compile("^Server returned HTTP response code: (\\d+)")
                .matcher(e.getMessage());
        if(exMsgStatusCodeMatcher.find()) {
            int statusCode = Integer.parseInt(exMsgStatusCodeMatcher.group(1));
            if (statusCode >= 500 && statusCode < 600)
                LOG.warn("manageSilentlyIOException", e);
            else
                LOG.error("manageSilentlyIOException", e);
        } else if (e instanceof UnknownHostException
                || e instanceof SocketTimeoutException
                || e instanceof FileNotFoundException
                || e instanceof HttpStatusException
                || e instanceof NoRouteToHostException)
            LOG.warn("manageSilentlyIOException", e);
        else
            LOG.error("manageSilentlyIOException", e);

    }

    public static void manageException(Exception e, IMessage message, Command command){
        ClientConfig.setSentryContext(message.getGuild(),
                message.getAuthor(), message.getChannel(), message);
        LOG.error("manageException", e);
        new UnknownErrorDiscordException().throwException(message, command);
    }

    public static void manageSilentlyException(Exception e){
        ClientConfig.setSentryContext(null, null, null, null);
        LOG.error("manageSilentlyException", e);
    }
}
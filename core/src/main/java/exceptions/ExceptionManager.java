package exceptions;

import commands.model.Command;
import enums.Language;
import org.jsoup.HttpStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Reporter;

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

    private static DiscordException gameWebsite503 = new AdvancedDiscordException("exception.advanced.game_website_503",
            new String[]{"game.url"}, new Boolean[]{true});

    public static void manageIOException(Exception e, IMessage message, Command command, Language lg, DiscordException notFound){
        // First we try parsing the exception message to see if it contains the response code
        Matcher exMsgStatusCodeMatcher = Pattern.compile("^Server returned HTTP response code: (\\d+)")
                .matcher(e.getMessage());
        if(exMsgStatusCodeMatcher.find()) {
            int statusCode = Integer.parseInt(exMsgStatusCodeMatcher.group(1));
            if (statusCode >= 500 && statusCode < 600) {
                LOG.warn("manageIOException", e);
                gameWebsite503.throwException(message, command, lg);
            }
            else {
                Reporter.report(e, message.getGuild(), message.getChannel(), message.getAuthor(), message);
                LOG.error("manageIOException", e);
                BasicDiscordException.UNKNOWN_ERROR.throwException(message, command, lg);
            }
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            gameWebsite503.throwException(message, command, lg);
        } else if (e instanceof FileNotFoundException
                || e instanceof HttpStatusException
                || e instanceof NoRouteToHostException){
            notFound.throwException(message, command, lg);
        }
        else {
            Reporter.report(e, message.getGuild(), message.getChannel(), message.getAuthor(), message);
            LOG.error("manageIOException", e);
            BasicDiscordException.UNKNOWN_ERROR.throwException(message, command, lg);
        }
    }

    public static void manageSilentlyIOException(Exception e){
        Reporter.report(e);
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

    public static void manageException(Exception e, IMessage message, Command command, Language lg){
        Reporter.report(e, message.getGuild(), message.getChannel(), message.getAuthor(), message);
        LOG.error("manageException", e);
        BasicDiscordException.UNKNOWN_ERROR.throwException(message, command, lg);
    }

    public static void manageSilentlyException(Exception e){
        Reporter.report(e);
        LOG.error("manageSilentlyException", e);
    }
}
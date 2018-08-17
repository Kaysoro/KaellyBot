package listeners;

import commands.*;
import commands.model.Command;
import enums.Language;
import exceptions.BasicDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import util.Reporter;
import util.Translator;

/**
 * Created by steve on 14/07/2016.
 */
public class MessageListener {

    private final static Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    @EventSubscriber
    public void onReady(MessageReceivedEvent event) {
        Language lg = Translator.getLanguageFrom(event.getChannel());

        // If the authorId is a bot, message get ignored
        if (! event.getMessage().getAuthor().isBot())
            for(Command command : CommandManager.getCommands())
                try {
                    command.request(event.getMessage());
                } catch (Exception e){
                    BasicDiscordException.UNKNOWN_ERROR.throwException(event.getMessage(), command, lg);
                    Reporter.report(e, event.getGuild(), event.getChannel(), event.getAuthor(), event.getMessage());
                    LOG.error("onReady", e);
                }
    }
}

package listeners;

import commands.*;
import enums.Language;
import exceptions.BasicDiscordException;
import exceptions.DiscordException;
import util.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import util.Translator;

/**
 * Created by steve on 14/07/2016.
 */
public class MessageListener {

    private final static Logger LOG = LoggerFactory.getLogger(MessageListener.class);
    private DiscordException unknown = new BasicDiscordException("exception.basic.unknown_error");

    public MessageListener(){
        super();
    }
        @EventSubscriber
        public void onReady(MessageReceivedEvent event) {
            ClientConfig.setSentryContext(event.getGuild(), event.getAuthor(), event.getChannel(), event.getMessage());
            Language lg = Translator.getLanguageFrom(event.getChannel());

            // If the authorId is a bot, message get ignored
            if (! event.getMessage().getAuthor().isBot())
                for(Command command : CommandManager.getCommands())
                    try {
                        command.request(event.getMessage());
                    } catch (Exception e){
                        unknown.throwException(event.getMessage(), command, lg);
                        LOG.error("MessageListener.onReady", e);
                    }
        }
}

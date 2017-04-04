package controler;

import commands.*;
import data.Guild;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class MessageListener {

    private final static Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    public MessageListener(){
        super();
    }
        @EventSubscriber
        public void onReady(MessageReceivedEvent event) {

            // If the author is a bot, message get ignored
            if (! event.getMessage().getAuthor().isBot())
                for(Command command : Command.commands)
                    command.request(event.getMessage());
            }
}

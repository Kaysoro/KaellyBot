package controler;

import commands.*;
import data.Guild;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

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
            // If the author doesn't exist in our database, we added it with the lowest rights
            String authorId = event.getMessage().getAuthor().getID();
            String authorName = event.getMessage().getAuthor().getDisplayName(event.getMessage().getGuild());
            String guildId = event.getMessage().getGuild().getID();
            String guildName = event.getMessage().getGuild().getName();

            if (! Guild.getGuilds().get(guildId).getName().equals(guildName))
                // GuildName from database is deprecated : it have to be updated.
                Guild.getGuilds().get(guildId).setName(guildName);

            if (! User.getUsers().get(guildId).get(authorId).getName().equals(authorName))
                // AuthorName from database is deprecated : it have to be updated.
                User.getUsers().get(guildId).get(authorId).setName(authorName);


            for(Command command : Command.commands)
                command.request(event.getMessage());
        }
}

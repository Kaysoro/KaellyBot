package controler;

import commands.*;
import data.Guild;
import data.User;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

/**
 * Created by steve on 14/07/2016.
 */
public class MessageListener {

    public MessageListener(){
        super();
    }
        @EventSubscriber
        public void onReady(MessageReceivedEvent event) {
            // If the author doesn't exist in our database, we added it with the lowest rights
            String authorId = event.getMessage().getAuthor().getID();
            String guildId = event.getMessage().getGuild().getID();
            if (! Guild.getGuild().containsKey(guildId))
                new Guild(guildId).addToDatabase();
            if (! User.getUsers().get(guildId).containsKey(authorId))
                new User(authorId, User.RIGHT_INVITE, Guild.getGuild().get(guildId)).addToDatabase();

            for(Command command : Command.commands)
                command.request(event.getMessage());
        }
}

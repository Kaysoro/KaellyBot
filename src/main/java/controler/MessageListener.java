package controler;

import commands.*;
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

            if (! User.getUsers(guildId).containsKey(authorId)){
                User user = new User(authorId, User.RIGHT_INVITE, guildId);
                user.addToDatabase();
            }

            for(Command command : Command.commands)
                command.request(event.getMessage());
        }
}

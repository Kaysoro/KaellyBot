package controler;

import commands.*;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 14/07/2016.
 */
public class MessageListener {

    private List<Command> commands;

    public MessageListener(){
        super();
        commands = new ArrayList<Command>();
        commands.add(new ItemCommand());
        commands.add(new ParrotCommand());
        commands.add(new MapCommand());
    }
        @EventSubscriber
        public void onReady(MessageReceivedEvent event) {
            for(Command command : commands)
                command.request(event.getMessage());
        }
}

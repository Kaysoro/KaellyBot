package controler;

import commands.Command;
import commands.ItemCommand;
import commands.ParrotCommand;
import data.ClientConfig;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

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
    }
        @EventSubscriber
        public void onReady(MessageReceivedEvent event) {
            for(Command command : commands)
                command.request(event.getMessage());
        }
}

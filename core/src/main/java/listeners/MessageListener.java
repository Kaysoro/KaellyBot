package listeners;

import commands.*;
import commands.model.AbstractCommand;
import commands.model.Command;
import enums.Language;
import exceptions.BasicDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import util.Reporter;
import util.Translator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 14/07/2016.
 */
public class MessageListener {

    private final static Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    @EventSubscriber
    public void onReady(MessageReceivedEvent event) {
        Language lg = Translator.getLanguageFrom(event.getChannel());
        String prefixe = AbstractCommand.getPrefix(event.getMessage());

        // If the authorId is a bot, message get ignored
        if (! event.getMessage().getAuthor().isBot()) {
            List<Command> commandsAvailable = new ArrayList<>();

            for (Command command : CommandManager.getCommands())
                if (event.getMessage().getContent().startsWith(prefixe + command.getName()))
                    commandsAvailable.add(command);

            if (!commandsAvailable.isEmpty()){
                commandsAvailable.sort((cmd1, cmd2) -> cmd2.getName().length() - cmd1.getName().length());
                Command command = commandsAvailable.get(0);

                try {
                    command.request(event.getMessage());
                } catch (Exception e) {
                    BasicDiscordException.UNKNOWN_ERROR.throwException(event.getMessage(), command, lg);
                    Reporter.report(e, event.getGuild(), event.getChannel(), event.getAuthor(), event.getMessage());
                    LOG.error("onReady", e);
                }

            }
        }
    }
}

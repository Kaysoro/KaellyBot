package listeners;

import commands.*;
import commands.model.AbstractCommand;
import commands.model.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import enums.Language;
import exceptions.BasicDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reporter;
import util.Translator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 14/07/2016.
 */
public class MessageListener {

    private final static Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    public void onReady(MessageCreateEvent event) {
        event.getMessage().getChannel()
                .doOnSuccess(channel -> {
                    Language lg = Translator.getLanguageFrom(channel);
                    String prefixe = AbstractCommand.getPrefix(event.getMessage());

                    // If the authorId is a bot, message get ignored
                    if (! event.getMessage().getAuthor().map(User::isBot).orElse(true)) {
                        List<Command> commandsAvailable = new ArrayList<>();

                        for (Command command : CommandManager.getCommands())
                            if (event.getMessage().getContent().map(content -> content
                                    .startsWith(prefixe + command.getName())).orElse(false))
                                commandsAvailable.add(command);

                        if (!commandsAvailable.isEmpty()){
                            commandsAvailable.sort((cmd1, cmd2) -> cmd2.getName().length() - cmd1.getName().length());
                            Command command = commandsAvailable.get(0);

                            try {
                                command.request(event.getMessage());
                            } catch (Exception e) {
                                BasicDiscordException.UNKNOWN_ERROR.throwException(event.getMessage(), command, lg);
                                Reporter.report(e, null, channel, event.getMessage().getAuthor()
                                        .orElse(null), event.getMessage());
                                LOG.error("onReady", e);
                            }
                        }
                    }
                }).subscribe();
    }
}

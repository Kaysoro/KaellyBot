package listeners;

import commands.CommandManager;
import commands.model.AbstractLegacyCommand;
import commands.model.LegacyCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import enums.Language;
import exceptions.BasicDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import util.Translator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 14/07/2016.
 */
public class MessageListener {

    private final static Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    public Mono<MessageChannel> onReady(MessageCreateEvent event) {
         return event.getMessage().getChannel()
                .doOnSuccess(channel -> {
                    Language lg = Translator.getLanguageFrom(channel);
                    String prefixe = AbstractLegacyCommand.getPrefix(event.getMessage());

                    // If the authorId is a bot, message get ignored
                    if (! event.getMessage().getAuthor().map(User::isBot).orElse(true)) {
                        List<LegacyCommand> commandsAvailable = new ArrayList<>();

                        for (LegacyCommand command : CommandManager.getCommands())
                            if (event.getMessage().getContent().startsWith(prefixe + command.getName()))
                                commandsAvailable.add(command);

                        if (!commandsAvailable.isEmpty()){
                            commandsAvailable.sort((cmd1, cmd2) -> cmd2.getName().length() - cmd1.getName().length());
                            LegacyCommand command = commandsAvailable.get(0);

                            try {
                                command.request(event, event.getMessage());
                            } catch (Exception e) {
                                BasicDiscordException.UNKNOWN_ERROR.throwException(event.getMessage(), command, lg);
                                Reporter.report(e, null, channel, event.getMessage().getAuthor()
                                        .orElse(null), event.getMessage());
                                LOG.error("onReady", e);
                            }
                        }
                    }
                });
    }
}

package listeners;

import commands.CommandManager;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SlashCommandListener {

    public Mono<Void> onReady(ChatInputInteractionEvent event) {
        return Flux.fromIterable(CommandManager.getSlashCommands())
                .filter(command -> command.getName().equals(event.getCommandName()))
                .next()
                .flatMap(command -> command.handle(event));
    }
}

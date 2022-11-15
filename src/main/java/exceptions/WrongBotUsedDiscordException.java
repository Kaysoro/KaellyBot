package exceptions;

import commands.model.LegacyCommand;
import data.Constants;
import data.ServerDofus;
import discord4j.core.object.entity.Message;
import enums.Language;
import util.Translator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by steve on 31/05/2019.
 */
public class WrongBotUsedDiscordException implements DiscordException {

    @Override
    public void throwException(Message message, LegacyCommand command, Language lg, Object... arguments) {
        StringBuilder st = new StringBuilder();
        List<ServerDofus> servers = (List<ServerDofus>) arguments[0];
        servers.stream()
                .collect(Collectors.groupingBy(ServerDofus::getGame))
            .forEach((game, list) -> st.append(Translator.getLabel(lg, "exception.wrong_bot_used.loop")
                .replaceAll("\\{game}", game.getName())
                .replaceAll("\\{url}", game.getBotInvite())
                .replaceAll("\\{servers}", list.stream().map(server -> server.getLabel(lg))
                        .reduce((server1, server2) -> server1 + ", " + server2)
                        .orElse("")))
                    .append("\n"));
        message.getChannel().flatMap(channel ->
                channel.createEmbed(spec ->
                        spec.setTitle(Translator.getLabel(lg, "exception.wrong_bot_used"))
                                .setDescription(st.toString())
                                .setImage(Constants.officialLogo)))
                .subscribe();

    }
}
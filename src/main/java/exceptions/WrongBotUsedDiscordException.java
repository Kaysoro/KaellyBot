package exceptions;

import commands.model.Command;
import data.ServerDofus;
import enums.Language;
import org.apache.commons.lang3.tuple.Pair;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;
import util.Translator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by steve on 31/05/2019.
 */
public class WrongBotUsedDiscordException implements DiscordException {

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        StringBuilder st = new StringBuilder(Translator.getLabel(lg, "exception.wrong_bot_used")).append("\n");
        List<ServerDofus> servers = (List<ServerDofus>) arguments[0];
        servers.stream()
                .collect(Collectors.groupingBy(ServerDofus::getGame))
                .forEach((game, list) -> st.append(Translator.getLabel(lg, "exception.wrong_bot_used.loop")
                        .replaceAll("\\{game\\}", game.getName())
                        .replaceAll("\\{url\\}", game.getBotInvite())
                        .replaceAll("\\{servers\\}", list.stream().map(ServerDofus::getName)
                                .reduce((server1, server2) -> server1 + ", " + server2)
                                .orElse("")))
                        .append("\n"));
        Message.sendText(message.getChannel(), st.toString());
    }
}
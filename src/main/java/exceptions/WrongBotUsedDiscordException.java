package exceptions;

import commands.model.Command;
import data.Constants;
import data.ServerDofus;
import enums.Language;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import util.ClientConfig;
import util.Message;
import util.Translator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by steve on 31/05/2019.
 */
public class WrongBotUsedDiscordException implements DiscordException {

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        StringBuilder st = new StringBuilder();
        List<ServerDofus> servers = (List<ServerDofus>) arguments[0];
        servers.stream()
                .collect(Collectors.groupingBy(ServerDofus::getGame))
            .forEach((game, list) -> st.append(Translator.getLabel(lg, "exception.wrong_bot_used.loop")
                .replaceAll("\\{game}", game.getName())
                .replaceAll("\\{url}", game.getBotInvite())
                .replaceAll("\\{servers}", list.stream().map(ServerDofus::getName)
                        .reduce((server1, server2) -> server1 + ", " + server2)
                        .orElse("")))
                    .append("\n"));
        Message.sendEmbed(message.getChannel(), new EmbedBuilder()
                .withTitle(Translator.getLabel(lg, "exception.wrong_bot_used"))
                .withDescription(st.toString())
                .withThumbnail(ClientConfig.DISCORD().getApplicationIconURL())
                .withColor(16711680)
                .withImage(Constants.officialLogo).build());
    }
}
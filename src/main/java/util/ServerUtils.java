package util;

import data.ChannelServer;
import data.Guild;
import data.ServerDofus;
import discord4j.core.object.entity.channel.MessageChannel;
import enums.Game;
import enums.Language;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import exceptions.TooMuchDiscordException;
import exceptions.WrongBotUsedDiscordException;
import org.jetbrains.annotations.NotNull;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ServerUtils {

    private static final DiscordException TOO_MANY_SERVERS = new TooMuchDiscordException("server");
    private static final DiscordException NOT_FOUND_SERVER = new NotFoundDiscordException("server");
    private static final DiscordException WRONG_BOT_USED = new WrongBotUsedDiscordException();


    public static ServerDofus getDofusServerFrom(Guild guild, MessageChannel channel){
        return Optional.ofNullable(ChannelServer.getChannelServers().get(channel.getId().asLong()))
                .map(ChannelServer::getServer)
                .orElse(guild.getServerDofus());
    }

    @NotNull
    public static ServerQuery getServerDofusFromName(String proposedServer, Language language){
        ServerQuery result = new ServerQuery();
        final String PROPOSED_SERVER = Normalizer.normalize(proposedServer, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("\\W+", "").toLowerCase().trim();

        List<ServerDofus> allServers = ServerDofus.getServersDofus().stream()
                .filter(server -> Normalizer.normalize(server.getName(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("\\W+", "").toLowerCase().trim()
                        .startsWith(PROPOSED_SERVER))
                .collect(Collectors.toList());

        List<ServerDofus> serverDofusList = allServers.stream()
            .filter(server -> server.getGame() == Game.DOFUS)
            .collect(Collectors.toList());

        // Check if the server name is exactly the good one
        List<ServerDofus> exactMatchDofusServer = serverDofusList.stream()
                .filter(server -> Normalizer.normalize(server.getName(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("\\W+", "").toLowerCase().trim()
                .equals(PROPOSED_SERVER))
                .collect(Collectors.toList());

        if (exactMatchDofusServer.size() == 1)
            serverDofusList = exactMatchDofusServer;

        if (!serverDofusList.isEmpty()) {
            result.serversFound = serverDofusList;
            if (serverDofusList.size() > 1)
                result.exceptions.add(TOO_MANY_SERVERS);
            else
                result.server = serverDofusList.get(0);
        }
        else if (!allServers.isEmpty()) {
            result.serversFound = allServers;
            result.exceptions.add(WRONG_BOT_USED);
        }
        else
            result.exceptions.add(NOT_FOUND_SERVER);
        return result;
    }

    public static final class ServerQuery {
        private ServerDofus server;
        private List<ServerDofus> serversFound;
        private List<DiscordException> exceptions;

        private ServerQuery(){
            serversFound = new ArrayList<>();
            exceptions = new ArrayList<>();
        }

        public ServerDofus getServer(){
            return server;
        }

        public boolean hasSucceed(){
            return exceptions.isEmpty();
        }

        public List<ServerDofus> getServersFound(){
            return serversFound;
        }

        public List<DiscordException> getExceptions(){
            return exceptions;
        }
    }
}
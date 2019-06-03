package finders;

import data.Guild;
import data.Position;
import data.ServerDofus;
import enums.Language;
import exceptions.ExceptionManager;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import util.ClientConfig;
import util.Message;
import util.Translator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PortalFinder
{
    private final static long DELTA = 20; // 20 minutes
    private static boolean isStarted = false;

    public static void start(){
        if (!isStarted) {
            isStarted = true;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    Map<ServerDofus, List<Position>> positionsUpdated = new HashMap<>();

                    for (ServerDofus server : ServerDofus.getServersDofus())
                        // Si les positions ne sont plus d'actualités, on les met à jour
                        if (System.currentTimeMillis() - server.getLastSweetRefresh() > DELTA
                                && ! server.getSweetId().equals(Position.NOT_PRESENT))
                            positionsUpdated.put(server, server.mergeSweetPositions(Position.getSweetPositions(server)));

                    for(PortalTracker tracker : PortalTracker.getPortalTrackers()) {
                        IGuild guild = ClientConfig.DISCORD().getGuildByID(Long.parseLong(tracker.getGuildId()));
                        IChannel chan = ClientConfig.DISCORD().getChannelByID(Long.parseLong(tracker.getChan()));
                        if (guild!= null && ! guild.isDeleted() && chan != null && ! chan.isDeleted()) {
                            Language lg = Translator.getLanguageFrom(chan);
                            for (Position position : positionsUpdated.get(Guild.getGuild(guild).getServerDofus()))
                                Message.sendEmbed(chan, position.getEmbedObjectChange(lg));
                        }
                    }
                } catch (IOException e) {
                    ExceptionManager.manageSilentlyIOException(e);
                } catch (Exception e) {
                    ExceptionManager.manageSilentlyException(e);
                }
            }, 0, 20, TimeUnit.MINUTES);
        }
    }
}
package finders;

import data.Guild;
import data.Portal;
import data.ServerDofus;
import enums.Language;
import exceptions.ExceptionManager;
import sx.blah.discord.handle.obj.IChannel;
import util.ClientConfig;
import util.Message;
import util.Translator;

import java.io.IOException;
import java.util.List;
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
                    for (Guild guild : Guild.getGuilds().values())
                        if (guild.getServerDofus() != null) { //Server renseigné ?
                            ServerDofus server = guild.getServerDofus();

                            // Si les positions ne sont plus d'actualités, on les met à jour
                            if (System.currentTimeMillis() - server.getLastSweetRefresh() > DELTA
                                    && ! server.getSweetId().equals(Portal.NOT_PRESENT))
                                server.setSweetPortals(Portal.getSweetPortals(server));

                            List<Portal> newPortals = guild.mergePortals(server.getSweetPortals());
                            for(PortalTracker tracker : guild.getPortalTrackers().values()) {
                                IChannel chan = ClientConfig.DISCORD().getChannelByID(Long.parseLong(tracker.getChan()));
                                if (chan != null && ! chan.isDeleted()) {
                                    Language lg = Translator.getLanguageFrom(chan);
                                    for (Portal portal : newPortals)
                                        Message.sendEmbed(chan, portal.getEmbedObjectChange(lg));
                                }
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

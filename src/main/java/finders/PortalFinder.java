package finders;

import data.Guild;
import data.Portal;
import data.ServerDofus;
import exceptions.ExceptionManager;

import java.io.IOException;
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

                            guild.mergePortals(server.getSweetPortals());
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

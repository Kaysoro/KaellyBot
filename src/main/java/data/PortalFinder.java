package data;

import exceptions.ExceptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class PortalFinder
{
    private final static Logger LOG = LoggerFactory.getLogger(RSSFinder.class);
    private final static long DELTA = 3600000; // 1 hour
    private static boolean isStarted = false;

    public static void start(){
        if (!isStarted) {
            isStarted = true;
            new Thread(() -> {
                while(true) {
                    try {
                        for (Guild guild : Guild.getGuilds().values())
                            if (guild.getServerDofus() != null) { //Server renseigné ?
                                ServerDofus server = guild.getServerDofus();

                                // Si les positions ne sont plus d'actualités, on les met à jour
                                if (System.currentTimeMillis() - server.getLastSweetRefresh() > DELTA)
                                    server.setSweetPortals(Portal.getSweetPortals(server));

                                guild.mergePortals(server.getSweetPortals());
                            }
                    } catch (IOException e) {
                        ExceptionManager.manageSilentlyIOException(e);
                    } catch (Exception e) {
                        ExceptionManager.manageSilentlyException(e);
                    }

                    try {
                        sleep(DELTA);
                    } catch (InterruptedException e) {
                        ClientConfig.setSentryContext(null, null, null, null);
                        LOG.error(e.getMessage());
                    }
                }
            }).start();
        }
    }
}

package data;

import exceptions.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                    for (Guild guild : Guild.getGuilds().values())
                        if (guild.getServerDofus() != null){ //Server renseigné ?
                            //TODO
                        }

                    try {
                        sleep(DELTA);
                    } catch (InterruptedException e) {
                        Reporter.report(e);
                        LOG.error(e.getMessage());
                    }
                }
            }).start();
        }
    }
}

package finders;

import data.RSS;
import discord4j.common.util.Snowflake;
import discord4j.rest.entity.RestChannel;
import discord4j.rest.http.client.ClientException;
import enums.Language;
import external.RSSAPI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientConfig;
import util.Connexion;
import util.Reporter;
import util.Translator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by steve on 12/01/2017.
 */
@Getter
@AllArgsConstructor
public class RSSFinder {
    private final static Logger LOG = LoggerFactory.getLogger(RSSFinder.class);
    private final static long DELTA = 10; // 10min
    private static boolean isStarted = false;

    private static Map<String, RSSFinder> rssFinders = null;
    private final String guildId;
    private final String chan;
    private long lastUpdate;

    public RSSFinder(String idGuild, String chan) {
        this(idGuild, chan, System.currentTimeMillis());
    }

    public synchronized void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE RSS_Finder SET last_update = ? WHERE id_chan = ?;");
            preparedStatement.setLong(1, lastUpdate);
            preparedStatement.setString(2, getChan());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("setLastRSS", e);
        }
    }

    public synchronized void addToDatabase(){
        if (! getRSSFinders().containsKey(getChan())) {
            getRSSFinders().put(getChan(), this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO RSS_Finder(id_guild, id_chan, last_update) VALUES(?, ?, ?);");
                preparedStatement.setString(1, getGuildId());
                preparedStatement.setString(2, getChan());
                preparedStatement.setLong(3, getLastUpdate());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("addToDatabase", e);
            }
        }
    }

    public synchronized void removeToDatabase() {
        getRSSFinders().remove(getChan());

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM RSS_Finder WHERE id_chan = ?;");
            request.setString(1, getChan());
            request.executeUpdate();

        } catch (SQLException e) {
            LOG.error("removeToDatabase", e);
        }
    }

    public synchronized static Map<String, RSSFinder> getRSSFinders(){
        if (rssFinders == null){
            rssFinders = new ConcurrentHashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id_guild, id_chan, last_update FROM RSS_Finder");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    String idChan = resultSet.getString("id_chan");
                    String idGuild = resultSet.getString("id_guild");
                    long lastUpdate = resultSet.getLong("last_update");
                    rssFinders.put(idChan, new RSSFinder(idGuild, idChan, lastUpdate));
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error("getRSSFinders", e);
            }
        }

        return rssFinders;
    }

    public static void start(){
        if (!isStarted) {
            LOG.info("Scheduling RSS Feeds polling from Dofus website...");
            isStarted = true;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                LOG.info("Dispatching the most recent RSS feeds from Dofus website...");
                Map<Language, List<RSS>> allFeeds = new HashMap<>();
                for(Language lg : Language.values())
                    allFeeds.put(lg, RSSAPI.getRSSFeeds(lg));

                for (RSSFinder finder : getRSSFinders().values())
                    try {
                        RestChannel chan = ClientConfig.DISCORD().getChannelById(Snowflake.of(finder.chan));
                        Language lg = Translator.getLanguageFrom(chan);
                        List<RSS> rssFeeds = allFeeds.get(Translator.getLanguageFrom(chan));
                        long lastRSS = -1;

                        for (RSS rss : rssFeeds)
                            if (rss.getDate() > finder.getLastUpdate()) {
                                chan.createMessage(rss.decorateRestEmbedObject())
                                        .doOnError(error -> {
                                            if (error instanceof ClientException)
                                                LOG.warn("RSSFinder: no access on " + finder.getChan());
                                            else LOG.error("RSSFinder", error);
                                        })
                                        .subscribe();
                                lastRSS = rss.getDate();
                            }

                        if (lastRSS != -1)
                            finder.setLastUpdate(lastRSS);
                    } catch(ClientException e){
                        LOG.warn("RSSFinder: no access on " + finder.getChan());
                    } catch(Exception e){
                        Reporter.report(e);
                        LOG.error("RSSFinder", e);
                    }
            }, 0, DELTA, TimeUnit.MINUTES);
        }
    }
}

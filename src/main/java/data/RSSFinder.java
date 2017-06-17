package data;

import discord.Message;
import exceptions.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * Created by steve on 12/01/2017.
 */
public class RSSFinder {
    private final static Logger LOG = LoggerFactory.getLogger(RSSFinder.class);
    private final static long DELTA = 600000; // 10min
    private static boolean isStarted = false;

    private static Map<Long, RSSFinder> rssFinders = null;
    private long idGuild;
    private long chan;
    private long lastRSS;

    public RSSFinder(long idGuild, long chan) {
        this(idGuild, chan, System.currentTimeMillis());
    }

    private RSSFinder(long idGuild, long chan, long lastRSS) {
        this.idGuild = idGuild;
        this.chan = chan;
        this.lastRSS = lastRSS;
    }

    public void setLastRSS(long lastRSS) {
        this.lastRSS = lastRSS;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE RSS_Finder SET last_update = ? WHERE id_chan = ?;");
            preparedStatement.setLong(1, lastRSS);
            preparedStatement.setString(2, String.valueOf(getChan()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error(e.getMessage());
        }
    }

    public void addToDatabase(){
        if (! getRSSFinders().containsKey(getChan())) {
            getRSSFinders().put(getChan(), this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO RSS_Finder(id_guild, id_chan, last_update) VALUES(?, ?, ?);");
                preparedStatement.setString(1, String.valueOf(getGuildId()));
                preparedStatement.setString(2, String.valueOf(getChan()));
                preparedStatement.setLong(3, getLastRSS());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }
    }

    public void removeToDatabase() {
        getRSSFinders().remove(this);

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM RSS_Finder WHERE id_chan = ? AND id_guild = ?;");
            request.setString(1, String.valueOf(getChan()));
            request.setString(2, String.valueOf(getGuildId()));
            request.executeUpdate();

        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error(getChan() + " : " + e.getMessage());
        }
    }

    public static Map<Long, RSSFinder> getRSSFinders(){
        if (rssFinders == null){
            rssFinders = new HashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id_guild, id_chan, last_update FROM RSS_Finder");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    long idChan = Long.parseLong(resultSet.getString("id_chan"));
                    long idGuild = Long.parseLong(resultSet.getString("id_guild"));
                    long lastUpdate = resultSet.getLong("last_update");

                    IChannel chan = ClientConfig.DISCORD().getChannelByID(idChan);

                    if (chan != null && ! chan.isDeleted())
                        rssFinders.put(chan.getLongID(), new RSSFinder(chan.getLongID(), lastUpdate));
                    else {
                        new RSSFinder(idGuild, idChan).removeToDatabase();
                        LOG.info("Chan deleted : " + idChan);
                    }
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }

        return rssFinders;
    }

    public static void start(){
        if (!isStarted) {
            isStarted = true;
            new Thread(() -> {
                    while(true) {
                        List<RSS> rssFeeds = RSS.getRSSFeeds();
                        for (RSSFinder finder : getRSSFinders().values()) {
                            long lastRSS = -1;

                            for (RSS rss : rssFeeds)
                                if (rss.getDate() > finder.getLastRSS()) {
                                    Message.sendText(ClientConfig.DISCORD().getChannelByID(finder.getChan()), rss.toStringDiscord());
                                    lastRSS = rss.getDate();
                                }

                            if (lastRSS != -1)
                                finder.setLastRSS(lastRSS);
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

    public long getChan() {
        return chan;
    }

    public long getGuildId() {
        return idGuild;
    }

    public long getLastRSS() {
        return lastRSS;
    }
}

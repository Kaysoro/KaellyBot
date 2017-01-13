package data;

import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 12/01/2017.
 */
public class RSSFinder {
    private final static Logger LOG = LoggerFactory.getLogger(RSSFinder.class);
    private final static long DELTA = 3600000; // 1h
    private static boolean isStarted = false;

    private static List<RSSFinder> rssFinders = null;
    private IChannel chan;
    private long lastRSS;

    public RSSFinder(IChannel chan) {
        this(chan, System.currentTimeMillis());
    }

    private RSSFinder(IChannel chan, long lastRSS) {
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
            preparedStatement.setString(2, getChan().getID());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public void addToDatabase(){
        if (! getRSSFinders().contains(this)) {
            getRSSFinders().add(this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO RSS_Finder(id_chan, last_update) VALUES(?, ?);");
                preparedStatement.setString(1, getChan().getID());
                preparedStatement.setLong(2, getLastRSS());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    public void removeToDatabase() {
        getRSSFinders().remove(this);

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM RSS_Finder WHERE id_chan = ?;");
            request.setString(1, getChan().getID());
            request.executeUpdate();

        } catch (SQLException e) {
            LOG.error(getChan().getID() + " : " + e.getMessage());
        }
    }

    public static List<RSSFinder> getRSSFinders(){
        if (rssFinders == null){
            rssFinders = new ArrayList<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id_chan, last_update FROM RSS_Finder");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    IChannel chan = ClientConfig.CLIENT().getChannelByID(resultSet.getString("id_chan"));
                    long lastUpdate = resultSet.getLong("last_update");
                    rssFinders.add(new RSSFinder(chan, lastUpdate));
                }
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }

        return rssFinders;
    }

    public static void start(){
        if (!isStarted) {
            isStarted = true;
            new Thread() {
                public void run() {
                    List<RSS> rssFeeds = RSS.getRSSFeeds();
                    for (RSSFinder finder : getRSSFinders()) {
                        long lastRSS = -1;

                        for(RSS rss : rssFeeds)
                            if (rss.getDate() > finder.getLastRSS()) {
                                Message.send(finder.getChan(), rss.toStringDiscord());
                                lastRSS = rss.getDate();
                            }

                        if (lastRSS != -1)
                            finder.setLastRSS(lastRSS);
                    }

                    try {
                        sleep(DELTA);
                    } catch (InterruptedException e) {
                        LOG.error(e.getMessage());
                    }
                }
            }.start();
        }
    }

    public IChannel getChan() {
        return chan;
    }

    public long getLastRSS() {
        return lastRSS;
    }
}

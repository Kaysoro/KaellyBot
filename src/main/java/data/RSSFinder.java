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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 12/01/2017.
 */
public class RSSFinder {
    private final static Logger LOG = LoggerFactory.getLogger(RSSFinder.class);
    private final static long DELTA = 600000; // 10min
    private static boolean isStarted = false;

    private static List<RSSFinder> rssFinders = null;
    private String chan;
    private long lastRSS;

    public RSSFinder(String chan) {
        this(chan, System.currentTimeMillis());
    }

    private RSSFinder(String chan, long lastRSS) {
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
            preparedStatement.setString(2, getChan());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e);
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
                preparedStatement.setString(1, getChan());
                preparedStatement.setLong(2, getLastRSS());

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
            PreparedStatement request = connection.prepareStatement("DELETE FROM RSS_Finder WHERE id_chan = ?;");
            request.setString(1, getChan());
            request.executeUpdate();

        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error(getChan() + " : " + e.getMessage());
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
                    IChannel chan = ClientConfig.DISCORD().getChannelByID(resultSet.getString("id_chan"));
                    long lastUpdate = resultSet.getLong("last_update");

                    if (chan != null)
                        rssFinders.add(new RSSFinder(chan.getID(), lastUpdate));
                    else
                        new RSSFinder(resultSet.getString("id_chan")).removeToDatabase();
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
            new Thread() {
                public void run() {
                    while(true) {
                        List<RSS> rssFeeds = RSS.getRSSFeeds();
                        for (RSSFinder finder : getRSSFinders()) {
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
                }
            }.start();
        }
    }

    public String getChan() {
        return chan;
    }

    public long getLastRSS() {
        return lastRSS;
    }
}

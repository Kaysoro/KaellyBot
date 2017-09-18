package data;

import discord.Message;
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

    private static Map<String, RSSFinder> rssFinders = null;
    private String idGuild;
    private String chan;
    private long lastRSS;

    public RSSFinder(String idGuild, String chan) {
        this(idGuild, chan, System.currentTimeMillis());
    }

    private RSSFinder(String idGuild, String chan, long lastRSS) {
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
            preparedStatement.setString(2, getChan());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(getGuildId())),
                    null, ClientConfig.DISCORD().getChannelByID(Long.parseLong(getChan())), null);
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
                preparedStatement.setString(1, getGuildId());
                preparedStatement.setString(2, getChan());
                preparedStatement.setLong(3, getLastRSS());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(getGuildId())),
                        null, ClientConfig.DISCORD().getChannelByID(Long.parseLong(getChan())), null);
                LOG.error(e.getMessage());
            }
        }
    }

    public void removeToDatabase() {
        getRSSFinders().remove(getChan());

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM RSS_Finder WHERE id_chan = ?;");
            request.setString(1, getChan());
            request.executeUpdate();

        } catch (SQLException e) {
            ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(getGuildId())),
                    null, ClientConfig.DISCORD().getChannelByID(Long.parseLong(getChan())), null);
            LOG.error(getChan() + " : " + e.getMessage());
        }
    }

    public static Map<String, RSSFinder> getRSSFinders(){
        if (rssFinders == null){
            rssFinders = new HashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id_guild, id_chan, last_update FROM RSS_Finder");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    String idChan = resultSet.getString("id_chan");
                    String idGuild = resultSet.getString("id_guild");
                    long lastUpdate = resultSet.getLong("last_update");

                    IChannel chan = ClientConfig.DISCORD().getChannelByID(Long.parseLong(idChan));

                    if (chan != null && ! chan.isDeleted())
                        rssFinders.put(chan.getStringID(), new RSSFinder(chan.getGuild().getStringID(), chan.getStringID(), lastUpdate));
                    else {
                        new RSSFinder(idGuild, idChan).removeToDatabase();
                        LOG.info("Chan deleted : " + idChan);
                    }
                }
            } catch (SQLException e) {
                ClientConfig.setSentryContext(null, null, null, null);
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
                                    Message.sendText(ClientConfig.DISCORD().getChannelByID(
                                            Long.parseLong(finder.getChan())), rss.toStringDiscord());
                                    lastRSS = rss.getDate();
                                }

                            if (lastRSS != -1)
                                finder.setLastRSS(lastRSS);
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

    public String getChan() {
        return chan;
    }

    public String getGuildId() {
        return idGuild;
    }

    public long getLastRSS() {
        return lastRSS;
    }
}

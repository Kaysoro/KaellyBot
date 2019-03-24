package finders;

import enums.Language;
import listeners.TwitterListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import twitter4j.FilterQuery;
import util.ClientConfig;
import util.Connexion;
import util.Reporter;
import util.Translator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 12/01/2017.
 */
public class TwitterFinder{
    private final static Logger LOG = LoggerFactory.getLogger(TwitterFinder.class);
    protected static Map<Long, TwitterFinder> twitterChannels;
    private long guildId;
    private long channelId;

    public TwitterFinder(long guidId, long channelId) {
        this.guildId = guidId;
        this.channelId = channelId;
    }

    public synchronized static Map<Long, TwitterFinder> getTwitterChannels(){
        if (twitterChannels == null) {
            twitterChannels = new ConcurrentHashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id_guild, id_chan FROM Twitter");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    long idChan = Long.parseLong(resultSet.getString("id_chan"));
                    long idGuild = Long.parseLong(resultSet.getString("id_guild"));
                    IChannel chan = ClientConfig.DISCORD().getChannelByID(idChan);
                    if (chan != null && ! chan.isDeleted())
                        twitterChannels.put(chan.getLongID(), new TwitterFinder(idGuild, idChan));
                    else {
                        new TwitterFinder(idGuild, idChan).removeToDatabase();
                        LOG.info("Chan deleted : " + idChan);
                    }
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error("getTwitterChannels", e);
            }
        }
        return twitterChannels;
    }

    public synchronized void addToDatabase(){
        if (! getTwitterChannels().containsKey(getChannelId())) {
            getTwitterChannels().put(getChannelId(), this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Twitter(id_chan, id_guild) VALUES(?, ?);");
                preparedStatement.setString(1, String.valueOf(getChannelId()));
                preparedStatement.setString(2, String.valueOf(getGuildId()));

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Reporter.report(e, ClientConfig.DISCORD().getGuildByID(getGuildId()),
                        ClientConfig.DISCORD().getChannelByID(getChannelId()));
                LOG.error("addToDatabase", e);
            }
        }
    }

    public synchronized void removeToDatabase() {
        getTwitterChannels().remove(getChannelId());

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM Twitter WHERE id_chan = ?;");
            request.setString(1, String.valueOf(getChannelId()));
            request.executeUpdate();

        } catch (SQLException e) {
            Reporter.report(e, ClientConfig.DISCORD().getGuildByID(getGuildId()),
                    ClientConfig.DISCORD().getChannelByID(getChannelId()));
            LOG.error("removeToDatabase", e);
        }
    }

    public Long getChannelId(){
        return channelId;
    }

    public Long getGuildId(){
        return guildId;
    }

    public static void start() {
        if (ClientConfig.TWITTER() != null) {
            ClientConfig.TWITTER().addListener(new TwitterListener());

            long[] twitterIDs = new long[Language.values().length];
            int i = 0;
            for(Language lg : Language.values())
                twitterIDs[i++] = Long.parseLong(Translator.getLabel(lg, "twitter.id"));
            ClientConfig.TWITTER().filter(new FilterQuery(0, twitterIDs, new String[]{}));
        }
    }
}

package finders;

import discord4j.common.util.Snowflake;
import discord4j.rest.entity.RestChannel;
import discord4j.rest.http.client.ClientException;
import enums.Language;
import external.TwitterAPI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import payloads.TweetDto;
import util.ClientConfig;
import util.Connexion;
import util.Reporter;
import util.Translator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class TwitterFinder{
    private static final Logger LOG = LoggerFactory.getLogger(TwitterFinder.class);
    private static final long DELTA = 10; // 10min
    private static boolean isStarted = false;
    private static TwitterAPI twitterAPI;
    private static Map<String, TwitterFinder> twitterChannels;
    private String guildId;
    private String channelId;
    private long lastUpdate;

    public TwitterFinder(String idGuild, String chan) {
        this(idGuild, chan, System.currentTimeMillis());
    }

    public synchronized void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Twitter SET last_update = ? WHERE id_chan = ?;");
            preparedStatement.setLong(1, lastUpdate);
            preparedStatement.setString(2, getChannelId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("setLastUpdate", e);
        }
    }

    public synchronized void addToDatabase(){
        if (! getTwitterFinders().containsKey(getChannelId())) {
            getTwitterFinders().put(getChannelId(), this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Twitter(id_chan, id_guild, last_update) VALUES(?, ?, ?);");
                preparedStatement.setString(1, String.valueOf(getChannelId()));
                preparedStatement.setString(2, String.valueOf(getGuildId()));
                preparedStatement.setLong(3, getLastUpdate());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("addToDatabase", e);
            }
        }
    }

    public synchronized void removeToDatabase() {
        getTwitterFinders().remove(getChannelId());

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM Twitter WHERE id_chan = ?;");
            request.setString(1, String.valueOf(getChannelId()));
            request.executeUpdate();

        } catch (SQLException e) {
            LOG.error("removeToDatabase", e);
        }
    }

    public static synchronized Map<String, TwitterFinder> getTwitterFinders(){
        if (twitterChannels == null) {
            twitterChannels = new ConcurrentHashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id_guild, id_chan, last_update FROM Twitter");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    String idChan = resultSet.getString("id_chan");
                    String idGuild = resultSet.getString("id_guild");
                    long lastUpdate = resultSet.getLong("last_update");
                    twitterChannels.put(idChan, new TwitterFinder(idGuild, idChan, lastUpdate));
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error("getTwitterChannels", e);
            }
        }
        return twitterChannels;
    }

    public static void start(){
        if (!isStarted){
            LOG.info("Scheduling tweets polling from Twitter API...");
            twitterAPI = new TwitterAPI();
            isStarted = true;

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                LOG.info("Dispatching the most recent tweets from Twitter API...");
                Map<Language, List<TweetDto>> allTweets = twitterAPI.getTweets();

                for (TwitterFinder finder : getTwitterFinders().values())
                    try {
                        RestChannel chan = ClientConfig.DISCORD().getChannelById(Snowflake.of(finder.channelId));
                        List<TweetDto> tweets = allTweets.get(Translator.getLanguageFrom(chan));
                        long lastUpdate = -1;

                        for (TweetDto tweet : tweets)
                            if (tweet.getCreatedAt().toEpochMilli() > finder.getLastUpdate()) {
                                chan.createMessage(tweet.getUrl())
                                        .doOnError(error -> {
                                            if (error instanceof ClientException)
                                                LOG.warn("TwitterFinder: no access on " + finder.getChannelId());
                                            else LOG.error("TwitterFinder", error);
                                        })
                                        .subscribe();
                                lastUpdate = tweet.getCreatedAt().toEpochMilli();
                            }

                        if (lastUpdate != -1)
                            finder.setLastUpdate(lastUpdate);
                    } catch(ClientException e){
                        LOG.warn("TwitterFinder: no access on " + finder.getChannelId());
                    } catch(Exception e){
                        Reporter.report(e);
                        LOG.error("TwitterFinder", e);
                    }
            }, 0, DELTA, TimeUnit.MINUTES);
        }
    }
}

package util;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.shard.ShardingClientBuilder;
import io.sentry.Sentry;
import listeners.ReadyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * Created by steve on 14/07/2016.
 */
public class ClientConfig {
    
    private static ClientConfig instance = null;
    private final static Logger LOG = LoggerFactory.getLogger(ClientConfig.class);
    private final static String FILENAME = "config.properties";
    private Flux<DiscordClient> DISCORD;
    private TwitterStream TWITTER;
    private String KAELLY_PORTALS_URL;

    private ClientConfig(){
        this(System.getProperty("user.dir"));
    }

    private ClientConfig(String path){
        super();
        Properties prop = new Properties();
        String config = path + File.separator + FILENAME;

        try (FileInputStream file = new FileInputStream(URLDecoder.decode(config, "UTF-8"))){
            prop.load(file);

            KAELLY_PORTALS_URL = prop.getProperty("kaelly.portals.url");

            try {
                DISCORD = new ShardingClientBuilder(prop.getProperty("discord.token"))
                        .build()
                        .map(DiscordClientBuilder::build)
                        .cache();

                ReadyListener readyListener = new ReadyListener();

                DISCORD.flatMap(client -> client.getEventDispatcher().on(ReadyEvent.class))
                        .subscribe(event -> readyListener.onReady(event.getClient()));

            } catch(Throwable e){
                    LOG.error("Impossible de se connecter à Discord : verifiez votre token dans "
                            + FILENAME + " ainsi que votre connexion.");
            }

            if (! prop.get("sentry.dsn").equals(""))
                Sentry.init(prop.getProperty("sentry.dsn"));

            if (! prop.get("twitter.consumer_key").equals("") && ! prop.get("twitter.consumer_secret").equals("")
            && ! prop.get("twitter.access_token").equals("") && ! prop.get("twitter.access_token_secret").equals("")) {
                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(false)
                        .setOAuthConsumerKey(prop.getProperty("twitter.consumer_key"))
                        .setOAuthConsumerSecret(prop.getProperty("twitter.consumer_secret"))
                        .setOAuthAccessToken(prop.getProperty("twitter.access_token"))
                        .setOAuthAccessTokenSecret(prop.getProperty("twitter.access_token_secret"));

                TWITTER = new TwitterStreamFactory(cb.build()).getInstance();
            }
            else {
                LOG.warn("Un ou plusieurs tokens associés à Twitter sont manquants. TwitterFinder est par conséquent désactivé");
                TWITTER = null;
            }

            } catch(FileNotFoundException e){
                LOG.error("Fichier de configuration non trouvé.");
                TWITTER = null;
            } catch (UnsupportedEncodingException e) {
                LOG.error(e.getMessage());
            } catch (IOException e) {
                LOG.error("IOException rencontré : " + e.getMessage());
                TWITTER = null;
            }
    }

    public static synchronized ClientConfig getInstance(){
        if (instance == null)
            instance = new ClientConfig();
        return instance;
    }

    public static TwitterStream TWITTER() {
        return getInstance().TWITTER;
    }
    public static Flux<DiscordClient> DISCORD() {
        return getInstance().DISCORD;
    }

    public static void loginDiscord(){
        DISCORD().flatMap(DiscordClient::login).blockLast();
    }

    public static String KAELLY_PORTALS_URL(){
        return getInstance().KAELLY_PORTALS_URL;
    }

    public static void loginDiscord(String path) {
        DISCORD(path).flatMap(DiscordClient::login).blockLast();
    }

    private static Flux<DiscordClient> DISCORD(String path) {
        return getInstance(path).DISCORD;
    }

    public static synchronized ClientConfig getInstance(String path){
        if (instance == null)
            instance = new ClientConfig(path);
        return instance;
    }
}
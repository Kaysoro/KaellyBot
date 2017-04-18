package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
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
    private final static String FILENAME = "\\config.properties";
    private IDiscordClient DISCORD;
    private TwitterStream TWITTER;

    private ClientConfig(){
        super();
        Properties prop = new Properties();

        try {
            FileInputStream file = new FileInputStream(locationFile() + FILENAME);

            prop.load(file);
            file.close();

            try {
                DISCORD = new ClientBuilder()
                        .withToken(prop.getProperty("discord.token"))
                        .withRecommendedShardCount()
                        .login();
            } catch(DiscordException e){
                    LOG.error("Impossible de se connecter à Discord : verifiez votre token dans "
                            + FILENAME + " ainsi que votre connexion.");
            }

            ConfigurationBuilder cb = new ConfigurationBuilder();

            if (! prop.get("twitter.consumer_key").equals("") && ! prop.get("twitter.consumer_secret").equals("")
            && ! prop.get("twitter.access_token").equals("") && ! prop.get("twitter.access_token_secret").equals("")) {
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
            } catch (IOException e) {
                LOG.error("IOException rencontré : " + e.getMessage());
                TWITTER = null;
            }
    }

    public static ClientConfig getInstance(){
        if (instance == null)
            instance = new ClientConfig();
        return instance;
    }

    public static File locationFile()
    {
        String path = System.getProperty("user.dir");

        try {
            path = URLDecoder.decode(path, "UTF-8"); } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage());
        }
        File ret = new File(path);
        return ret;
    }

    public static TwitterStream TWITTER() {
        return getInstance().TWITTER;
    }
    public static IDiscordClient DISCORD() {
        return getInstance().DISCORD;
    }
}
package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

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
    private IDiscordClient CLIENT;

    private ClientConfig(){
        super();
        Properties prop = new Properties();

        try {
            FileInputStream file = new FileInputStream(locationFile() + FILENAME);

            prop.load(file);
            file.close();

            String token = prop.getProperty("discord.token");
            CLIENT = new ClientBuilder().withToken(token).login();

            } catch(FileNotFoundException e){
            LOG.error("Fichier de configuration non trouvé.");
            } catch (IOException e) {
            LOG.error("IOException rencontré : " + e.getMessage());
            } catch(DiscordException e){
            LOG.error("Impossible de se connecter à Discord : verifiez votre token dans "
                    + FILENAME + " ainsi que votre connexion.");
        }
    }

    public static ClientConfig getInstance(){
        if (instance == null)
            instance = new ClientConfig();
        return instance;
    }

    private static File locationFile()
    {
        String path = System.getProperty("user.dir");

        try {
            path = URLDecoder.decode(path, "UTF-8"); } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage());
        }
        File ret = new File(path);
        return ret;
    }

    public static IDiscordClient CLIENT() {
        return getInstance().CLIENT;
    }
}
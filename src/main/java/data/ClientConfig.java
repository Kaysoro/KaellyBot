package data;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by steve on 14/07/2016.
 */
public class ClientConfig {
    
    private static ClientConfig instance = null;
    private IDiscordClient client;
    private String token;
    private ClientConfig(){
        super();
        Properties prop = new Properties();
        String propFileName = "config\\config.properties";
        System.out.println( new File(propFileName));

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            prop.load(inputStream);
            inputStream.close();
            token = prop.getProperty("discord.token");

            client = new ClientBuilder().withToken(token).login();

            } catch(FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch(DiscordException e){
            e.printStackTrace();
        }
    }

    public static ClientConfig getInstance(){
        if (instance == null)
            instance = new ClientConfig();
        return instance;
    }

    public static IDiscordClient getIDiscordClient() {
        return getInstance().client;
    }

    public static String getToken() {
        return getInstance().token;
    }
}
package util;

import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

/**
 * Created by steve on 12/02/2017.
 */
public class Reporter {

    private static Logger LOG = LoggerFactory.getLogger(Reporter.class);
    private static Reporter instance = null;

    private final static String GUILD = "Guild";
    private final static String CHANNEL = "Channel";
    private final static String USER = "User";
    private final static String MESSAGE = "Message";


    private Reporter(){
        // Charge Sentry.dsn au cas où
        ClientConfig.getInstance();
    }

    private static Reporter getInstance(){
        if (instance == null)
            instance = new Reporter();
        return instance;
    }

    /**
     * Rejette une exception dans un salon discord via Sentry. Politique du "posté uniquement une fois"
     * @param e Exception à rejeter
     */
    public static void report(Exception e){
        Reporter.getInstance().send(e, null, null, null, null);
    }

    /**
     * Rejette une exception dans un salon discord. Politique du "posté uniquement une fois"
     * @param e Exception à rejeter
     * @param guild Guilde d'origine de l'erreur
     */
    public static void report(Exception e, IGuild guild){
        Reporter.getInstance().send(e, guild, null, null, null);
    }

    /**
     * Rejette une exception dans un salon discord. Politique du "posté uniquement une fois"
     * @param e Exception à rejeter
     * @param channel Salon d'origine de l'erreur
     */
    public static void report(Exception e, IChannel channel){
        Reporter.getInstance().send(e, null, channel, null, null);
    }

    /**
     * Rejette une exception dans un salon discord. Politique du "posté uniquement une fois"
     * @param exception Exception à rejeter
     * @param user Auteur du message d'origine de l'erreur
     */
    public static synchronized void report(Exception exception, IUser user){
        Reporter.getInstance().send(exception, null, null, user, null);
    }

    /**
     * Rejette une exception dans un salon discord. Politique du "posté uniquement une fois"
     * @param exception Exception à rejeter
     * @param guild Guilde d'origine de l'erreur
     * @param channel Salon d'origine de l'erreur
     */
    public static synchronized void report(Exception exception, IGuild guild, IChannel channel){
        Reporter.getInstance().send(exception, guild, channel, null, null);
    }

    /**
     * Rejette une exception dans un salon discord. Politique du "posté uniquement une fois"
     * @param exception Exception à rejeter
     * @param guild Guilde d'origine de l'erreur
     * @param channel Salon d'origine de l'erreur
     * @param user Auteur du message d'origine de l'erreur
     * @param message Contenu du message à l'origine de l'erreur
     */
    public static synchronized void report(Exception exception, IGuild guild, IChannel channel, IUser user, IMessage message){
        Reporter.getInstance().send(exception, guild, channel, user, message);
    }

    /**
     * Rejette une exception dans un salon discord. Politique du "posté uniquement une fois"
     * @param exception Exception à rejeter
     * @param guild Guilde d'origine de l'erreur
     * @param channel Salon d'origine de l'erreur
     * @param user Auteur du message d'origine de l'erreur
     * @param message Contenu du message à l'origine de l'erreur
     */
    public void send(Exception exception, IGuild guild, IChannel channel, IUser user, IMessage message){
        try {
            Sentry.getContext().clearTags();
            if (guild != null)
                Sentry.getContext().addTag(GUILD, guild.getStringID() + " - " + guild.getName());
            if (channel != null)
                Sentry.getContext().addTag(CHANNEL, channel.getStringID() + " - " + channel.getName());
            if (user != null)
                Sentry.getContext().addTag(USER, user.getStringID() + " - " + user.getName());
            if (message != null)
                Sentry.getContext().addTag(MESSAGE, message.getContent());

            Sentry.capture(exception);

        } catch(Exception e){
            Sentry.capture(exception);
            LOG.error("report", exception);
            Sentry.capture(e);
            LOG.error("report", e);
        }
    }
}
package util;

import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static void report(Exception e, Guild guild){
        Reporter.getInstance().send(e, guild, null, null, null);
    }

    /**
     * Rejette une exception dans un salon discord. Politique du "posté uniquement une fois"
     * @param e Exception à rejeter
     * @param channel Salon d'origine de l'erreur
     */
    public static void report(Exception e, Channel channel){
        Reporter.getInstance().send(e, null, channel, null, null);
    }

    /**
     * Rejette une exception dans un salon discord. Politique du "posté uniquement une fois"
     * @param exception Exception à rejeter
     * @param user Auteur du message d'origine de l'erreur
     */
    public static synchronized void report(Exception exception, User user){
        Reporter.getInstance().send(exception, null, null, user, null);
    }

    /**
     * Rejette une exception dans un salon discord. Politique du "posté uniquement une fois"
     * @param exception Exception à rejeter
     * @param guild Guilde d'origine de l'erreur
     * @param channel Salon d'origine de l'erreur
     */
    public static synchronized void report(Exception exception, Guild guild, Channel channel){
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
    public static synchronized void report(Exception exception, Guild guild, Channel channel, User user, discord4j.core.object.entity.Message message){
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
    public void send(Exception exception, Guild guild, Channel channel, User user, discord4j.core.object.entity.Message message){
        try {
            Sentry.getContext().clearTags();
            if (guild != null)
                Sentry.getContext().addTag(GUILD, guild.getId().asString() + " - " + guild.getName());
            if (channel != null)
                Sentry.getContext().addTag(CHANNEL, channel.getId().asString() + " - " + channel.getId());
            if (user != null)
                Sentry.getContext().addTag(USER, user.getId().asString() + " - " + user.getUsername());
            if (message != null)
                Sentry.getContext().addTag(MESSAGE, message.getContent().orElse(""));

            Sentry.capture(exception);

        } catch(Exception e){
            Sentry.capture(exception);
            LOG.error("report", exception);
            Sentry.capture(e);
            LOG.error("report", e);
        }
    }
}
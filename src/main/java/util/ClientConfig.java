package util;

import data.Constants;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.event.domain.guild.GuildUpdateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.core.object.presence.Presence;
import discord4j.core.shard.MemberRequestFilter;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import io.sentry.Sentry;
import listeners.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
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
    private DiscordClient DISCORD;
    private TwitterStream TWITTER;

    private ClientConfig(){
        this(System.getProperty("user.dir"));
    }

    private ClientConfig(String path){
        super();
        Properties prop = new Properties();
        String config = path + File.separator + FILENAME;

        try (FileInputStream file = new FileInputStream(URLDecoder.decode(config, "UTF-8"))){
            prop.load(file);

            try {
                DISCORD = DiscordClient.create(prop.getProperty("discord.token"));
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

    public static synchronized ClientConfig getInstance(String path){
        if (instance == null)
            instance = new ClientConfig(path);
        return instance;
    }

    public static synchronized ClientConfig getInstance(){
        if (instance == null)
            instance = new ClientConfig();
        return instance;
    }

    public static TwitterStream TWITTER() {
        return getInstance().TWITTER;
    }

    public static DiscordClient DISCORD() {
        return getInstance().DISCORD;
    }

    public void loginDiscord(){
        DISCORD().gateway()
                .setEnabledIntents(IntentSet.of(
                        Intent.GUILDS,
                        Intent.GUILD_MEMBERS,
                        Intent.GUILD_MESSAGES,
                        Intent.GUILD_MESSAGE_REACTIONS,
                        Intent.DIRECT_MESSAGES))
                .setInitialPresence(ignored -> ClientPresence.online(ClientActivity.watching(Constants.discordInvite)))
                .setMemberRequestFilter(MemberRequestFilter.none())
                .withGateway(client -> Mono.when(
                        readyListener(client),
                        guildCreateListener(client),
                        guildUpdateListener(client),
                        guildDeleteListener(client),
                        commandListener(client)))
                .block();
    }

    private Mono<Void> commandListener(GatewayDiscordClient client){
        final MessageListener listener = new MessageListener();
        return client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(listener::onReady)
                .then();
    }

    private Mono<Void> guildCreateListener(GatewayDiscordClient client){
        final GuildCreateListener listener = new GuildCreateListener();
        return client.getEventDispatcher().on(GuildCreateEvent.class)
                .flatMap(listener::onReady)
                .then();
    }

    private Mono<Void> guildUpdateListener(GatewayDiscordClient client){
        final GuildUpdateListener listener = new GuildUpdateListener();
        return client.getEventDispatcher().on(GuildUpdateEvent.class)
                .flatMap(listener::onReady)
                .then();
    }

    private Mono<Void> guildDeleteListener(GatewayDiscordClient client){
        final GuildLeaveListener listener = new GuildLeaveListener();
        return client.getEventDispatcher().on(GuildDeleteEvent.class)
                .filter(event -> (! event.isUnavailable()))
                .flatMap(listener::onReady)
                .then();
    }

    private Mono<Void> readyListener(GatewayDiscordClient client){
        final ReadyListener listener = new ReadyListener();
        return client.getEventDispatcher().on(ReadyEvent.class)
                .flatMap(listener::onReady)
                .then();
    }
}
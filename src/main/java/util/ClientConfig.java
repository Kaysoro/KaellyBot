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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import external.TwitterAPI;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by steve on 14/07/2016.
 */
public class ClientConfig {
    
    private static ClientConfig instance = null;
    private final static Logger LOG = LoggerFactory.getLogger(ClientConfig.class);
    private final static String FILENAME = "config.properties";
    private DiscordClient DISCORD;
    private TwitterAPI TWITTER;

    private ClientConfig(){
        this(System.getProperty("user.dir"));
    }

    private ClientConfig(String path){
        super();
        Properties prop = new Properties();
        String config = path + File.separator + FILENAME;

        try (FileInputStream file = new FileInputStream(URLDecoder.decode(config, StandardCharsets.UTF_8))){
            prop.load(file);
        } catch(FileNotFoundException e){
            LOG.error("Configuration file not found");
            TWITTER = null;
        } catch (IOException e) {
            LOG.error("IOException encountered", e);
            TWITTER = null;
        }

		try {
			DISCORD = DiscordClient.create(prop.getProperty("discord.token"));
		} catch(Throwable e){
			LOG.error("Impossible de se connecter à Discord : verifiez votre token dans "
					+ FILENAME + " ainsi que votre connexion.");
		}

		if (! prop.get("sentry.dsn").equals(""))
			Sentry.init(prop.getProperty("sentry.dsn"));

        Optional.ofNullable(prop.get("twitter.bearer_token"))
                .map(Object::toString)
                .filter(StringUtils::isNotBlank)
                .ifPresentOrElse(bearerToken -> TWITTER = new TwitterAPI(bearerToken),
                        () -> LOG.error("Twitter bearer token missing, Twitter Finder disabled"));
    }

    public static synchronized ClientConfig getInstance(){
        if (instance == null)
            instance = new ClientConfig();
        return instance;
    }

    public static TwitterAPI TWITTER() {
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
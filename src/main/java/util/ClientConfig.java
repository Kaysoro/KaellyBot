package util;

import data.Constants;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.event.domain.guild.GuildUpdateEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.core.shard.MemberRequestFilter;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import io.sentry.Sentry;
import listeners.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by steve on 14/07/2016.
 */
public class ClientConfig {
    
    private static ClientConfig instance = null;
    private static final Logger LOG = LoggerFactory.getLogger(ClientConfig.class);
    private static final String FILENAME = "config.properties";
    private DiscordClient DISCORD;
    private final String DOFUS_PORTALS_URL;
    private final String DOFUS_PORTALS_TOKEN;
    private final String TWITTER_URL;
    private final String KAELLY_CACHE_URL;
    private final String KAELLY_CACHE_PASSWORD;

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
        } catch (IOException e) {
            LOG.error("IOException encountered", e);
        }

        DOFUS_PORTALS_URL = prop.getProperty("dofus_portals.url");
        DOFUS_PORTALS_TOKEN = prop.getProperty("dofus_portals.token");
        TWITTER_URL = prop.getProperty("twitter.url");
        KAELLY_CACHE_URL = prop.getProperty("kaelly.cache.url");
        KAELLY_CACHE_PASSWORD = prop.getProperty("kaelly.cache.password");

        try {
            DISCORD = DiscordClient.create(prop.getProperty("discord.token"));
        } catch(Exception e){
                LOG.error("Impossible to connect to Discord: check your token in "
                        + FILENAME + " as well as your connection.");
        }

        if (! prop.get("sentry.dsn").equals(""))
            Sentry.init(prop.getProperty("sentry.dsn"));
    }

    public static synchronized ClientConfig getInstance(){
        if (instance == null)
            instance = new ClientConfig();
        return instance;
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
                .setInitialPresence($ -> ClientPresence.online(ClientActivity.watching(Constants.discordInvite)))
                .setMemberRequestFilter(MemberRequestFilter.none())
                .withGateway(client -> Mono.when(
                        readyListener(client),
                        guildCreateListener(client),
                        guildUpdateListener(client),
                        guildDeleteListener(client),
                        legacyCommandListener(client),
                        slashCommandListener(client)))
                .block();
    }

    public static String DOFUS_PORTALS_URL(){
        return getInstance().DOFUS_PORTALS_URL;
    }

    public static String DOFUS_PORTALS_TOKEN(){
        return getInstance().DOFUS_PORTALS_TOKEN;
    }

    public static String TWITTER_URL(){
        return getInstance().TWITTER_URL;
    }

    public static String KAELLY_CACHE_URL() {
        return getInstance().KAELLY_CACHE_URL;
    }

    public static String KAELLY_CACHE_PASSWORD() {
        return getInstance().KAELLY_CACHE_PASSWORD;
    }

    public static synchronized ClientConfig getInstance(String path){
        if (instance == null)
            instance = new ClientConfig(path);
        return instance;
    }

    private Mono<Void> legacyCommandListener(GatewayDiscordClient client){
        final MessageListener listener = new MessageListener();
        return client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(listener::onReady)
                .then();
    }

    private Mono<Void> slashCommandListener(GatewayDiscordClient client){
        final SlashCommandListener listener = new SlashCommandListener();
        return client.getEventDispatcher().on(ChatInputInteractionEvent.class)
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
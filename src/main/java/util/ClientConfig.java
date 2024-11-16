package util;

import data.Constants;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.core.shard.MemberRequestFilter;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import listeners.MessageListener;
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

        try {
            DISCORD = DiscordClient.create(prop.getProperty("discord.token"));
        } catch(Exception e){
                LOG.error("Impossible to connect to Discord: check your token in "
                        + FILENAME + " as well as your connection.");
        }
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
                .withGateway(client -> Mono.when(legacyCommandListener(client)))
                .block();
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
}
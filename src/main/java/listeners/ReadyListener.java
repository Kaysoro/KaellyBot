package listeners;

import data.*;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.channel.TextChannelDeleteEvent;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.event.domain.guild.GuildUpdateEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import finders.AlmanaxCalendar;
import finders.RSSFinder;
import finders.TwitterFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by steve on 14/07/2016.
 */
public class ReadyListener {
    private final static Logger LOG = LoggerFactory.getLogger(ReadyListener.class);

    private MessageListener messageListener;
    private GuildCreateListener guildCreateListener;
    private GuildLeaveListener guildLeaveListener;
    private GuildUpdateListener guildUpdateListener;
    private ChannelDeleteListener channelDeleteListener;

    public ReadyListener(){
         messageListener = new MessageListener();
         guildCreateListener = new GuildCreateListener();
         guildLeaveListener = new GuildLeaveListener();
         guildUpdateListener = new GuildUpdateListener();
         channelDeleteListener = new ChannelDeleteListener();
    }

    public Flux<Void> onReady(DiscordClient client) {
        long time = System.currentTimeMillis();

        LOG.info("Ajout des différents listeners...");
        Flux<Void> result = client.getEventDispatcher().on(GuildCreateEvent.class)
                .flatMap(guildCreateEvent -> guildCreateListener.onReady(client, guildCreateEvent))
                .thenMany(client.getEventDispatcher().on(GuildDeleteEvent.class))
                .flatMap(guildDeleteEvent -> guildLeaveListener.onReady(guildDeleteEvent))
                .thenMany(client.getEventDispatcher().on(GuildUpdateEvent.class))
                .flatMap(guildUpdateEvent -> guildUpdateListener.onReady(guildUpdateEvent))
                .thenMany(client.getEventDispatcher().on(TextChannelDeleteEvent.class))
                .flatMap(textChannelDeleteEvent -> channelDeleteListener.onReady(textChannelDeleteEvent));

        LOG.info("Check des guildes...");

        result = result.thenMany(client.getGuilds().collectList())
                .flatMap(guilds -> {
                    for (discord4j.core.object.entity.Guild guild : guilds)
                        if (Guild.getGuilds().containsKey(guild.getId().asString())
                                && !guild.getName().equals(Guild.getGuild(guild).getName()))
                            Guild.getGuild(guild).setName(guild.getName());
                        else
                            client.getEventDispatcher().publish(new GuildCreateEvent(client, guild));
                    return Flux.empty();
                });

        // Joue à...
        result = result.thenMany(client.updatePresence(Presence.online(Activity.watching(Constants.discordInvite))));

        LOG.info("Ecoute des flux RSS du site Dofus...");
        RSSFinder.start();

        LOG.info("Lancement du calendrier Almanax...");
        AlmanaxCalendar.start();

        LOG.info("Connexion à l'API Twitter...");
        TwitterFinder.start();

        LOG.info("Ecoute des messages...");
        result = result.thenMany(client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(msgEvent -> messageListener.onReady(client, msgEvent)))
        .then().concatWith(Mono.empty());

        LOG.info("Mise en place des ressources en " + (System.currentTimeMillis() - time) + "ms");
        return result;
    }
}

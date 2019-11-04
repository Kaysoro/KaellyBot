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

import java.util.Collections;

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
    private boolean isReadyOnce;

    public ReadyListener(){
         messageListener = new MessageListener();
         guildCreateListener = new GuildCreateListener();
         guildLeaveListener = new GuildLeaveListener();
         guildUpdateListener = new GuildUpdateListener();
         channelDeleteListener = new ChannelDeleteListener();
         isReadyOnce = false;
    }

    public void onReady(DiscordClient client) {

        if (!isReadyOnce) {
            long time = System.currentTimeMillis();

            LOG.info("Ajout des différents listeners...");
            client.getEventDispatcher().on(GuildCreateEvent.class)
                    .subscribe(guildCreateEvent -> guildCreateListener.onReady(client, guildCreateEvent));
            client.getEventDispatcher().on(GuildDeleteEvent.class)
                    .subscribe(guildDeleteEvent -> guildLeaveListener.onReady(guildDeleteEvent));
            client.getEventDispatcher().on(GuildUpdateEvent.class)
                    .subscribe(guildUpdateEvent -> guildUpdateListener.onReady(guildUpdateEvent));
            client.getEventDispatcher().on(TextChannelDeleteEvent.class)
                    .subscribe(textChannelDeleteEvent -> channelDeleteListener.onReady(textChannelDeleteEvent));

            LOG.info("Check des guildes...");
            client.getGuilds().collectList().blockOptional().orElse(Collections.emptyList())
                    .forEach(guild -> {
                        if (Guild.getGuilds().containsKey(guild.getId().asString())
                                && !guild.getName().equals(Guild.getGuild(guild).getName()))
                            Guild.getGuild(guild).setName(guild.getName());
                        else
                            client.getEventDispatcher().publish(new GuildCreateEvent(client, guild));
                    });

            // Joue à...
            client.updatePresence(Presence.online(Activity.watching(Constants.discordInvite))).subscribe();

            LOG.info("Ecoute des flux RSS du site Dofus...");
            RSSFinder.start();

            LOG.info("Lancement du calendrier Almanax...");
            AlmanaxCalendar.start();

            LOG.info("Connexion à l'API Twitter...");
            TwitterFinder.start();

            LOG.info("Ecoute des messages...");
            client.getEventDispatcher().on(MessageCreateEvent.class)
                    .subscribe(event -> messageListener.onReady(event));

            isReadyOnce = true;
            LOG.info("Mise en place des ressources en " + (System.currentTimeMillis() - time) + "ms");

        }
    }
}

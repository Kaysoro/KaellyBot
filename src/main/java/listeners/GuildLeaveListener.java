package listeners;

import data.*;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Flux;
import finders.AlmanaxCalendar;
import finders.RSSFinder;
import finders.TwitterFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientConfig;
import util.Reporter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildLeaveListener {

    private final static Logger LOG = LoggerFactory.getLogger(GuildLeaveListener.class);

    public GuildLeaveListener(){
        super();
    }

    public void onReady(GuildDeleteEvent event) {
        try {

            Optional<Guild> optionalGuild = event.getGuild().map(guildEvent -> Guild.getGuild(guildEvent, false));
            if (optionalGuild.isPresent()) {
                Guild guild = optionalGuild.get();
                guild.removeToDatabase();

                LOG.info("La guilde " + event.getGuildId().asString() + " - " + guild.getName()
                        + " a supprimé " + Constants.name);

                List<TextChannel> channels = event.getGuild().map(guildEvent -> guildEvent.getChannels()
                        .filter(channel -> channel instanceof TextChannel)
                        .map(channel -> (TextChannel) channel).collectList().block())
                        .orElse(Collections.emptyList());

                for(TextChannel channel : channels) {
                    if (RSSFinder.getRSSFinders().containsKey(channel.getId().asString())) {
                        RSSFinder.getRSSFinders().get(channel.getId().asString()).removeToDatabase();
                        LOG.info("RSS Chan \"" + channel.getName() + "\"");
                    }

                    if (TwitterFinder.getTwitterChannels().containsKey(channel.getId().asLong())) {
                        TwitterFinder.getTwitterChannels().get(channel.getId().asLong()).removeToDatabase();
                        LOG.info("Twitter Chan \"" + channel.getName() + "\"");
                    }

                    if (AlmanaxCalendar.getAlmanaxCalendars().containsKey(channel.getId().asString())) {
                        AlmanaxCalendar.getAlmanaxCalendars().get(channel.getId().asString()).removeToDatabase();
                        LOG.info("Almanax Chan \"" + channel.getName() + "\"");
                    }
                }

                Flux.fromIterable(ClientConfig.DISCORD())
                        .flatMap(cli -> cli.getChannelById(Snowflake.of(Constants.chanReportID)))
                        .filter(chan -> chan instanceof TextChannel)
                        .map(chan -> (TextChannel) chan)
                        .flatMap(chan -> chan.getGuild().flatMap(guildLost -> chan
                                .createMessage("[LOSE] **" + guildLost.getName() + "**, -"
                                        + guildLost.getMemberCount().orElse(0) +  " utilisateurs")))
                .subscribe();
            }
        } catch(Exception e){
            Reporter.report(e, event.getGuild().orElse(null));
            LOG.error("onReady", e);
        }
    }
}

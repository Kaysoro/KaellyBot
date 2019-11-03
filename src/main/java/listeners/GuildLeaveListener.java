package listeners;

import data.*;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Flux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientConfig;
import util.Reporter;

import java.util.Optional;
import java.util.OptionalInt;

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

                Flux.fromIterable(ClientConfig.DISCORD())
                        .flatMap(cli -> cli.getChannelById(Snowflake.of(Constants.chanReportID)))
                        .filter(chan -> chan instanceof TextChannel)
                        .map(chan -> (TextChannel) chan)
                        .flatMap(chan -> chan.createMessage("[LOSE] **" + optionalGuild.get().getName() + "**, -"
                                        + event.getGuild().map(discord4j.core.object.entity.Guild::getMemberCount)
                                .orElse(OptionalInt.empty()).orElse(0) +  " utilisateurs"))
                        .subscribe();
            }
        } catch(Exception e){
            Reporter.report(e, event.getGuild().orElse(null));
            LOG.error("onReady", e);
        }
    }
}

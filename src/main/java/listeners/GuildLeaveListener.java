package listeners;

import data.*;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.discordjson.json.MessageData;
import discord4j.rest.entity.RestChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import util.ClientConfig;
import util.Reporter;

import java.util.Optional;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildLeaveListener {

    private final static Logger LOG = LoggerFactory.getLogger(GuildLeaveListener.class);

    public GuildLeaveListener(){
        super();
    }

    public Mono<MessageData> onReady(GuildDeleteEvent event) {
        try {
            Optional<Guild> optionalGuild = event.getGuild().map(guildEvent -> Guild.getGuild(guildEvent, false));
            if (optionalGuild.isPresent()) {
                Guild guild = optionalGuild.get();
                guild.removeToDatabase();

                LOG.info("La guilde " + event.getGuildId().asString() + " - " + guild.getName()
                        + " a supprimé " + Constants.name);

                RestChannel channel = ClientConfig.DISCORD().getChannelById(Snowflake.of(Constants.chanReportID));
                return channel.createMessage("[LOSE] **" + optionalGuild.get().getName() + "**, -"
                        + event.getGuild().map(discord4j.core.object.entity.Guild::getMemberCount)
                        .orElse(0) +  " utilisateurs");
            }
        } catch(Exception e){
            Reporter.report(e, event.getGuild().orElse(null));
            LOG.error("onReady", e);
        }
        return Mono.empty();
    }
}

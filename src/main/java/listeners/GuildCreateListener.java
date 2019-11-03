package listeners;

import commands.classic.HelpCommand;
import commands.config.*;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import enums.Language;
import reactor.core.publisher.Flux;
import util.ClientConfig;
import data.Constants;
import data.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reporter;
import util.Translator;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildCreateListener {

    private final static Logger LOG = LoggerFactory.getLogger(GuildCreateListener.class);

    public void onReady(DiscordClient client, GuildCreateEvent event) {
        try {
            if (!Guild.getGuilds().containsKey(event.getGuild().getId().asString())) {

                event.getGuild().getChannels()
                        .filter(chan -> chan instanceof TextChannel)
                        .map(chan -> (TextChannel) chan).take(1).flatMap(chan -> {
                    Guild guild = new Guild(event.getGuild().getId().asString(), event.getGuild().getName(),
                            Translator.detectLanguage(chan));
                    guild.addToDatabase();

                    Language lg = guild.getLanguage();
                    LOG.info("La guilde " + guild.getId() + " - " + guild.getName() + " a ajouté " + Constants.name);

                    return event.getGuild().getOwner().flatMap(owner -> {
                        String customMessage = Translator.getLabel(lg, "welcome.message")
                                .replaceAll("\\{name}", Constants.name)
                                .replaceAll("\\{game}", Constants.game.getName())
                                .replaceAll("\\{prefix}", Constants.prefixCommand)
                                .replaceAll("\\{help}", HelpCommand.NAME)
                                .replaceAll("\\{server}", new ServerCommand().getName())
                                .replaceAll("\\{lang}", new LanguageCommand().getName())
                                .replaceAll("\\{twitter}", new TwitterCommand().getName())
                                .replaceAll("\\{almanax-auto}", new AlmanaxAutoCommand().getName())
                                .replaceAll("\\{rss}", new RSSCommand().getName())
                                .replaceAll("\\{owner}", owner.getMention())
                                .replaceAll("\\{guild}", event.getGuild().getName());

                        return chan.getEffectivePermissions(client.getSelfId().orElse(null))
                                .flatMap(perm -> perm.contains(Permission.SEND_MESSAGES) ?
                                        chan.createMessage(customMessage) : event.getGuild().getOwner()
                                        .flatMap(Member::getPrivateChannel)
                                        .flatMap(ownerChan -> ownerChan.createMessage(customMessage)))
                                .thenMany(Flux.fromIterable(ClientConfig.DISCORD())
                                        .flatMap(cli -> cli.getChannelById(Snowflake.of(Constants.chanReportID)))
                                        .filter(channel -> channel instanceof TextChannel)
                                        .map(channel -> (TextChannel) channel)
                                        .flatMap(channel -> channel.createMessage("[NEW] **" + event.getGuild().getName()
                                                + "** (" + guild.getLanguage().getAbrev() + "), +"
                                                + event.getGuild().getMemberCount().orElse(0) +  " utilisateurs")))
                                .collectList();
                    });
                }).subscribe();
            }
        } catch(Exception e){
            Reporter.report(e, event.getGuild());
            LOG.error("onReady", e);
        }
    }
}

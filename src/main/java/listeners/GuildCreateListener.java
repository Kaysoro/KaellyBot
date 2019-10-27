package listeners;

import commands.classic.HelpCommand;
import commands.config.*;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import enums.Language;
import reactor.core.publisher.Mono;
import util.ClientConfig;
import data.Constants;
import data.Guild;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reporter;
import util.Translator;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildCreateListener {

    private final static Logger LOG = LoggerFactory.getLogger(GuildCreateListener.class);

    public Mono<Void> onReady(DiscordClient client, GuildCreateEvent event) {
        try {
            if (!Guild.getGuilds().containsKey(event.getGuild().getId().asString())) {


                Guild guild = new Guild(event.getGuild().getId().asString(), event.getGuild().getName(),
                        Translator.detectLanguage(event.getGuild().getChannels().blockFirst()));
                guild.addToDatabase();

                Language lg = guild.getLanguage();
                LOG.info("La guilde " + guild.getId() + " - " + guild.getName() + " a ajouté " + Constants.name);

                String customMessage = Translator.getLabel(lg, "welcome.message");

                customMessage = customMessage
                        .replaceAll("\\{name\\}", Constants.name)
                        .replaceAll("\\{game\\}", Constants.game.getName())
                        .replaceAll("\\{prefix\\}", Constants.prefixCommand)
                        .replaceAll("\\{help\\}", HelpCommand.NAME)
                        .replaceAll("\\{server\\}", new ServerCommand().getName())
                        .replaceAll("\\{lang\\}", new LanguageCommand().getName())
                        .replaceAll("\\{twitter\\}", new TwitterCommand().getName())
                        .replaceAll("\\{almanax-auto\\}", new AlmanaxAutoCommand().getName())
                        .replaceAll("\\{rss\\}", new RSSCommand().getName())
                        .replaceAll("\\{owner\\}", event.getGuild().getOwner().mention())
                        .replaceAll("\\{guild\\}", event.getGuild().getName());

                if (event.getGuild().getDefaultChannel() != null && event.getGuild().getDefaultChannel()
                        .getModifiedPermissions(ClientConfig.DISCORD().getOurUser())
                        .contains(Permissions.SEND_MESSAGES))
                    Message.sendText(event.getGuild().getDefaultChannel(), customMessage);
                else try {
                    Message.sendText(event.getGuild().getOwner().getOrCreatePMChannel(), customMessage);
                } catch (DiscordException e) {
                    LOG.warn("onReady", "Impossible de contacter l'administrateur de la guilde ["
                            + guild.getName() + "].");
                }

                Message.sendText(ClientConfig.DISCORD().getChannelByID(Constants.chanReportID),
                        "[NEW] **" + guild.getName() + "** (" + guild.getLanguage().getAbrev() + "), +"
                                + event.getGuild().getUsers().size() + " utilisateurs");

            }
        } catch(Exception e){
            Reporter.report(e, event.getGuild());
            LOG.error("onReady", e);
        }

        return Mono.empty();
    }
}

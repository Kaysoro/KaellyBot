package listeners;

import commands.classic.HelpCommand;
import commands.config.*;
import enums.Language;
import sx.blah.discord.util.DiscordException;
import util.ClientConfig;
import data.Constants;
import data.Guild;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.Permissions;
import util.Reporter;
import util.Translator;

/**
 * Created by steve on 14/07/2016.
 */
public class GuildCreateListener {

    private final static Logger LOG = LoggerFactory.getLogger(GuildCreateListener.class);

    public GuildCreateListener(){
        super();
    }

    @EventSubscriber
    public void onReady(GuildCreateEvent event) {
        try {
            if (!Guild.getGuilds().containsKey(event.getGuild().getStringID())) {
                Guild guild = new Guild(event.getGuild().getStringID(), event.getGuild().getName(),
                        Translator.detectLanguage(event.getGuild().getDefaultChannel()));
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
    }
}

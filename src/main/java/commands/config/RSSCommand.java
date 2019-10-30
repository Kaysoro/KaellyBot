package commands.config;

import commands.model.AbstractCommand;
import data.Constants;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import enums.Language;
import exceptions.*;
import finders.RSSFinder;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class RSSCommand extends AbstractCommand {

    private DiscordException rssFound;
    private DiscordException rssNotFound;

    public RSSCommand(){
        super("rss","(\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)");
        setUsableInMP(false);
        rssFound = new AdvancedDiscordException("exception.advanced.rss_found", new String[]{"game.url"}, new Boolean[]{true});
        rssNotFound = new AdvancedDiscordException("exception.advanced.rss_not_found", new String[]{"game.url"}, new Boolean[]{true});
    }

    @Override
    public void request(Message message, Matcher m, Language lg) {
        //On check si la personne a bien les droits pour exécuter cette commande
        if (isUserHasEnoughRights(message)) {
            String value = m.group(1);

            String guildId = message.getGuild().blockOptional()
                    .map(Guild::getId).map(Snowflake::asString).orElse("");
            String channelId = message.getChannel().blockOptional()
                    .map(MessageChannel::getId).map(Snowflake::asString).orElse("");

            if (value.matches("\\s+true") || value.matches("\\s+0") || value.matches("\\s+on")){

                if (!RSSFinder.getRSSFinders().containsKey(channelId)) {
                    new RSSFinder(guildId, channelId).addToDatabase();
                    message.getChannel().flatMap(chan -> chan
                            .createMessage(Translator.getLabel(lg, "rss.request.1")
                                    .replace("{game.url}", Translator.getLabel(lg, "game.url"))))
                            .subscribe();
                }
                else
                    rssFound.throwException(message, this, lg);
            }
            else if (value.matches("\\s+false") || value.matches("\\s+1") || value.matches("\\s+off"))
                if (RSSFinder.getRSSFinders().containsKey(channelId)){
                    RSSFinder.getRSSFinders().get(channelId).removeToDatabase();
                    message.getChannel().flatMap(chan -> chan
                            .createMessage(Translator.getLabel(lg, "rss.request.2")
                                    .replace("{game.url}", Translator.getLabel(lg, "game.url"))))
                            .subscribe();
                }
                else
                    rssNotFound.throwException(message, this, lg);
            else
                badUse.throwException(message, this, lg);
        } else
            BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "rss.help")
                .replace("{game}", Constants.game.getName());
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " true` : " + Translator.getLabel(lg, "rss.help.detailed.1")
                .replace("{game.url}", Translator.getLabel(lg, "game.url"))
                + "\n`" + prefixe + name + " false` : " + Translator.getLabel(lg, "rss.help.detailed.2") + "\n";
    }
}

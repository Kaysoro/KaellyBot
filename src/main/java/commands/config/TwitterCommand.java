package commands.config;

import commands.model.AbstractCommand;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import enums.Language;
import finders.TwitterFinder;
import exceptions.*;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class TwitterCommand extends AbstractCommand {

    private DiscordException twitterFound;
    private DiscordException twitterNotFound;

    public TwitterCommand(){
        super("twitter", "(\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)");
        setUsableInMP(false);
        twitterFound = new AdvancedDiscordException("exception.advanced.tweet_found", new String[]{"twitter.name"}, new Boolean[]{true});
        twitterNotFound = new AdvancedDiscordException("exception.advanced.tweet_not_found", new String[]{"twitter.name"}, new Boolean[]{true});
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        //On check si la personne a bien les droits pour exécuter cette commande
        if (isUserHasEnoughRights(message)) {
            String value = m.group(1);

            Long guildId = message.getGuild().blockOptional()
                    .map(Guild::getId).map(Snowflake::asLong).orElse(0L);
            Long channelId = message.getChannel().blockOptional()
                    .map(MessageChannel::getId).map(Snowflake::asLong).orElse(0L);

            if (value.matches("\\s+true") || value.matches("\\s+0") || value.matches("\\s+on")){
                if (! TwitterFinder.getTwitterChannels().containsKey(channelId)) {
                    new TwitterFinder(guildId, channelId).addToDatabase();
                    message.getChannel().flatMap(chan -> chan
                            .createMessage(Translator.getLabel(lg, "twitter.request.1")
                            .replace("{twitter.name}", Translator.getLabel(lg, "twitter.name"))))
                            .subscribe();
                }
                else
                    twitterFound.throwException(message, this, lg);
            }
            else if (value.matches("\\s+false") || value.matches("\\s+1") || value.matches("\\s+off")){
                if (TwitterFinder.getTwitterChannels().containsKey(channelId)) {
                    TwitterFinder.getTwitterChannels().get(channelId).removeToDatabase();
                    message.getChannel().flatMap(chan -> chan
                            .createMessage(Translator.getLabel(lg, "twitter.request.2")
                                    .replace("{twitter.name}", Translator.getLabel(lg, "twitter.name"))))
                            .subscribe();
                }
                else
                    twitterNotFound.throwException(message, this, lg);
            }
            else
                badUse.throwException(message, this, lg);
        }
        else
            BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "twitter.help")
                .replace("{twitter.name}", Translator.getLabel(lg, "twitter.name"));
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " true` : " + Translator.getLabel(lg, "twitter.help.detailed.1")
                    .replace("{twitter.name}", Translator.getLabel(lg, "twitter.name"))
                + "\n`" + prefixe + name + " false` : " + Translator.getLabel(lg, "twitter.help.detailed.2") + "\n";
    }
}

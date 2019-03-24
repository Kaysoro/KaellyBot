package commands.config;

import commands.model.AbstractCommand;
import enums.Language;
import finders.TwitterFinder;
import util.Message;
import exceptions.*;
import sx.blah.discord.handle.obj.IMessage;
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
    public void request(IMessage message, Matcher m, Language lg) {
        //On check si la personne a bien les droits pour exécuter cette commande
        if (isUserHasEnoughRights(message)) {
            String value = m.group(1);

            if (value.matches("\\s+true") || value.matches("\\s+0") || value.matches("\\s+on")){
                if (! TwitterFinder.getTwitterChannels().containsKey(message.getChannel().getLongID())) {
                    new TwitterFinder(message.getGuild().getLongID(), message.getChannel().getLongID()).addToDatabase();
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "twitter.request.1")
                            .replace("{twitter.name}", Translator.getLabel(lg, "twitter.name")));
                }
                else
                    twitterFound.throwException(message, this, lg);
            }
            else if (value.matches("\\s+false") || value.matches("\\s+1") || value.matches("\\s+off")){
                if (TwitterFinder.getTwitterChannels().containsKey(message.getChannel().getLongID())) {
                    TwitterFinder.getTwitterChannels().get(message.getChannel().getLongID()).removeToDatabase();
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "twitter.request.2")
                            .replace("{twitter.name}", Translator.getLabel(lg, "twitter.name")));
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

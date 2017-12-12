package commands.config;

import commands.model.AbstractCommand;
import enums.Language;
import exceptions.*;
import finders.RSSFinder;
import data.User;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class RSSCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(RSSCommand.class);

    private DiscordException noEnoughRights;
    private DiscordException rssFound;
    private DiscordException rssNotFound;

    public RSSCommand(){
        super("rss","(\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)");
        setUsableInMP(false);
        noEnoughRights = new BasicDiscordException("exception.basic.no_enough_rights");
        rssFound = new AdvancedDiscordException("exception.advanced.rss_found", new String[]{"game.url"}, new Boolean[]{true});
        rssNotFound = new AdvancedDiscordException("exception.advanced.rss_not_found", new String[]{"game.url"}, new Boolean[]{true});
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Language lg = Translator.getLanguageFrom(message.getChannel());
            //On check si la personne a bien les droits pour exécuter cette commande
            if (User.getUser(message.getGuild(), message.getAuthor()).getRights() >= User.RIGHT_MODERATOR) {
                Matcher m = getMatcher(message);
                m.find();
                String value = m.group(1);

                if (value.matches("\\s+true") || value.matches("\\s+0") || value.matches("\\s+on")){

                    if (!RSSFinder.getRSSFinders().containsKey(message.getChannel().getStringID())) {
                        new RSSFinder(message.getGuild().getStringID(), message.getChannel().getStringID()).addToDatabase();
                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "rss.request.1"));
                    }
                    else
                        rssFound.throwException(message, this, lg);
                }
                else if (value.matches("\\s+false") || value.matches("\\s+1") || value.matches("\\s+off"))
                    if (RSSFinder.getRSSFinders().containsKey(message.getChannel().getStringID())){
                        RSSFinder.getRSSFinders().get(message.getChannel().getStringID()).removeToDatabase();
                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "rss.request.2"));
                    }
                    else
                        rssNotFound.throwException(message, this, lg);
                else
                    new BadUseCommandDiscordException().throwException(message, this, lg);
            } else
                noEnoughRights.throwException(message, this, lg);
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "rss.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " true` : " + Translator.getLabel(lg, "rss.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " false` : " + Translator.getLabel(lg, "rss.help.detailed.2") + "\n";
    }
}

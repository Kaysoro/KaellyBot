package commands;

import enums.Language;
import finders.RSSFinder;
import data.User;
import util.Message;
import exceptions.BadUseCommandDiscordException;
import exceptions.NotEnoughRightsDiscordException;
import exceptions.RSSFoundDiscordException;
import exceptions.RSSNotFoundDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class RSSCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(RSSCommand.class);

    public RSSCommand(){
        super("rss","(\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)");
        setUsableInMP(false);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            //On check si la personne a bien les droits pour exécuter cette commande
            if (User.getUser(message.getGuild(), message.getAuthor()).getRights() >= User.RIGHT_MODERATOR) {
                Matcher m = getMatcher(message);
                m.find();
                Language lg = Translator.getLanguageFrom(message.getChannel());
                String value = m.group(1);

                if (value.matches("\\s+true") || value.matches("\\s+0") || value.matches("\\s+on")){

                    if (!RSSFinder.getRSSFinders().containsKey(message.getChannel().getStringID())) {
                        new RSSFinder(message.getGuild().getStringID(), message.getChannel().getStringID()).addToDatabase();
                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "rss.request.1"));
                    }
                    else
                        new RSSFoundDiscordException().throwException(message, this);
                }
                else if (value.matches("\\s+false") || value.matches("\\s+1") || value.matches("\\s+off"))
                    if (RSSFinder.getRSSFinders().containsKey(message.getChannel().getStringID())){
                        RSSFinder.getRSSFinders().get(message.getChannel().getStringID()).removeToDatabase();
                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "rss.request.2"));
                    }
                    else
                        new RSSNotFoundDiscordException().throwException(message, this);
                else
                    new BadUseCommandDiscordException().throwException(message, this);
            } else
                new NotEnoughRightsDiscordException().throwException(message, this);
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

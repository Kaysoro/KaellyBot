package commands;

import data.RSSFinder;
import data.User;
import discord.Message;
import exceptions.BadUseCommandDiscordException;
import exceptions.NotEnoughRightsDiscordException;
import exceptions.RSSFoundDiscordException;
import exceptions.RSSNotFoundDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

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
            if (User.getUsers().get(message.getGuild().getStringID())
                    .get(message.getAuthor().getStringID()).getRights() >= User.RIGHT_MODERATOR) {
                Matcher m = getMatcher(message);
                m.find();
                String value = m.group(1);

                if (value.matches("\\s+true") || value.matches("\\s+0") || value.matches("\\s+on")){

                    if (!RSSFinder.getRSSFinders().containsKey(message.getChannel().getStringID())) {
                        new RSSFinder(message.getGuild().getStringID(), message.getChannel().getStringID()).addToDatabase();
                        Message.sendText(message.getChannel(), "Les news de dofus.com seront automatiquement postées ici.");
                    }
                    else
                        new RSSFoundDiscordException().throwException(message, this);
                }
                else if (value.matches("\\s+false") || value.matches("\\s+1") || value.matches("\\s+off"))
                    if (RSSFinder.getRSSFinders().containsKey(message.getChannel().getStringID())){
                        RSSFinder.getRSSFinders().get(message.getChannel().getStringID()).removeToDatabase();
                        Message.sendText(message.getChannel(), "Les news de dofus.com ne sont plus postées ici.");
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
    public String help(String prefixe) {
        return "**" + prefixe + name + "** gère le flux RSS Dofus dans un salon; "
        + "nécessite un niveau d'administration 2 (Modérateur) minimum.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + " true` : poste les news à partir du flux RSS de Dofus.com. Fonctionne aussi avec \"on\" et \"0\"."
                + "\n" + prefixe + "`"  + name + " false` : ne poste plus les flux RSS dans le salon. Fonctionne aussi avec \"off\" et \"1\".\n";
    }
}

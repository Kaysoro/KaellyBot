package commands;

import data.Constants;
import data.RSSFinder;
import data.User;
import discord.Message;
import exceptions.NotEnoughRightsException;
import exceptions.RSSFoundException;
import exceptions.RSSNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class RSSCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(RSSCommand.class);

    public RSSCommand(){
        super(Pattern.compile("rss"),
        Pattern.compile("^(" + Constants.prefixCommand + "rss)(\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            //On check si la personne a bien les droits pour exécuter cette commande
            if (User.getUsers().get(message.getGuild().getID())
                    .get(message.getAuthor().getID()).getRights() >= User.RIGHT_MODERATOR) {

                String value = m.group(2);

                if (value.matches("\\s+true") || value.matches("\\s+0") || value.matches("\\s+on")){
                    boolean found = false;

                    for(RSSFinder finder : RSSFinder.getRSSFinders())
                        if (finder.getChan().equals(message.getChannel().getID())){
                            found = true;
                            break;
                        }

                    if (!found) {
                        new RSSFinder(message.getChannel().getID()).addToDatabase();
                        Message.send(message.getChannel(), "Les news de dofus.com seront automatiquement postées ici.");
                    }
                    else
                        new RSSFoundException().throwException(message, this);
                }
                else {
                    boolean found = false;
                    for(RSSFinder finder : RSSFinder.getRSSFinders())
                        if (finder.getChan().equals(message.getChannel().getID())){
                            found = true;
                            finder.removeToDatabase();
                            Message.send(message.getChannel(), "Les news de dofus.com ne sont plus postées ici.");
                            break;
                        }

                    if (!found)
                        new RSSNotFoundException().throwException(message, this);
                }
            } else
                new NotEnoughRightsException().throwException(message, this);
        }
        return false;
    }

    @Override
    public boolean isUsableInMP() {
        return false;
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "rss** gère le flux RSS Dofus par channel; nécessite un niveau d'administration 2 (Modérateur) minimum.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "rss true` : poste les news à partir du flux RSS de Dofus.com. Fonctionne aussi avec \"on\" et \"0\"."
                + "\n`" + Constants.prefixCommand + "rss false` : ne poste plus les flux RSS sur le channel. Fonctionne aussi avec \"off\" et \"1\".\n";
    }
}

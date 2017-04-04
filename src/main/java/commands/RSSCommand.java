package commands;

import data.Constants;
import data.RSSFinder;
import data.User;
import discord.Message;
import exceptions.InDeveloppmentException;
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
        Pattern.compile("^(" + Constants.prefixCommand + "rss)(\\s+-rm)?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            //On check si la personne a bien les droits pour exécuter cette commande
            if (User.getUsers().get(message.getGuild().getID())
                    .get(message.getAuthor().getID()).getRights() >= User.RIGHT_MODERATOR) {

                if (m.group(2) == null) {
                    boolean found = false;

                    for(RSSFinder finder : RSSFinder.getRSSFinders())
                        if (finder.getChan() == message.getChannel()){
                            found = true;
                            break;
                        }

                    if (!found) {
                        new RSSFinder(message.getChannel()).addToDatabase();
                        Message.send(message.getChannel(), "Les news de dofus.com seront automatiquement postées ici.");
                    }
                    else
                        new RSSFoundException().throwException(message, this);

                }
                else {
                    boolean found = false;
                    for(RSSFinder finder : RSSFinder.getRSSFinders())
                        if (finder.getChan() == message.getChannel()){
                            found = true;
                            finder.removeToDatabase();
                            Message.send(message.getChannel(), "Les news de dofus.com ne sont plus postées ici.");
                            break;
                        }

                    if (!found)
                        new RSSNotFoundException().throwException(message, this);
                }

                return true;
            } else {
                new NotEnoughRightsException().throwException(message, this);
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isUsableInMP() {
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "rss** gère le flux RSS Dofus par channel; nécessite un niveau d'administration 2 (Modérateur) minimum.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "rss` : écoute le flux RSS de Dofus.com dans le channel et poste dedans"
                + " s'il y a des news."
                + "\n`" + Constants.prefixCommand + "rss -rm` : supprime le flux RSS rattaché au channel.\n";
    }
}

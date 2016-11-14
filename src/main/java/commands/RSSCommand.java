package commands;

import data.Constants;
import data.User;
import exceptions.InDeveloppmentException;
import exceptions.NotEnoughRightsException;
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
        Pattern.compile("^(" + Constants.prefixCommand + "rss)\\s+(-add)\\s+(.*)$"));
        //TODO name ?
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            //On check si la personne a bien les droits pour exécuter cette commande
            if (User.getUsers().get(message.getGuild().getID())
                    .get(message.getAuthor().getID()).getRights() >= User.RIGHT_MODERATOR) {

                //TODO Do command

                new InDeveloppmentException().throwException(message, this);
                return true;
            } else {
                new NotEnoughRightsException().throwException(message, this);
                return false;
            }
        }
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "rss** gère les flux RSS par channel; nécessite un niveau d'administration 2 (Modérateur) minimum.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "rss` : montre tous les flux RSS rattachés au channel."
                + "\n`" + Constants.prefixCommand + "rss -add `*`http://rss.xml`* : écoute le flux RSS dans le channel et poste dedans"
                + " s'il y a des news."
                + "\n`" + Constants.prefixCommand + "rss -rm `*`rss`* : supprime le *ième* flux RSS rattaché au channel.\n";
    }
}

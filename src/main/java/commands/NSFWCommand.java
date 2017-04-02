package commands;

import data.Constants;
import data.NSFWAuthorization;
import data.User;
import discord.Message;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class NSFWCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(NSFWCommand.class);

    public NSFWCommand(){
        super(Pattern.compile("nsfw"),
        Pattern.compile("^(" + Constants.prefixCommand + "nsfw)(\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)?$"));
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

                    for(NSFWAuthorization nsfw : NSFWAuthorization.getNSFWChannels())
                        if (nsfw.getChannelId().equals(message.getChannel().getID())){
                            found = true;
                            break;
                        }

                    if (!found) {
                        new NSFWAuthorization(message.getChannel().getID()).addToDatabase();
                        Message.send(message.getChannel(), "Les commandes NSFW peuvent être utilisées ici.");
                    }
                    else
                        new NSFWFoundException().throwException(message, this);
                }
                else {
                    boolean found = false;
                    for(NSFWAuthorization nsfw : NSFWAuthorization.getNSFWChannels())
                        if (nsfw.getChannelId().equals(message.getChannel().getID())){
                            found = true;
                            nsfw.removeToDatabase();
                            Message.send(message.getChannel(), "Les commandes NSFW ne sont plus autorisées ici.");
                            break;
                        }

                    if (!found)
                        new NSFWNotFoundException().throwException(message, this);
                }
            }
        }
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "nsfw** gère les commandes NSFW sur un channel; nécessite un niveau d'administration 2 (Modérateur) minimum.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "nsfw true` : autorise les commandes NSFW. Fonctionne aussi avec \"on\" et \"0\"."
                + "\n`" + Constants.prefixCommand + "nsfw false` : interdit les commandes NSFW. Fonctionne aussi avec \"off\" et \"1\".\n";
    }
}

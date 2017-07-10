package commands;

import data.TwitterFinder;
import data.User;
import discord.Message;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class TwitterCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(TwitterCommand.class);

    public TwitterCommand(){
        super("twitter", "(\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)");
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
                    if (! TwitterFinder.getTwitterChannels().containsKey(message.getChannel().getLongID())) {
                        new TwitterFinder(message.getGuild().getLongID(), message.getChannel().getLongID()).addToDatabase();
                        Message.sendText(message.getChannel(), "Les tweets de @Dofusfr seront postés ici.");
                    }
                    else
                        new TwitterFoundDiscordException().throwException(message, this);
                }
                else if (value.matches("\\s+false") || value.matches("\\s+1") || value.matches("\\s+off")){
                    if (TwitterFinder.getTwitterChannels().containsKey(message.getChannel().getLongID())) {
                        TwitterFinder.getTwitterChannels().get(message.getChannel().getLongID()).removeToDatabase();
                        Message.sendText(message.getChannel(), "Les tweets de @Dofusfr ne seront plus postés ici.");
                    }
                    else
                        new TwitterNotFoundDiscordException().throwException(message, this);
                }
                else
                    new BadUseCommandDiscordException().throwException(message, this);
            }
            else
                new NotEnoughRightsDiscordException().throwException(message, this);
        }
        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** poste les tweets de Dofusfr dans un salon; "
            + "nécessite un niveau d'administration 2 (Modérateur) minimum.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + " true` : poste les tweets de Dofusfr. Fonctionne aussi avec \"on\" et \"0\"."
                + "\n" + prefixe + "`"  + name + " false` : ne poste plus les tweets dans le salon. Fonctionne aussi avec \"off\" et \"1\".\n";
    }
}

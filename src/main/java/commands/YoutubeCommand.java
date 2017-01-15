package commands;

import data.Constants;
import data.User;
import discord.Message;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class YoutubeCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(YoutubeCommand.class);

    public YoutubeCommand(){
        super(Pattern.compile("ytb"),
        Pattern.compile("^(" + Constants.prefixCommand + "ytb)(\\s+-[join|play|pause|skip|shuffle|leave])?(\\s+.+)?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            new InDeveloppmentException().throwException(message, this);
        }

        return true;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "ytb** permet d'écouter de la musique provenant de Youtube dans un canal vocal.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "ytb -join` : rejoint le canal vocal dans lequel vous êtes."
                + "\n`" + Constants.prefixCommand + "ytb `*`http://youtube.com/yourVideo`* : ajoute la vidéo youtube à la liste de lecture."
                + "\n`" + Constants.prefixCommand + "ytb -play` : joue la musique actuelle."
                + "\n`" + Constants.prefixCommand + "ytb -pause` : arrête la musique actuelle."
                + "\n`" + Constants.prefixCommand + "ytb -skip` : passe à la musique suivante."
                + "\n`" + Constants.prefixCommand + "ytb -shuffle` : mélange la liste de lecture."
                + "\n`" + Constants.prefixCommand + "ytb -leave` : quitte le canal vocal.\n";
    }
}

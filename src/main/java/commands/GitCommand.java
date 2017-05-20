package commands;

import data.Constants;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class GitCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(GitCommand.class);

    public GitCommand(){
        super(Pattern.compile("git"),
        Pattern.compile("^(" + Constants.prefixCommand + "git)$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){

            Message.sendText(message.getChannel(), "Le code source de " + Constants.name + " se trouve sur " + Constants.git
                    + " ! N'hésitez pas à réagir à ce propos :)");
            return true;
        }

        return false;
    }

    @Override
    public boolean isUsableInMP() {
        return true;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "git** donne le lien du code source de " + Constants.name + ".";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "job` : donne le lien du code source de " + Constants.name + ".\n";
    }
}

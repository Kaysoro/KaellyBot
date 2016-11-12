package commands;

import data.ClientConfig;
import data.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

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
            RequestBuffer.request(() -> {
                try {
                    new MessageBuilder(ClientConfig.CLIENT())
                            .withChannel(message.getChannel())
                            .withContent("Le code source de " + Constants.name + " se trouve sur " + Constants.git
                                    + " ! N'hésitez pas à réagir à ce propos :)")
                            .build();
                } catch (DiscordException e){
                    LOG.error(e.getErrorMessage());
                } catch(MissingPermissionsException e){
                    LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                }
                return null;
            });
            return true;
        }

        return false;
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

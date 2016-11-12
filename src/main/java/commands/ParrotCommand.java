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
public class ParrotCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(ParrotCommand.class);

    public ParrotCommand(){
        super(Pattern.compile("parrot"),
        Pattern.compile("^(" + Constants.prefixCommand + "parrot)\\s+(.*)$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            RequestBuffer.request(() -> {
                try {
                    new MessageBuilder(ClientConfig.CLIENT())
                            .withChannel(message.getChannel())
                            .withContent(m.group(2))
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
        return "**" + Constants.prefixCommand + "parrot** renvoit le message envoyé en argument, tel un perroquet.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "parrot `*`message`* : renvoit le message spécifié.\n";
    }
}

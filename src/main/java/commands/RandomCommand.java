package commands;

import data.Constants;
import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class RandomCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(RandomCommand.class);

    public RandomCommand(){
        super(Pattern.compile("rdm"),
              Pattern.compile("^(" + Constants.prefixCommand + "rdm)(\\s+.+)?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            if (m.group(2) == null) {
                boolean value = new Random().nextBoolean();
                Message.send(message.getChannel(), value ? "Vrai" : "Faux" + " !");
            }
            else if (m.group(2).matches("\\s+\\d+")){
                int limit = Integer.parseInt(m.group(2).trim());
                Message.send(message.getChannel(), new Random().nextInt(limit) + " !");
            }
            else {
                String value = m.group(2).trim();
                String[] values = value.split("\\s+");
                Message.send(message.getChannel(), values[new Random().nextInt(values.length)] + " !");
            }
        }
        return false;
    }

    @Override
    public boolean isUsableInMP() {
        return true;
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "rdm** tire au hasard une valeur spécifiée ou non.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "rdm` : tire une valeur entre Vrai et Faux."
                + "\n`" + Constants.prefixCommand + "rdm `*`nombre`* : tire une valeur entre 0 et *nombre*."
                + "\n`" + Constants.prefixCommand + "rdm `*`valeur1 valeur2 ...`* : tire une valeur parmi celles spécifiées en paramètre.\n";
    }
}

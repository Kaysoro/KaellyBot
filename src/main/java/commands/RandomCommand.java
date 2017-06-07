package commands;

import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Random;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class RandomCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(RandomCommand.class);

    public RandomCommand(){
        super("rdm","(\\s+.+)?");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Matcher m = getMatcher(message);
            m.find();
            if (m.group(1) == null) {
                boolean value = new Random().nextBoolean();
                Message.sendText(message.getChannel(), value ? "Vrai" : "Faux" + " !");
            }
            else if (m.group(1).matches("\\s+\\d+")){
                int limit = Integer.parseInt(m.group(1).trim());
                Message.sendText(message.getChannel(), new Random().nextInt(limit) + " !");
            }
            else {
                String value = m.group(1).trim();
                String[] values = value.split("\\s+");
                Message.sendText(message.getChannel(), values[new Random().nextInt(values.length)] + " !");
            }
        }
        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** tire au hasard une valeur spécifiée ou non.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n`" + prefixe + name + "` : tire une valeur entre Vrai et Faux."
                + "\n`" + prefixe + name + " `*`nombre`* : tire une valeur entre 0 et *nombre*."
                + "\n`" + prefixe + name + " `*`valeur1 valeur2 ...`* : tire une valeur parmi celles spécifiées en paramètre.\n";
    }
}

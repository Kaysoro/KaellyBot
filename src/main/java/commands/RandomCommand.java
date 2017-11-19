package commands;

import enums.Language;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

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
            Language lg = Translator.getLanguageFrom(message.getChannel());
            Matcher m = getMatcher(message);
            m.find();
            if (m.group(1) == null) {
                boolean value = new Random().nextBoolean();
                Message.sendText(message.getChannel(), value ? Translator.getLabel(lg, "random.request.1") :
                        Translator.getLabel(lg, "random.request.2") + " !");
            }
            else if (m.group(1).matches("\\s+-?\\d+")){
                try {
                    int limit = Integer.parseInt(m.group(1).trim());
                    if (limit > 0)
                        Message.sendText(message.getChannel(),new Random().nextInt(limit) + " !");
                    else
                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "random.request.3"));
                } catch(NumberFormatException e){
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "random.request.4")
                            + " " + Integer.MAX_VALUE + ").");
                }
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
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "random.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + "` : " + Translator.getLabel(lg, "random.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " `*`nombre`* : " + Translator.getLabel(lg, "random.help.detailed.2")
                + "\n" + prefixe + "`"  + name + " `*`valeur1 valeur2 ...`* : "
                    + Translator.getLabel(lg, "random.help.detailed.3") + "\n";
    }
}

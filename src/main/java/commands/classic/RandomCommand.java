package commands.classic;

import commands.model.AbstractCommand;
import enums.Dungeon;
import enums.Language;
import util.Message;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by steve on 14/07/2016.
 */
public class RandomCommand extends AbstractCommand {

    private final static int DEFAULT_TOLERANCE = 0;
    private final static int MIN_LEVEL = 10;
    private final static int DEFAULT_LEVEL = 190;
    private final static int MAX_TOLERANCE = 200;

    public RandomCommand(){
        super("rdm","(.*)");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        Random r = new Random();
        Matcher tmp;

        if (m.group(1) == null || m.group(1).trim().isEmpty()) {
            boolean value = r.nextBoolean();
            Message.sendText(message.getChannel(), value ? Translator.getLabel(lg, "random.request.1") :
                    Translator.getLabel(lg, "random.request.2") + " !");
        }
        else if (m.group(1).matches("\\s+-?\\d+")){
            try {
                int limit = Integer.parseInt(m.group(1).trim());
                if (limit > 0)
                    Message.sendText(message.getChannel(),r.nextInt(limit) + " !");
                else
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "random.request.3"));
            } catch(NumberFormatException e){
                Message.sendText(message.getChannel(), Translator.getLabel(lg, "random.request.4")
                        + " " + Integer.MAX_VALUE + ").");
            }
        }
        else if ((tmp = Pattern.compile("\\s+-dj(\\s+\\d{1,3})?(\\s+\\d{1,3})?").matcher(m.group(1))).matches()){
            int level = DEFAULT_LEVEL;
            int tolerance = DEFAULT_TOLERANCE;

            if (tmp.group(1) != null) {
                level = Integer.parseInt(tmp.group(1).trim());
                level = level - level % 10;
                if (level > DEFAULT_LEVEL) level = DEFAULT_LEVEL;
                if (level < MIN_LEVEL) level = MIN_LEVEL;
            }
            if (tmp.group(2) != null)
                tolerance = Integer.parseInt(tmp.group(2).trim());
            else if (tmp.group(1) == null) tolerance = MAX_TOLERANCE;

            final int finalLevel = level;
            final int finalTolerance = tolerance;

            List<Dungeon> djs = Arrays.stream(Dungeon.values())
                    .filter(dj -> dj.filterByLevel(finalLevel, finalTolerance))
                    .collect(Collectors.toList());

            Dungeon selected = djs.get(r.nextInt(djs.size()));

            if (selected.isEvent() && djs.size() > 1) {
                Dungeon selected2 = selected;
                while(selected == selected2 || selected2.isEvent())
                    selected2 = djs.get(r.nextInt(djs.size()));

                Message.sendText(message.getChannel(), Translator.getLabel(lg, "random.request.6")
                        .replace("{dj}", selected.getLabel(lg))
                        .replace("{dj2}", selected2.getLabel(lg))
                        .replace("{size}", String.valueOf(djs.size())));
            }
            else
                Message.sendText(message.getChannel(), Translator.getLabel(lg, "random.request.5")
                        .replace("{dj}", selected.getLabel(lg))
                        .replace("{size}", String.valueOf(djs.size())));
        }
        else if (m.group(1).matches("\\s+-dj(\\s+\\d+)?(\\s+\\d+)?"))
            badUse.throwException(message, this, lg);
        else {
            String value = m.group(1).trim();
            String[] values = value.split("\\s+");
            Message.sendText(message.getChannel(), values[r.nextInt(values.length)] + " !");
        }
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "random.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + "` : " + Translator.getLabel(lg, "random.help.detailed.1")
                + "\n`" + prefixe + name + " `*`number`* : " + Translator.getLabel(lg, "random.help.detailed.2")
                + "\n`" + prefixe + name + " `*`value1 value2 ...`* : "
                    + Translator.getLabel(lg, "random.help.detailed.3")
                + "\n`" + prefixe + name + " -dj` : " + Translator.getLabel(lg, "random.help.detailed.4")
                + "\n`" + prefixe + name + " -dj `*`level tolerance`* : "
                    + Translator.getLabel(lg, "random.help.detailed.5") + "\n";
    }
}

package commands;

import discord.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class MapCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(MapCommand.class);
    private final static Map<String, String> urls = new HashMap<>();
    static {
        urls.put("I", "https://image.noelshack.com/fichiers/2017/14/1491433376-i.png");
        urls.put("II", "https://image.noelshack.com/fichiers/2017/14/1491433376-ii.png");
        urls.put("III", "https://image.noelshack.com/fichiers/2017/14/1491433375-iii.png");
        urls.put("IV", "https://image.noelshack.com/fichiers/2017/14/1491433376-iv.png");
        urls.put("V", "https://image.noelshack.com/fichiers/2017/14/1491433376-v.png");
        urls.put("VI", "https://image.noelshack.com/fichiers/2017/14/1491433377-vi.png");
        urls.put("VII", "https://image.noelshack.com/fichiers/2017/14/1491433378-vii.png");
        urls.put("VIII", "https://image.noelshack.com/fichiers/2017/14/1491433378-viii.png");
        urls.put("IX", "https://image.noelshack.com/fichiers/2017/14/1491433376-ix.png");
        urls.put("X", "https://image.noelshack.com/fichiers/2017/14/1491433379-x.png");
        urls.put("XI", "https://image.noelshack.com/fichiers/2017/14/1491433381-xi.png");
        urls.put("XII", "https://image.noelshack.com/fichiers/2017/14/1491433381-xii.png");
        urls.put("XIII", "https://image.noelshack.com/fichiers/2017/33/1/1502741729-xiii.png");
        urls.put("XIV", "https://image.noelshack.com/fichiers/2017/33/1/1502741732-xiv.png");
        urls.put("XV", "https://image.noelshack.com/fichiers/2017/33/1/1502741732-xv.png");
        urls.put("XVI", "https://image.noelshack.com/fichiers/2017/33/1/1502741731-xvi.png");
        urls.put("XVII", "https://image.noelshack.com/fichiers/2017/33/1/1502741730-xvii.png");
    }

    public MapCommand(){
        super("map",
        "((\\s+I|\\s+II|\\s+III|\\s+IV|\\s+V|\\s+VI|\\s+VII|\\s+VIII|\\s+IX|\\s+X"
                + "|\\s+XI|\\s+XII|\\s+XIII|\\s+XIV|\\s+XV|\\s+XVI|\\s+XVII"
                + "|\\s+i|\\s+ii|\\s+iii|\\s+iv|\\s+v|\\s+vi|\\s+vii|\\s+viii|\\s+ix|\\s+x"
                + "|\\s+xi|\\s+xii|\\s+xiii|\\s+xiv|\\s+xv|\\s+xvi|\\s+xvii"
                + "|\\s+1|\\s+2|\\s+3|\\s+4|\\s+5|\\s+6|\\s+7|\\s+8|\\s+9|\\s+10"
                + "|\\s+11|\\s+12|\\s+13|\\s+14|\\s+15|\\s+16|\\s+17"
                + ")+)?");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            String[] maps;
            Matcher m = getMatcher(message);
            m.find();
            if (m.group(1) == null)
                maps = new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
                        "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII"};
            else {
                String text = m.group(1).trim().toUpperCase()
                        .replaceAll("17", "XVII")
                        .replaceAll("16", "XVI").replaceAll("15", "XV")
                        .replaceAll("14", "XIV").replaceAll("13", "XIII")
                        .replaceAll("12", "XII").replaceAll("11", "XI")
                        .replaceAll("10", "X").replaceAll("9", "IX")
                        .replaceAll("8", "VIII").replaceAll("7", "VII")
                        .replaceAll("6", "VI").replaceAll("5", "V")
                        .replaceAll("4", "IV").replaceAll("3", "III")
                        .replaceAll("2", "II").replaceAll("1", "I") ;

                maps = text.split("\\s+");
            }

            int random = new Random().nextInt(maps.length);

            Message.sendText(message.getChannel(), "Le combat aura lieu sur la carte "
                    + maps[random] + " !"
                    + "\n" + urls.get(maps[random]));
        }
        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** tire au hasard une carte du Goultarminator ou bien parmi celles"
                + " spécifiées en paramètre.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + "` : tire une carte du Goultarminator entre I et XVII compris."
                + "\n" + prefixe + "`"  + name + " `*`map1 map2 ...`* : tire une carte parmi celles spécifiées en paramètre. Nombres romains ou numériques uniquement.\n";
    }
}

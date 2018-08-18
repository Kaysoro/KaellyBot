package commands.classic;

import commands.model.AbstractCommand;
import data.Constants;
import enums.Language;
import sx.blah.discord.util.EmbedBuilder;
import util.Message;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.*;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class MapCommand extends AbstractCommand {

    private static final int[] decimal = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
    private static final String[] letters = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};


    public MapCommand(){
        super("map",
        "(\\s+-ban)?((\\s+\\w+)+)?");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        List<String> classicMaps = new ArrayList<>();
        for(int i = 1; i < 18; i++)
            classicMaps.add(String.valueOf(i));
        List<String> maps = new ArrayList<>();

        if (m.group(1) == null && m.group(2) == null)
            maps.addAll(classicMaps);
        else if (m.group(2) != null){
            String[] text = m.group(2).trim().toUpperCase().split("\\s+");
            for(String value : text){
                value = getNumberValue(value);
                if (value != null)
                    maps.add(value);
            }
        }
        else {
            badUse.throwException(message, this, lg);
            return;
        }

        if (m.group(1) == null && maps.isEmpty()){
            badUse.throwException(message, this, lg);
            return;
        }
        else if (m.group(1) != null){
            classicMaps.removeAll(maps);
            maps = classicMaps;
        }

        String number = maps.get(new Random().nextInt(maps.size()));
        String url = Constants.turnamentMapImg.replace("{number}", number);

        String[] punchlines = Translator.getLabel(lg, "map.punchline").split(";");
        String punchline = punchlines[new Random().nextInt(punchlines.length)];
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(Translator.getLabel(lg, "map.embed.title") + " " + numberToRoman(number));
        builder.withDescription(punchline);
        builder.withImage(url);
        builder.withColor(new Random().nextInt(16777216));
        builder.withImage(url);

        Message.sendEmbed(message.getChannel(), builder.build());
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "map.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + "` : " + Translator.getLabel(lg, "map.help.detailed.1")
                + "\n`" + prefixe + name + " `*`map1 map2 ...`* : " + Translator.getLabel(lg, "map.help.detailed.2")
                + "\n`" + prefixe + name + " -ban `*`map1 map2 ...`* : " + Translator.getLabel(lg, "map.help.detailed.3") + "\n";
    }

    private static String numberToRoman(String num) {
        try {
            int number = Integer.parseInt(num);
            String roman = "";

            if (number < 1 || number > 3999)
                return null;

            while (number > 0) {
                int maxFound = 0;
                for (int i = 0; i < decimal.length; i++)
                    if (number >= decimal[i])
                        maxFound = i;

                roman += letters[maxFound];
                number -= decimal[maxFound];
            }

            return roman;
        } catch(NumberFormatException e){
            return null;
        }
    }

    private static String getNumberValue(String value)
    {
        value = value.trim().toUpperCase();
        if (value.equals("I") || value.equals("1"))
            return "1";
        else if (value.equals("I") || value.equals("1"))
            return "1";
        else if (value.equals("II") || value.equals("2"))
            return "2";
        else if (value.equals("III") || value.equals("3"))
            return "3";
        else if (value.equals("IV") || value.equals("4"))
            return "4";
        else if (value.equals("V") || value.equals("5"))
            return "5";
        else if (value.equals("VI") || value.equals("6"))
            return "6";
        else if (value.equals("VII") || value.equals("7"))
            return "7";
        else if (value.equals("VIII") || value.equals("8"))
            return "8";
        else if (value.equals("IX") || value.equals("9"))
            return "9";
        else if (value.equals("X") || value.equals("10"))
            return "10";
        else if (value.equals("XI") || value.equals("11"))
            return "11";
        else if (value.equals("XII") || value.equals("12"))
            return "12";
        else if (value.equals("XIII") || value.equals("13"))
            return "13";
        else if (value.equals("XIV") || value.equals("14"))
            return "14";
        else if (value.equals("XV") || value.equals("15"))
            return "15";
        else if (value.equals("XVI") || value.equals("16"))
            return "16";
        else if (value.equals("XVII") || value.equals("17"))
            return "17";
        return null;
    }
}

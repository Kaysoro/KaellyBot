package util;

import enums.Language;
import enums.Statistique;

public class EmojiManager {

    public static String getEmojiForStat(Language lg, String text){
        text = text
                .replaceAll(Translator.getLabel(lg, "emoji.from") + "\\s+", "")
                .replaceAll("-?\\+?\\d+\\s+" + Translator.getLabel(lg, "emoji.to") + "\\s+-?\\+?\\d+", "")
                .replaceAll("-?\\+?\\d+", "")
                .replaceAll(":", "")
                .replaceAll("\\.", "")
                .trim();

        for(Statistique stat : Statistique.values())
            for(String proposal : stat.getNames())
                if (proposal.equals(text))
                    return stat.getEmoji();
        return "";
    }
}

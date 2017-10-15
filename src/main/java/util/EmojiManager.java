package util;

import enums.Statistique;

public class EmojiManager {

    public static String getEmojiForStat(String text){
        text = text.replaceAll("-?\\d+", "").replaceAll("De ", "")
                .replaceAll(" à ", "").replaceAll(" :", "").trim();

        for(Statistique stat : Statistique.values())
            for(String proposal : stat.getNames())
                if (proposal.equals(text))
                    return stat.getEmoji();
        return "";
    }
}

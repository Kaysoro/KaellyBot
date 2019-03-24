package util;

import enums.Language;
import enums.Statistique;
import sx.blah.discord.handle.obj.StatusType;

public class EmojiManager {

    public static String getEmojiForStat(Language lg, String text){
        text = text
                .replaceAll(Translator.getLabel(lg, "emoji.from") + "\\s+", "")
                .replaceAll("-?\\+?\\d+\\s+" + Translator.getLabel(lg, "emoji.to") + "\\s+-?\\+?\\d+", "")
                .replaceAll("-?\\+?\\d+", "")
                .replaceAll(Translator.getLabel(lg, "emoji.to") + " %", "")
                .replaceAll(":", "")
                .replaceAll("\\.", "")
                .replaceAll("\\s{2}", " ")
                .trim();

        for(Statistique stat : Statistique.values()) {
            String[] names = stat.getNames(lg);
            for (String proposal : names)
                if (proposal.equals(text))
                    return stat.getEmoji();
        }
        return "";
    }

    public static String getEmojiForLadder(String place){
        switch (place) {
            case "1": return ":crown:";
            case "2": return ":second_place:";
            case "3": return ":third_place:";
            default : return "**" + place + "**";
        }
    }

    public static String getEmojiForPresence(StatusType presence){
        switch (presence){
            case ONLINE : return "<:here:435696207776710656>";
            case DND    : return "<:busy:435696207256354817> ";
            case IDLE   : return "<:away:435696207302492173> ";
            default     : return "<:invisible:435697900601868288>";
        }
    }
}

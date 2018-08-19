package commands.classic;

import commands.model.DofusEncyclopediaRequestCommand;
import data.Embedded;
import data.Item;
import enums.Language;
import enums.SuperTypeEquipment;
import enums.TypeEquipment;
import exceptions.*;
import util.*;
import sx.blah.discord.handle.obj.IMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class ItemCommand extends DofusEncyclopediaRequestCommand {

    private final static String forName = "text=";
    private final static String forLevelMin = "object_level_min=";
    private final static String levelMin = "1";
    private final static String forLevelMax = "object_level_max=";
    private final static String levelMax = "200";
    private final static String and = "EFFECTMAIN_and_or=AND";
    private final static String size = "size=";

    private DiscordException tooMuchItems;
    private DiscordException notFoundItem;

    public ItemCommand(){
        super("item", "\\s+(-more)?(.*)");
        tooMuchItems = new TooMuchDiscordException("item");
        notFoundItem = new NotFoundDiscordException("item");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        if (isChannelHasExternalEmojisPermission(message)) {
            String normalName = Normalizer.normalize(m.group(2).trim(), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            BestMatcher matcher = new BestMatcher(normalName);

            try {
                for (TypeEquipment equip : TypeEquipment.values()) {
                    String[] names = equip.getNames(lg);
                    gatherData(message, matcher, names, normalName, equip, notFoundItem);
                }

                if (matcher.isEmpty())
                    for (SuperTypeEquipment type : SuperTypeEquipment.values())
                        matcher.evaluateAll(getListRequestableFrom(
                                getSearchURL(type.getUrl(lg), normalName, null, lg), message, notFoundItem));

                if (matcher.isUnique()) { // We have found it !
                    Embedded item = Item.getItem(lg, Translator.getLabel(lg, "game.url")
                            + matcher.getBest().getUrl());
                    if (m.group(1) != null)
                        Message.sendEmbed(message.getChannel(), item.getMoreEmbedObject(lg));
                    else
                        Message.sendEmbed(message.getChannel(), item.getEmbedObject(lg));
                } else if (!matcher.isEmpty()) // Too much items
                    tooMuchItems.throwException(message, this, lg, matcher.getBests());
                else // empty
                    notFoundItem.throwException(message, this, lg);
            } catch (IOException e) {
                ExceptionManager.manageIOException(e, message, this, lg, notFoundItem);
            }
        }
        else
            BasicDiscordException.NO_EXTERNAL_EMOJI_PERMISSION.throwException(message, this, lg);
    }

    protected String getSearchURL(String SuperTypeURL, String text, String typeArg, Language lg) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(Translator.getLabel(lg, "game.url")).append(SuperTypeURL)
                .append("?").append(forName.toLowerCase()).append(URLEncoder.encode(text, "UTF-8"))
                .append("&").append(forName.toUpperCase()).append(URLEncoder.encode(text, "UTF-8"))
                .append("&").append(and).append("&").append(forLevelMin).append(levelMin)
                .append("&").append(forLevelMax).append(levelMax).append("&").append(and)
                .append("&").append(size).append(50);

        if (typeArg != null && ! typeArg.isEmpty())
            url.append(typeArg);

        return url.toString();
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "item.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`item`* : " + Translator.getLabel(lg, "item.help.detailed.1")
                + "\n`" + prefixe + name + " -more `*`item`* : " + Translator.getLabel(lg, "item.help.detailed.2") + "\n";
    }
}

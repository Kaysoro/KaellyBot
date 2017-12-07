package commands;

import data.Embedded;
import data.Item;
import enums.Language;
import enums.SuperTypeEquipment;
import enums.TypeEquipment;
import exceptions.*;
import sx.blah.discord.handle.obj.Permissions;
import util.*;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class ItemCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(ItemCommand.class);
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
        tooMuchItems = new TooMuchDiscordException("exception.toomuch.items", "exception.toomuch.items_found");
        notFoundItem = new NotFoundDiscordException("exception.notfound.item", "exception.notfound.item_found");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){
            Matcher m = getMatcher(message);
            Language lg = Translator.getLanguageFrom(message.getChannel());
            m.find();
            if (message.getChannel().getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.USE_EXTERNAL_EMOJIS)
                    && ClientConfig.DISCORD().getOurUser().getPermissionsForGuild(message.getGuild())
                    .contains(Permissions.USE_EXTERNAL_EMOJIS)) {
                String normalName = Normalizer.normalize(m.group(2).trim(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
                String editedName = removeUselessWords(normalName);
                BestMatcher matcher = new BestMatcher(normalName);

                try {
                    for (TypeEquipment equip : TypeEquipment.values()) {
                        String[] names = equip.getNames(lg);
                        for (String name : names) {
                            String potentialName = Normalizer.normalize(name, Normalizer.Form.NFD)
                                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
                            if (normalName.equals(potentialName)) {
                                matcher.evaluateAll(getListItemFrom(getSearchURL(equip.getType().getUrl(lg),
                                        potentialName, equip.getTypeID(), lg), message));
                                break;
                            } else if (normalName.contains(potentialName)) {
                                matcher.evaluateAll(getListItemFrom(getSearchURL(equip.getType().getUrl(lg),
                                        editedName.replace(potentialName, "").trim(), equip.getTypeID(), lg), message));
                                break;
                            }
                        }
                    }

                    if (matcher.isEmpty())
                        for (SuperTypeEquipment type : SuperTypeEquipment.values())
                            matcher.evaluateAll(getListItemFrom(getSearchURL(type.getUrl(lg), normalName, null, lg), message));

                    if (matcher.isUnique()) { // We have found it !
                        Embedded item = Item.getItem(lg, Translator.getLabel(lg, "game.url")
                                + matcher.getBest().getRight());
                        if (m.group(1) != null)
                            Message.sendEmbed(message.getChannel(), item.getMoreEmbedObject(lg));
                        else
                            Message.sendEmbed(message.getChannel(), item.getEmbedObject(lg));
                    } else if (!matcher.isEmpty()) { // Too much items
                        List<String> names = new ArrayList<>();
                        for(Pair<String, String> item : matcher.getBests())
                            names.add(item.getLeft());
                        tooMuchItems.throwException(message, this, lg, names);
                    }
                    else // empty
                        notFoundItem.throwException(message, this, lg);
                } catch (IOException e) {
                    ExceptionManager.manageIOException(e, message, this, lg, notFoundItem);
                }

                return true;
            }
            else
                new NoExternalEmojiPermissionDiscordException().throwException(message, this, lg);
        }


        return false;
    }

    private String getSearchURL(String SuperTypeURL, String text, String typeArg, Language lg) throws UnsupportedEncodingException {
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

    private List<Pair<String, String>> getListItemFrom(String url, IMessage message){
        List<Pair<String, String>> result = new ArrayList<>();
        Language lg = Translator.getLanguageFrom(message.getChannel());
        try {
            Document doc = JSoupManager.getDocument(url);
            Elements elems = doc.getElementsByClass("ak-bg-odd");
            elems.addAll(doc.getElementsByClass("ak-bg-even"));

            for (Element element : elems)
                result.add(Pair.of(element.child(1).text(),
                        element.child(1).select("a").attr("href")));

        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, lg, notFoundItem);
            return new ArrayList<>();
        }  catch (Exception e) {
            ExceptionManager.manageException(e, message, this, lg);
            return new ArrayList<>();
        }

        return result;
    }

    private String removeUselessWords(String search){
        return search.replaceAll("\\s+\\w{1,3}\\s+", " ");
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "item.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " `*`item`* : " + Translator.getLabel(lg, "item.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " -more `*`item`* : " + Translator.getLabel(lg, "item.help.detailed.2") + "\n";
    }
}

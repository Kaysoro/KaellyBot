package commands;

import collections.BestMatcher;
import data.*;
import discord.Message;
import exceptions.ExceptionManager;
import exceptions.ItemNotFoundDiscordException;
import exceptions.TooMuchItemsDiscordException;
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

    public ItemCommand(){
        super("item", "\\s+(-more)?(.*)");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){
            Matcher m = getMatcher(message);
            m.find();
            String normalName = Normalizer.normalize(m.group(2).trim(), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            String editedName = removeUselessWords(normalName);
            BestMatcher matcher = new BestMatcher(normalName);

            try {
                for (int i = 0; i < TypeEquipment.values().length ; i++) {
                    TypeEquipment equip = TypeEquipment.values()[i];
                    for(int j = 0; j < equip.getNames().length; j++){
                        String potentialName = Normalizer.normalize(equip.getNames()[j], Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
                        if (normalName.equals(potentialName)){
                            matcher.evaluateAll(getListItemFrom(getSearchURL(equip.getType().getUrl(),
                                    potentialName, equip.getTypeID()), message));
                            break;
                        }
                        else if (normalName.contains(potentialName)) {
                            matcher.evaluateAll(getListItemFrom(getSearchURL(equip.getType().getUrl(),
                                    editedName.replace(potentialName, "").trim(), equip.getTypeID()), message));
                            break;
                        }
                    }
                }

                if (matcher.isEmpty())
                    for(SuperTypeEquipment type : SuperTypeEquipment.values())
                        matcher.evaluateAll(getListItemFrom(getSearchURL(type.getUrl(), normalName, null), message));

                if (matcher.isUnique()) { // We have found it !
                    Embedded item = Item.getItem(Constants.officialURL + matcher.getBest().getRight());
                    if (m.group(1) != null)
                        Message.sendEmbed(message.getChannel(), item.getMoreEmbedObject());
                    else
                        Message.sendEmbed(message.getChannel(), item.getEmbedObject());
                } else if (! matcher.isEmpty()) // Too much items
                    new TooMuchItemsDiscordException().throwException(message, this, matcher.getBests());
                else // empty
                    new ItemNotFoundDiscordException().throwException(message, this);
            } catch(IOException e){
                ExceptionManager.manageIOException(e, message, this, new ItemNotFoundDiscordException());
            }

            return true;
        }

        return false;
    }

    private String getSearchURL(String SuperTypeURL, String text, String typeArg) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(Constants.officialURL).append(SuperTypeURL)
                .append("?").append(forName.toLowerCase()).append(URLEncoder.encode(text, "UTF-8"))
                .append("&").append(forName.toUpperCase()).append(URLEncoder.encode(text, "UTF-8"))
                .append("&").append(and).append("&").append(forLevelMin).append(levelMin)
                .append("&").append(forLevelMax).append(levelMax).append("&").append(and);

        if (typeArg != null && ! typeArg.isEmpty())
            url.append(typeArg);

        return url.toString();
    }

    private List<Pair<String, String>> getListItemFrom(String url, IMessage message){
        List<Pair<String, String>> result = new ArrayList<>();
        try {
            Document doc = JSoupManager.getDocument(url);
            Elements elems = doc.getElementsByClass("ak-bg-odd");
            elems.addAll(doc.getElementsByClass("ak-bg-even"));

            for (Element element : elems)
                result.add(Pair.of(element.child(1).text(),
                        element.child(1).select("a").attr("href")));

        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, new ItemNotFoundDiscordException());
            return new ArrayList<>();
        }  catch (Exception e) {
            ExceptionManager.manageException(e, message, this);
            return new ArrayList<>();
        }

        return result;
    }

    private String removeUselessWords(String search){
        return search.replaceAll("\\s+\\w{1,3}\\s+", " ");
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** renvoie les statistiques d'un item du jeu Dofus.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + " `*`item`* : renvoie les statistiques de l'item spécifié :"
                + " son nom peut être approximatif s'il est suffisamment précis."
                + "\n" + prefixe + "`"  + name + " -more `*`item`* : renvoie les statistiques détaillées de l'item spécifié.\n";
    }
}

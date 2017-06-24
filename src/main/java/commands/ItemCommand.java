package commands;

import data.Constants;
import data.Embedded;
import data.Item;
import discord.Message;
import exceptions.ExceptionManager;
import exceptions.ItemNotFoundDiscordException;
import exceptions.TooMuchItemsDiscordException;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class ItemCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(ItemCommand.class);
    private final static String forName = "text=";
    private final static String forLevelMin = "&object_level_min=";
    private final static String levelMin = "50";
    private final static String forLevelMax = "&object_level_max=";
    private final static String levelMax = "200";
    private final static String and = "&EFFECTMAIN_and_or=AND";

    public ItemCommand(){
        super("item", "\\s+(.*)");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){
            Matcher m = getMatcher(message);
            m.find();
            String name = m.group(1).trim().toLowerCase();

            StringBuilder urlEquipement = null;
            StringBuilder urlWeapon = null;
            try {
                urlEquipement = new StringBuilder(Constants.officialURL + Constants.equipementPageURL)
                        .append("?").append(forName).append(URLEncoder.encode(name, "UTF-8"))
                        .append(and).append(forLevelMin).append(levelMin)
                        .append(forLevelMax).append(levelMax).append(and);
                urlWeapon = new StringBuilder(Constants.officialURL + Constants.weaponPageURL)
                        .append("?").append(forName).append(URLEncoder.encode(name, "UTF-8"))
                        .append(and).append(forLevelMin).append(levelMin)
                        .append(forLevelMax).append(levelMax).append(and);

            } catch (UnsupportedEncodingException e) {
                ExceptionManager.manageException(e, message, this);
                return false;
            }

            List<Pair<String, String>> items = getListItemFrom(urlEquipement, message);
            if (items == null) items = new ArrayList<>();
            items.addAll(getListItemFrom(urlWeapon, message));

            // If there still a problem
            if (items == null) return false;

            try {
                if (items.size() == 1) { // We have found it !
                    Embedded item = Item.getItem(Constants.officialURL + items.get(0).getRight());
                    Message.sendEmbed(message.getChannel(), item.getEmbedObject());
                } else if (items.size() > 1) {
                    // We are looking for a specific item. If not found, exception thrown
                    Pair<String, String> result = null;
                    for (Pair<String, String> pair : items)
                        if (name.equals(pair.getLeft().trim().toLowerCase())) {
                            result = pair;
                            break;
                        }

                    if (result != null){
                        Embedded item = Item.getItem(Constants.officialURL + result.getRight());
                        Message.sendEmbed(message.getChannel(), item.getEmbedObject());
                    }
                    else
                        new TooMuchItemsDiscordException().throwException(message, this, items);

                } else // empty
                    new ItemNotFoundDiscordException().throwException(message, this);
            } catch(IOException e){
                ExceptionManager.manageIOException(e, message, this, new ItemNotFoundDiscordException());
            }

            return true;
        }

        return false;
    }

    private List<Pair<String, String>> getListItemFrom(StringBuilder url, IMessage message){
        List<Pair<String, String>> result = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(new URL(url.toString()).openStream(), "UTF-8", url.toString());
            Elements elems = doc.getElementsByClass("ak-bg-odd");
            elems.addAll(doc.getElementsByClass("ak-bg-even"));

            for (Element element : elems)
                result.add(Pair.of(element.child(1).text(),
                        element.child(1).select("a").attr("href")));

        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, new ItemNotFoundDiscordException());
            return null;
        }  catch (Exception e) {
            ExceptionManager.manageException(e, message, this);
            return null;
        }

        return result;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** renvoit les statistiques d'un item du jeu Dofus.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + " `*`item`* : renvoie les statistiques de l'item spécifié :"
                + " son nom peut être approximatif s'il est suffisemment précis. A noter que les items inférieurs"
                + " au niveau " + levelMin + " sont exclus.\n";
    }
}

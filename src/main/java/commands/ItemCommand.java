package commands;

import data.Constants;
import data.Embedded;
import data.Item;
import discord.Message;
import exceptions.ExceptionManager;
import exceptions.ItemNotFoundException;
import exceptions.TooMuchPossibilitiesException;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class ItemCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(ItemCommand.class);
    private final static String forName = "text=";

    public ItemCommand(){
        super(Pattern.compile("item"),
        Pattern.compile("^(" + Constants.prefixCommand + "item)\\s+(.*)$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){

            String name = m.group(2).trim().toLowerCase();

            StringBuilder urlEquipement = null;
            StringBuilder urlWeapon = null;
            try {
                urlEquipement = new StringBuilder(Constants.officialURL + Constants.equipementPageURL)
                        .append("?").append(forName).append(URLEncoder.encode(name, "UTF-8"));
                urlWeapon = new StringBuilder(Constants.officialURL + Constants.weaponPageURL)
                        .append("?").append(forName).append(URLEncoder.encode(name, "UTF-8"));

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
                        new TooMuchPossibilitiesException().throwException(message, this);

                } else // empty
                    new ItemNotFoundException().throwException(message, this);
            } catch(IOException e){
                ExceptionManager.manageIOException(e, message, this);
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

        } catch (FileNotFoundException | HttpStatusException e){
            new ItemNotFoundException().throwException(message, this);
            return null;
        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this);
            return null;
        }  catch (Exception e) {
            ExceptionManager.manageException(e, message, this);
            return null;
        }

        return result;
    }

    @Override
    public boolean isUsableInMP() {
        return true;
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "item** renvoit les statistiques d'un item du jeu Dofus.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "item `*`item`* : renvoit les statistiques de l'item spécifié :"
                + " son nom peut être approximatif s'il est suffisemment précis.\n";
    }
}

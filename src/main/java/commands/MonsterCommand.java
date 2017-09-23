package commands;

import collections.BestMatcher;
import data.*;
import discord.Message;
import exceptions.ExceptionManager;
import exceptions.MonsterNotFoundDiscordException;
import exceptions.TooMuchMonstersDiscordException;
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
public class MonsterCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(MonsterCommand.class);
    private final static String forName = "text=";

    public MonsterCommand(){
        super("monster", "\\s+(-more)?(.*)");
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
                matcher.evaluateAll(getListMonsterFrom(getSearchURL(normalName), message));

                if (matcher.isUnique()) { // We have found it !
                    Embedded monster = Monster.getMonster(Constants.officialURL + matcher.getBest().getRight());
                    if (m.group(1) != null)
                        Message.sendEmbed(message.getChannel(), monster.getMoreEmbedObject());
                    else
                    Message.sendEmbed(message.getChannel(), monster.getEmbedObject());
                } else if (! matcher.isEmpty()) // Too much items
                    new TooMuchMonstersDiscordException().throwException(message, this, matcher.getBests());
                else // empty
                    new MonsterNotFoundDiscordException().throwException(message, this);
            } catch(IOException e){
                ExceptionManager.manageIOException(e, message, this, new MonsterNotFoundDiscordException());
            }

            return true;
        }

        return false;
    }

    private String getSearchURL(String text) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(Constants.officialURL).append(Constants.monsterPageURL)
                .append("?").append(forName.toLowerCase()).append(URLEncoder.encode(text, "UTF-8"))
                .append("&").append(forName.toUpperCase()).append(URLEncoder.encode(text, "UTF-8"));

        return url.toString();
    }

    private List<Pair<String, String>> getListMonsterFrom(String url, IMessage message){
        List<Pair<String, String>> result = new ArrayList<>();
        try {
            Document doc = JSoupManager.getDocument(url);
            Elements elems = doc.getElementsByClass("ak-bg-odd");
            elems.addAll(doc.getElementsByClass("ak-bg-even"));

            for (Element element : elems)
                result.add(Pair.of(element.child(1).text(),
                        element.child(1).select("a").attr("href")));

        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, new MonsterNotFoundDiscordException());
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
        return "**" + prefixe + name + "** renvoie les statistiques d'un monstre du jeu Dofus.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + " `*`monstre`* : renvoie les statistiques du monstre spécifié :"
                + " son nom peut être approximatif s'il est suffisamment précis."
                + "\n" + prefixe + "`"  + name + " -more `*`monstre`* : renvoie les statistiques détaillées du monstre spécifié.\n";
    }
}

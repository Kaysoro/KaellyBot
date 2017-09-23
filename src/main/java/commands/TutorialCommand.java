package commands;

import collections.BestMatcher;
import data.*;
import discord.Message;
import exceptions.ExceptionManager;
import exceptions.TooMuchTutosDiscordException;
import exceptions.TutoNotFoundDiscordException;
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
public class TutorialCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(TutorialCommand.class);
    private final static String forName = "q=";
    private final static String filtered = "filter=page";

    public TutorialCommand(){
        super("tuto", "\\s+(.*)");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){
            Matcher m = getMatcher(message);
            m.find();
            String normalName = Normalizer.normalize(m.group(1).trim(), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            String editedName = removeUselessWords(normalName);
            BestMatcher matcher = new BestMatcher(normalName);

            try {
                matcher.evaluateAll(getListTutoFrom(getSearchURL(normalName), message));

                if (matcher.isUnique())// We have found it !
                    Message.sendText(message.getChannel(), "Voici le lien du tutoriel souhaité : " +
                            Constants.dofusPourLesNoobURL + matcher.getBest().getRight());
                else if (! matcher.isEmpty()) // Too much items
                    new TooMuchTutosDiscordException().throwException(message, this, matcher.getBests());
                else // empty
                    new TutoNotFoundDiscordException().throwException(message, this);
            } catch(IOException e){
                ExceptionManager.manageIOException(e, message, this, new TutoNotFoundDiscordException());
            }

            return true;
        }

        return false;
    }

    private String getSearchURL(String text) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(Constants.dofusPourLesNoobURL).append(Constants.dofusPourLesNoobSearch)
                .append("?").append(forName.toLowerCase()).append(URLEncoder.encode(text, "UTF-8"))
                .append("&").append(filtered);

        return url.toString();
    }

    private List<Pair<String, String>> getListTutoFrom(String url, IMessage message){
        List<Pair<String, String>> result = new ArrayList<>();

        try {
            Document doc = JSoupManager.getDocument(url);
            Elements elems = doc.getElementById("wsite-search-list").getElementsByTag("a");

            for (Element element : elems)
                result.add(Pair.of(element.child(0).text(),
                        element.attr("href")));

        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, new TutoNotFoundDiscordException());
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
        return "**" + prefixe + name + "** renvoie le tutoriel correspondant à la recherche effectuée sur dofuspourlesnoobs.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + " `*`recherche`* : renvoie le tutoriel correspondant à la recherche effectuée"
                + " sur dofuspourlesnoobs: quête, donjon... Son nom peut être approximatif s'il est suffisamment précis.\n";
    }
}

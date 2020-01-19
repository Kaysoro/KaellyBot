package commands.classic;

import commands.model.AbstractCommand;
import discord4j.core.object.entity.Message;
import enums.Language;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import exceptions.TooMuchDiscordException;
import util.*;
import data.*;
import exceptions.ExceptionManager;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class TutorialCommand extends AbstractCommand {

    private final static String forName = "s=";
    private DiscordException tooMuchTutos;
    private DiscordException notFoundTuto;

    public TutorialCommand(){
        super("tuto", "\\s+(.*)");
        tooMuchTutos = new TooMuchDiscordException("tuto");
        notFoundTuto = new NotFoundDiscordException("tuto");
    }

    @Override
    public void request(Message message, Matcher m, Language lg) {
        String normalName = Normalizer.normalize(m.group(1).trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        String editedName = removeUselessWords(normalName);
        BestMatcher matcher = new BestMatcher(normalName);

        try {
            matcher.evaluateAll(getListTutoFrom(getSearchURL(editedName), message));

            if (matcher.isUnique())// We have found it !
                message.getChannel().flatMap(chan -> chan
                        .createMessage(Translator.getLabel(lg, "tutorial.request") + " " +
                                matcher.getBest().getUrl()))
                        .subscribe();
            else if (! matcher.isEmpty())  // Too much tutos
                tooMuchTutos.throwException(message, this, lg, matcher.getBests());
            else // empty
                notFoundTuto.throwException(message, this, lg);
        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, lg, notFoundTuto);
        }
    }

    private String getSearchURL(String text) throws UnsupportedEncodingException {
        return Constants.papychaURL + "?"
                + forName.toLowerCase() + URLEncoder.encode(text, "UTF-8").replace("%E2%80%99", "%27");
    }

    private List<Requestable> getListTutoFrom(String url, Message message){
        List<Requestable> result = new ArrayList<>();
        Language lg = Translator.getLanguageFrom(message.getChannel().block());

        try {
            Document doc = JSoupManager.getDocument(url);
            Elements elems = doc.getElementById("content").getElementsByTag("h2").select(".entry-title");

            for (Element element : elems)
                result.add(new Requestable(element.child(0).text(),
                        element.child(0).attr("href")));

        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, lg, notFoundTuto);
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
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "tutorial.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`search`* : " + Translator.getLabel(lg, "tutorial.help.detailed") + "\n";
    }
}

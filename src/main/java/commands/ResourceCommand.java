package commands;

import data.Constants;
import data.Embedded;
import data.Resource;
import enums.Language;
import enums.SuperTypeResource;
import enums.TypeResource;
import exceptions.ExceptionManager;
import exceptions.ResourceNotFoundDiscordException;
import exceptions.TooMuchResourcesDiscordException;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.BestMatcher;
import util.JSoupManager;
import util.Message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 27/10/2016.
 */
public class ResourceCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(ResourceCommand.class);
    private final static String forName = "text=";
    private final static String forLevelMin = "object_level_min=";
    private final static String levelMin = "1";
    private final static String forLevelMax = "object_level_max=";
    private final static String levelMax = "200";
    private final static String and = "EFFECTMAIN_and_or=AND";
    private final static String size = "size=";

    public ResourceCommand(){
        super("resource", "\\s+(-more)?(.*)");
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
                for (int i = 0; i < TypeResource.values().length; i++) {
                    TypeResource equip = TypeResource.values()[i];
                    for (int j = 0; j < equip.getNames().length; j++) {
                        String potentialName = Normalizer.normalize(equip.getNames()[j], Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
                        if (normalName.equals(potentialName)) {
                            matcher.evaluateAll(getListResourceFrom(getSearchURL(equip.getType().getUrl(),
                                    potentialName, equip.getTypeID()), message));
                            break;
                        } else if (normalName.contains(potentialName)) {
                            matcher.evaluateAll(getListResourceFrom(getSearchURL(equip.getType().getUrl(),
                                    editedName.replace(potentialName, "").trim(), equip.getTypeID()), message));
                            break;
                        }
                    }
                }

                if (matcher.isEmpty())
                    for (SuperTypeResource type : SuperTypeResource.values())
                        matcher.evaluateAll(getListResourceFrom(getSearchURL(type.getUrl(), normalName, null), message));

                if (matcher.isUnique()) { // We have found it !
                    Embedded resource = Resource.getResource(Constants.officialURL + matcher.getBest().getRight());
                    if (m.group(1) != null)
                        Message.sendEmbed(message.getChannel(), resource.getMoreEmbedObject());
                    else
                        Message.sendEmbed(message.getChannel(), resource.getEmbedObject());
                } else if (!matcher.isEmpty()) // Too much items
                    new TooMuchResourcesDiscordException().throwException(message, this, matcher.getBests());
                else // empty
                    new ResourceNotFoundDiscordException().throwException(message, this);
            } catch (IOException e) {
                ExceptionManager.manageIOException(e, message, this, new ResourceNotFoundDiscordException());
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
                .append("&").append(forLevelMax).append(levelMax).append("&").append(and)
                .append("&").append(size).append(50);

        if (typeArg != null && ! typeArg.isEmpty())
            url.append(typeArg);

        return url.toString();
    }

    private List<Pair<String, String>> getListResourceFrom(String url, IMessage message){
        List<Pair<String, String>> result = new ArrayList<>();
        try {
            Document doc = JSoupManager.getDocument(url);
            Elements elems = doc.getElementsByClass("ak-bg-odd");
            elems.addAll(doc.getElementsByClass("ak-bg-even"));

            for (Element element : elems)
                result.add(Pair.of(element.child(1).text(),
                        element.child(1).select("a").attr("href")));

        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, new ResourceNotFoundDiscordException());
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
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** renvoie les statistiques d'une ressource du jeu Dofus.";
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " `*`resource`* : renvoie les statistiques d'une ressource spécifiée :"
                + " son nom peut être approximatif s'il est suffisamment précis."
                + "\n" + prefixe + "`"  + name + " -more `*`resource`* : renvoie les statistiques détaillées de la ressource spécifiée.\n";
    }
}

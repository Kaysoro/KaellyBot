package commands;

import exceptions.*;
import sx.blah.discord.handle.obj.Permissions;
import util.BestMatcher;
import data.*;
import util.ClientConfig;
import util.Message;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.JSoupManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 12/10/2016.
 */
public class SetCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(SetCommand.class);
    private final static String forName = "text=";

    public SetCommand(){
        super("set", "\\s+(-more)?(.*)");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Matcher m = getMatcher(message);
            m.find();

            if (message.getChannel().getModifiedPermissions(ClientConfig.DISCORD().getOurUser()).contains(Permissions.USE_EXTERNAL_EMOJIS)
                    && ClientConfig.DISCORD().getOurUser().getPermissionsForGuild(message.getGuild())
                    .contains(Permissions.USE_EXTERNAL_EMOJIS)) {
                String normalName = Normalizer.normalize(m.group(2).trim(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
                String editedName = removeUselessWords(normalName);
                BestMatcher matcher = new BestMatcher(normalName);

                try {
                    matcher.evaluateAll(getListSetFrom(getSearchURL(editedName), message));

                    if (matcher.isUnique()) { // We have found it !
                        Embedded set = Set.getSet(Constants.officialURL + matcher.getBest().getRight());
                        if (m.group(1) != null)
                            Message.sendEmbed(message.getChannel(), set.getMoreEmbedObject());
                        else
                            Message.sendEmbed(message.getChannel(), set.getEmbedObject());
                    } else if (!matcher.isEmpty()) // Too much items
                        new TooMuchSetsDiscordException().throwException(message, this, matcher.getBests());
                    else // empty
                        new SetNotFoundDiscordException().throwException(message, this);
                } catch (IOException e) {
                    ExceptionManager.manageIOException(e, message, this, new SetNotFoundDiscordException());
                }

                return true;
            }
            else
                new NoExternalEmojiPermissionDiscordException().throwException(message, this);
        }

        return false;
    }

    private String getSearchURL(String text) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(Constants.officialURL).append(Constants.setPageURL)
                .append("?").append(forName.toLowerCase()).append(URLEncoder.encode(text, "UTF-8"))
                .append("&").append(forName.toUpperCase()).append(URLEncoder.encode(text, "UTF-8"));

        return url.toString();
    }

    private List<Pair<String, String>> getListSetFrom(String url, IMessage message){
        List<Pair<String, String>> result = new ArrayList<>();
        try {
            Document doc = JSoupManager.getDocument(url);
            Elements elems = doc.getElementsByClass("ak-bg-odd");
            elems.addAll(doc.getElementsByClass("ak-bg-even"));

            for (Element element : elems)
                result.add(Pair.of(element.child(1).text(),
                        element.child(1).select("a").attr("href")));

        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, new SetNotFoundDiscordException());
            return new ArrayList<>();
        }  catch (Exception e) {
            ExceptionManager.manageException(e, message, this);
            return new ArrayList<>();
        }

        return result;
    }

    private String removeUselessWords(String search){
        return search.replaceAll("\\s+\\w{1,2}\\s+", " ").replaceAll("panoplie", "");
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** renvoie les statistiques d'une panoplie du jeu Dofus.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + " `*`set`* : renvoie les statistiques d'une panoplie spécifiée :"
                + " son nom peut être approximatif s'il est suffisamment précis."
                + "\n" + prefixe + "`"  + name + " -more `*`set`* : renvoie les statistiques détaillées d'une panoplie spécifiée.\n";
    }
}

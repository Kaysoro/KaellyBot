package commands;

import enums.Language;
import exceptions.NoExternalEmojiPermissionDiscordException;
import sx.blah.discord.handle.obj.Permissions;
import util.*;
import data.*;
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
                    matcher.evaluateAll(getListMonsterFrom(getSearchURL(lg, editedName), message));

                    if (matcher.isUnique()) { // We have found it !
                        Embedded monster = Monster.getMonster(lg, Translator.getLabel(lg, "game.url")
                                + matcher.getBest().getRight());
                        if (m.group(1) != null)
                            Message.sendEmbed(message.getChannel(), monster.getMoreEmbedObject(lg));
                        else
                            Message.sendEmbed(message.getChannel(), monster.getEmbedObject(lg));
                    } else if (!matcher.isEmpty()) // Too much items
                        new TooMuchMonstersDiscordException().throwException(message, this, matcher.getBests());
                    else // empty
                        new MonsterNotFoundDiscordException().throwException(message, this);
                } catch (IOException e) {
                    ExceptionManager.manageIOException(e, message, this, new MonsterNotFoundDiscordException());
                }

                return true;
            }
            else
                new NoExternalEmojiPermissionDiscordException().throwException(message, this);
        }

        return false;
    }

    private String getSearchURL(Language lg, String text) throws UnsupportedEncodingException {
        return Translator.getLabel(lg, "game.url") + Translator.getLabel(lg, "monster.url")
                + "?" + forName.toLowerCase() + URLEncoder.encode(text, "UTF-8")
                + "&" + forName.toUpperCase() + URLEncoder.encode(text, "UTF-8");
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
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "monster.help")
                .replace("{game}", Constants.game);
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " `*`monstre`* : " + Translator.getLabel(lg, "monster.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " -more `*`monstre`* : " + Translator.getLabel(lg, "monster.help.detailed.2") + "\n";
    }
}

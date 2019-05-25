package commands.classic;

import commands.model.DofusRequestCommand;
import enums.Language;
import exceptions.*;
import util.*;
import data.*;
import sx.blah.discord.handle.obj.IMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class MonsterCommand extends DofusRequestCommand {

    private final static String forName = "text=";

    private DiscordException tooMuchMonsters;
    private DiscordException notFoundMonster;


    public MonsterCommand(){
        super("monster", "\\s+(-more)?(.*)");
        tooMuchMonsters = new TooMuchDiscordException("monster");
        notFoundMonster = new NotFoundDiscordException("monster");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        if (isChannelHasExternalEmojisPermission(message)) {
            String normalName = Normalizer.normalize(m.group(2).trim(), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            String editedName = removeUselessWords(normalName);
            BestMatcher matcher = new BestMatcher(normalName);

            try {
                matcher.evaluateAll(getListRequestableFrom(getSearchURL(lg, editedName), message, notFoundMonster));

                if (matcher.isUnique()) { // We have found it !
                    Embedded monster = Monster.getMonster(lg, Translator.getLabel(lg, "game.url")
                            + matcher.getBest().getUrl());
                    if (m.group(1) != null)
                        Message.sendEmbed(message.getChannel(), monster.getMoreEmbedObject(lg));
                    else
                        Message.sendEmbed(message.getChannel(), monster.getEmbedObject(lg));
                } else if (!matcher.isEmpty())  // Too much monsters
                    tooMuchMonsters.throwException(message, this, lg, matcher.getBests());
                else // empty
                    notFoundMonster.throwException(message, this, lg);
            } catch (IOException e) {
                ExceptionManager.manageIOException(e, message, this, lg, notFoundMonster);
            }
        }
        else
            BasicDiscordException.NO_EXTERNAL_EMOJI_PERMISSION.throwException(message, this, lg);
    }

    private String getSearchURL(Language lg, String text) throws UnsupportedEncodingException {
        return Translator.getLabel(lg, "game.url") + Translator.getLabel(lg, "monster.url")
                + "?" + forName.toLowerCase() + URLEncoder.encode(text, "UTF-8")
                + "&" + forName.toUpperCase() + URLEncoder.encode(text, "UTF-8");
    }

    private String removeUselessWords(String search){
        return search.replaceAll("\\s+\\w{1,3}\\s+", " ");
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "monster.help")
                .replace("{game}", Constants.game.getName());
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`monster`* : " + Translator.getLabel(lg, "monster.help.detailed.1")
                + "\n`" + prefixe + name + " -more `*`monster`* : " + Translator.getLabel(lg, "monster.help.detailed.2") + "\n";
    }
}

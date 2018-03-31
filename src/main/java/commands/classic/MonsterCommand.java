package commands.classic;

import commands.model.DofusRequestCommand;
import enums.Language;
import exceptions.*;
import sx.blah.discord.handle.obj.Permissions;
import util.*;
import data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger LOG = LoggerFactory.getLogger(MonsterCommand.class);
    private final static String forName = "text=";

    private DiscordException tooMuchMonsters;
    private DiscordException notFoundMonster;
    private DiscordException noExternalEmojiPermission;

    public MonsterCommand(){
        super("monster", "\\s+(-more)?(.*)");
        tooMuchMonsters = new TooMuchDiscordException("monster");
        notFoundMonster = new NotFoundDiscordException("monster");
        noExternalEmojiPermission = new BasicDiscordException("exception.basic.no_external_emoji_permission");
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

                return true;
            }
            else
                noExternalEmojiPermission.throwException(message, this, lg);
        }

        return false;
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
                .replace("{game}", Constants.game);
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " `*`monster`* : " + Translator.getLabel(lg, "monster.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " -more `*`monster`* : " + Translator.getLabel(lg, "monster.help.detailed.2") + "\n";
    }
}

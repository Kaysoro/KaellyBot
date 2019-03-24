package commands.model;

import enums.Language;
import enums.Type;
import exceptions.DiscordException;
import sx.blah.discord.handle.obj.IMessage;
import util.BestMatcher;
import util.Translator;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

public abstract class DofusEncyclopediaRequestCommand extends DofusRequestCommand {

    protected DofusEncyclopediaRequestCommand(String name, String pattern) {
        super(name, pattern);
    }

    protected void gatherData(IMessage message, BestMatcher matcher, String[] names, String normalName, Type type,
                              DiscordException notFound) throws UnsupportedEncodingException{
        Language lg = Translator.getLanguageFrom(message.getChannel());
        String editedName = removeUselessWords(normalName);

        for (String name : names) {
            String potentialName = Normalizer.normalize(name, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            if (normalName.equals(potentialName)) {
                matcher.evaluateAll(getListRequestableFrom(getSearchURL(type.getType().getUrl(lg),
                        potentialName, type.getTypeID(), lg), message, notFound));
                break;
            } else if (normalName.contains(potentialName)) {
                matcher.evaluateAll(getListRequestableFrom(
                        getSearchURL(type.getType().getUrl(lg), editedName.replace(potentialName, "").trim(),
                                type.getTypeID(), lg), message, notFound));
                break;
            }
        }
    }

    private String removeUselessWords(String search){
        return search.replaceAll("\\s+\\w{1,3}\\s+", " ");
    }

    protected abstract String getSearchURL(String SuperTypeURL, String text, String typeArg, Language lg) throws UnsupportedEncodingException;
}

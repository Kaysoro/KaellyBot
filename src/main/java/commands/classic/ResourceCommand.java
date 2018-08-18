package commands.classic;

import commands.model.DofusEncyclopediaRequestCommand;
import data.Embedded;
import data.Resource;
import enums.Language;
import enums.SuperTypeResource;
import enums.TypeResource;
import exceptions.*;
import sx.blah.discord.handle.obj.IMessage;
import util.BestMatcher;
import util.Message;
import util.Translator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.regex.Matcher;

/**
 * Created by steve on 27/10/2016.
 */
public class ResourceCommand extends DofusEncyclopediaRequestCommand {

    private final static String forName = "text=";
    private final static String forLevelMin = "object_level_min=";
    private final static String levelMin = "1";
    private final static String forLevelMax = "object_level_max=";
    private final static String levelMax = "200";
    private final static String and = "EFFECTMAIN_and_or=AND";
    private final static String size = "size=";

    private DiscordException tooMuchRsrcs;
    private DiscordException notFoundRsrc;

    public ResourceCommand(){
        super("resource", "\\s+(-more)?(.*)");
        tooMuchRsrcs = new TooMuchDiscordException("resource");
        notFoundRsrc = new NotFoundDiscordException("resource");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        if (isChannelHasExternalEmojisPermission(message)) {
            String normalName = Normalizer.normalize(m.group(2).trim(), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            BestMatcher matcher = new BestMatcher(normalName);

            try {
                for (TypeResource resource : TypeResource.values()) {
                    String[] names = resource.getNames(lg);
                    gatherData(message, matcher, names, normalName, resource, notFoundRsrc);
                }

                if (matcher.isEmpty())
                    for (SuperTypeResource type : SuperTypeResource.values())
                        matcher.evaluateAll(getListRequestableFrom(
                                getSearchURL(type.getUrl(lg), normalName, null, lg), message, notFoundRsrc));

                if (matcher.isUnique()) { // We have found it !
                    Embedded resource = Resource.getResource(lg, Translator.getLabel(lg, "game.url")
                            + matcher.getBest().getUrl());
                    if (m.group(1) != null)
                        Message.sendEmbed(message.getChannel(), resource.getMoreEmbedObject(lg));
                    else
                        Message.sendEmbed(message.getChannel(), resource.getEmbedObject(lg));
                } else if (!matcher.isEmpty()) // Too much items
                    tooMuchRsrcs.throwException(message, this, lg, matcher.getBests());
                else // empty
                    notFoundRsrc.throwException(message, this, lg);
            } catch (IOException e) {
                ExceptionManager.manageIOException(e, message, this, lg, notFoundRsrc);
            }
        }
        else
            BasicDiscordException.NO_EXTERNAL_EMOJI_PERMISSION.throwException(message, this, lg);
    }

    protected String getSearchURL(String SuperTypeURL, String text, String typeArg, Language lg) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(Translator.getLabel(lg, "game.url")).append(SuperTypeURL)
                .append("?").append(forName.toLowerCase()).append(URLEncoder.encode(text, "UTF-8"))
                .append("&").append(forName.toUpperCase()).append(URLEncoder.encode(text, "UTF-8"))
                .append("&").append(and).append("&").append(forLevelMin).append(levelMin)
                .append("&").append(forLevelMax).append(levelMax).append("&").append(and)
                .append("&").append(size).append(50);

        if (typeArg != null && ! typeArg.isEmpty())
            url.append(typeArg);

        return url.toString();
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "resource.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`resource`* : " + Translator.getLabel(lg, "resource.help.detailed.1")
                + "\n`" + prefixe + name + " -more `*`resource`* : " + Translator.getLabel(lg, "resource.help.detailed.2") + "\n";
    }
}

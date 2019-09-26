package commands.classic;

import commands.model.AbstractCommand;
import data.Character;
import enums.Language;
import util.JSoupManager;
import util.Message;
import exceptions.*;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.handle.obj.IMessage;
import util.ServerUtils;
import util.Translator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Exception;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class WhoisCommand extends AbstractCommand {

    private final static String forPseudo = "text=";
    private final static String forServer = "character_homeserv[]=";

    private DiscordException tooMuchCharacters;
    private DiscordException notFoundCharacter;

    public WhoisCommand(){
        super("whois","(\\s+-more)?(\\s+[\\p{L}|-]+)(\\s+.+)?");
        tooMuchCharacters = new TooMuchDiscordException("character");
        notFoundCharacter = new NotFoundDiscordException("character");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        String pseudo = m.group(2).trim().toLowerCase();

        StringBuilder url;
        try {
            url = new StringBuilder(Translator.getLabel(lg, "game.url"))
                    .append(Translator.getLabel(lg, "character.url"))
                    .append("?").append(forPseudo).append(URLEncoder.encode(pseudo, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            ExceptionManager.manageException(e, message, this, lg);
            return;
        }

        if (m.group(3) != null){
            String serverName = m.group(3).trim().toLowerCase();
            ServerUtils.ServerQuery serverQuery = ServerUtils.getServerDofusFromName(serverName);

            if (serverQuery.hasSucceed())
                url.append("&").append(forServer).append(serverQuery.getServer().getId());
            else {
                serverQuery.getExceptions()
                        .forEach(e -> e.throwException(message, this, lg, serverQuery.getServersFound()));
                return;
            }
        }

        try
        {
            Document doc = JSoupManager.getDocument(url.toString());
            Elements elems = doc.getElementsByClass("ak-bg-odd");
            elems.addAll(doc.getElementsByClass("ak-bg-even"));

            if (!elems.isEmpty()) {
                // on boucle jusqu'à temps de trouver le bon personnage (ie le plus proche du nom donnée)
                List<String> result = new ArrayList<>();
                List<String> servers = new ArrayList<>();

                for (Element element : elems)
                    if (pseudo.equals(element.child(1).text().trim().toLowerCase())) {
                        result.add(element.child(1).select("a").attr("href"));
                        servers.add(element.child(element.children().size() - 2).text());
                    }

                if (result.size() == 1) {

                    Connection.Response response = JSoupManager
                            .getResponse(Translator.getLabel(lg, "game.url") + result.get(0));

                    if (!response.url().getPath().endsWith(Translator.getLabel(lg, "whois.request"))) {
                        if (m.group(1) == null)
                            Message.sendEmbed(message.getChannel(), Character.getCharacter(
                                    Translator.getLabel(lg, "game.url") + result.get(0), lg)
                                    .getEmbedObject(lg));
                        else
                            Message.sendEmbed(message.getChannel(), Character.getCharacterStuff(
                                    Translator.getLabel(lg, "game.url") + result.get(0) + Translator.getLabel(lg, "character.stuff.url"), lg)
                                    .getMoreEmbedObject(lg));
                    } else
                        BasicDiscordException.CHARACTER_TOO_OLD.throwException(message, this, lg);
                }
                else if (result.size() > 1)
                    tooMuchCharacters.throwException(message, this, lg, servers);
                else
                    notFoundCharacter.throwException(message, this, lg);
            }
            else
                notFoundCharacter.throwException(message, this, lg);
        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, lg, BasicDiscordException.CHARACTERPAGE_INACCESSIBLE);
        }  catch (Exception e) {
            ExceptionManager.manageException(e, message, this, lg);
        }
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "whois.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`pseudo`* : " + Translator.getLabel(lg, "whois.help.detailed.1")
                + "\n`" + prefixe + name + " `*`pseudo server`* : " + Translator.getLabel(lg, "whois.help.detailed.2")
                + "\n`" + prefixe + name + " -more `*`pseudo server`* : " + Translator.getLabel(lg, "whois.help.detailed.3") + "\n";
    }
}

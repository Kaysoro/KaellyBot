package commands.classic;

import commands.model.AbstractCommand;
import data.Character;
import enums.Language;
import util.JSoupManager;
import data.ServerDofus;
import util.Message;
import exceptions.*;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
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

    private final static Logger LOG = LoggerFactory.getLogger(WhoisCommand.class);

    private final static String forPseudo = "TEXT=";
    private final static String forServer = "character_homeserv[]=";
    private final static String forClass = "character_breed_id[]=";
    private final static String forSex = "character_sex[]=";
    private final static String forLevelMin = "character_level_min=";
    private final static String forLevelMax = "character_level_max=";
    private final static String forGuild = "guild_id[]=";

    private DiscordException tooMuchCharacters;
    private DiscordException tooMuchServers;
    private DiscordException notFoundCharacter;
    private DiscordException characterPageInaccessible;
    private DiscordException characterTooOld;

    public WhoisCommand(){
        super("whois","(\\s+[\\p{L}|-]+)(\\s+.+)?");
        tooMuchCharacters = new TooMuchDiscordException("exception.toomuch.characters", "exception.toomuch.characters_found");
        tooMuchServers = new TooMuchDiscordException("exception.toomuch.servers", "exception.toomuch.servers_found");
        notFoundCharacter = new NotFoundDiscordException("exception.notfound.character", "exception.notfound.character_found");
        characterPageInaccessible = new BasicDiscordException("exception.basic.characterpage_inaccessible");
        characterTooOld = new BasicDiscordException("exception.basic.character_too_old");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            Matcher m = getMatcher(message);
            m.find();
            Language lg = Translator.getLanguageFrom(message.getChannel());
            String pseudo = m.group(1).trim().toLowerCase();

            StringBuilder url;
            try {
                url = new StringBuilder(Translator.getLabel(lg, "game.url"))
                        .append(Translator.getLabel(lg, "character.url"))
                        .append("?").append(forPseudo).append(URLEncoder.encode(pseudo, "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                ExceptionManager.manageException(e, message, this, lg);
                return false;
            }

            if (m.group(2) != null){
                String serverName = m.group(2).trim().toLowerCase();
                List<ServerDofus> result = new ArrayList<>();

                for(ServerDofus server : ServerDofus.getServersDofus())
                    if (server.getName().toLowerCase().startsWith(serverName))
                        result.add(server);

                if (result.size() == 1)
                    url.append("&").append(forServer).append(result.get(0).getId());
                else {
                    if (! result.isEmpty())
                        tooMuchServers.throwException(message, this, lg);
                    else
                        notFoundCharacter.throwException(message, this, lg);
                    return false;
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
                            Character characPage = Character.getCharacter(Translator.getLabel(lg, "game.url")
                                    + result.get(0), lg);
                            Message.sendEmbed(message.getChannel(), characPage.getEmbedObject(lg));
                        } else
                            characterTooOld.throwException(message, this, lg);
                    }
                    else if (result.size() > 1)
                        tooMuchCharacters.throwException(message, this, lg, servers);
                    else
                        notFoundCharacter.throwException(message, this, lg);
                }
                else
                    notFoundCharacter.throwException(message, this, lg);
            } catch(IOException e){
                ExceptionManager.manageIOException(e, message, this, lg, characterPageInaccessible);
            }  catch (Exception e) {
                ExceptionManager.manageException(e, message, this, lg);
            }
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "whois.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " `*`pseudo`* : " + Translator.getLabel(lg, "whois.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " `*`pseudo serveur`* : " + Translator.getLabel(lg, "whois.help.detailed.2") + "\n";
    }
}

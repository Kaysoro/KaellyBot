package commands;

import data.Character;
import data.Constants;
import data.JSoupManager;
import data.ServerDofus;
import discord.Message;
import exceptions.*;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
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
public class WhoisCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(WhoisCommand.class);

    private final static String forPseudo = "TEXT=";
    private final static String forServer = "character_homeserv[]=";
    private final static String forClass = "character_breed_id[]=";
    private final static String forSex = "character_sex[]=";
    private final static String forLevelMin = "character_level_min=";
    private final static String forLevelMax = "character_level_max=";
    private final static String forGuild = "guild_id[]=";

    public WhoisCommand(){
        super("whois","(\\s+[\\p{L}|-]+)(\\s+.+)?");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            Matcher m = getMatcher(message);
            m.find();
            String pseudo = m.group(1).trim().toLowerCase();

            StringBuilder url = null;
            try {
                url = new StringBuilder(Constants.officialURL + Constants.characterPageURL)
                        .append("?").append(forPseudo).append(URLEncoder.encode(pseudo, "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                ExceptionManager.manageException(e, message, this);
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
                        new TooMuchServersDiscordException().throwException(message, this);
                    else
                        new ServerNotFoundDiscordException().throwException(message, this);
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
                                .getResponse(Constants.officialURL + result.get(0));

                        if (!response.url().getPath().endsWith("indisponible")) {
                            Character characPage = Character.getCharacter(Constants.officialURL + result.get(0));
                            Message.sendEmbed(message.getChannel(), characPage.getEmbedObject());
                        } else
                            new CharacterTooOldDiscordException().throwException(message, this);
                    }
                    else if (result.size() > 1)
                        new TooMuchCharactersDiscordException().throwException(message, this, servers);
                    else
                        new CharacterNotFoundDiscordException().throwException(message, this);
                }
                else
                    new CharacterNotFoundDiscordException().throwException(message, this);
            } catch(IOException e){
                ExceptionManager.manageIOException(e, message, this, new CharacterPageNotFoundDiscordException());
            }  catch (Exception e) {
                ExceptionManager.manageException(e, message, this);
            }
        }
        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** donne la page personnelle d'un personnage.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + " *pseudo`* : donne la page personnelle associée au pseudo. Celui-ci doit être exact."
                + "\n" + prefixe + "`"  + name + " *pseudo serveur`* : est à utiliser lorsque le pseudo ne suffit pas pour déterminer la fiche d'un personnage.\n";
    }
}

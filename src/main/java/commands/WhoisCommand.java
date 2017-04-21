package commands;

import data.CharacterPage;
import data.Constants;
import discord.Message;
import exceptions.*;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.lang.Exception;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        super(Pattern.compile("whois"),
                Pattern.compile("^(" + Constants.prefixCommand + "whois)(\\s+[\\p{L}|-]+)(\\s+[\\p{L}]+)?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            String pseudo = m.group(2).trim().toLowerCase();

            StringBuilder url = new StringBuilder(Constants.dofusCharacterPageURL)
                    .append("?").append(forPseudo).append(pseudo);

            if (m.group(3) != null){
                String server = m.group(3).trim().toLowerCase();
                // Mapping serveurs dofus TODO
                url.append("&").append(forServer).append(server);
            }

            try
            {
                Document doc = Jsoup.parse(new URL(url.toString()).openStream(), "UTF-8", url.toString());
                Elements elems = doc.getElementsByClass("ak-bg-odd");
                elems.addAll(doc.getElementsByClass("ak-bg-even"));

                if (!elems.isEmpty()) {
                    // on boucle jusqu'à temps de trouver le bon personnage (ie le plus proche du nom donnée)
                    List<String> result = new ArrayList<>();

                    for (Element element : elems)
                        if (pseudo.equals(element.child(1).text().trim().toLowerCase()))
                            result.add(element.child(1).select("a").attr("href"));

                    if (result.size() == 1) {
                        CharacterPage characPage = CharacterPage.getCharacterPage("http://www.dofus.com" + result.get(0));
                        Message.sendEmbed(message.getChannel(), characPage.getEmbedObject());
                    }
                    else if (result.size() > 1)
                        new TooMuchPossibilitiesException().throwException(message, this);
                    else
                        new CharacterNotFoundException().throwException(message, this);
                }
                else
                    new CharacterNotFoundException().throwException(message, this);
            } catch (HttpStatusException e) {
                if (e.getStatusCode() >= 500 && e.getStatusCode() < 600){
                    LOG.warn(e.getMessage());
                    new DofusWebsiteInaccessibleException().throwException(message, this);
                }
                else {
                    LOG.error(e.getMessage());
                    Reporter.report(e);
                    new UnknownErrorException().throwException(message, this);
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
                Reporter.report(e);
                new UnknownErrorException().throwException(message, this);
            }
        }
        return false;
    }

    @Override
    public boolean isUsableInMP() {
        return true;
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "whois** donne la page personnelle d'un personnage.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "whois *pseudo`* : donne la page personnelle associée au pseudo. Celui-ci doit être exact."
                + "\n`" + Constants.prefixCommand + "whois *pseudo serveur`* : est à utiliser lorsque le pseudo ne suffit pas pour déterminer la fiche d'un personnage.\n";
    }
}

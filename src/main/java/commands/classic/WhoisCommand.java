package commands.classic;

import commands.model.AbstractCommand;
import data.Character;
import data.ServerDofus;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JSoupManager;
import util.Message;
import exceptions.*;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
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
import java.util.stream.Collectors;

/**
 * Created by steve on 14/07/2016.
 */
public class WhoisCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(WhoisCommand.class);
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
        ServerDofus server = null;
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

            if (serverQuery.hasSucceed()) {
                url.append("&").append(forServer).append(serverQuery.getServer().getId());
                server = serverQuery.getServer();
            }
            else {
                serverQuery.getExceptions()
                        .forEach(e -> e.throwException(message, this, lg, serverQuery.getServersFound()));
                return;
            }
        }

        try
        {
            Document doc = JSoupManager.getDocument(url.toString());
            Elements elements = doc.getElementsByClass("ak-bg-odd");
            elements.addAll(doc.getElementsByClass("ak-bg-even"));

            if (!elements.isEmpty()) {
                // loop until finding the nearest character
                List<CharacterQuery> result = elements.stream()
                        .filter(e -> pseudo.equals(e.child(1).text().trim().toLowerCase()))
                        .map(e -> new CharacterQuery()
                                .withUrl(e.child(1).select("a").attr("href"))
                                .withServer(e.child(e.children().size() - 2).text()))
                        .collect(Collectors.toList());

                // Ankama bug workaround
                if (server != null && result.size() > 1){
                    List<CharacterQuery> filteredResult = new ArrayList<>();
                    for(CharacterQuery query : result)
                        try {
                            JSoupManager.getResponse(Translator.getLabel(lg, "game.url") + query.getUrl());
                            filteredResult.add(query);
                        } catch(IOException e) {
                            LOG.warn("Not distinct character for same server: " + query.getUrl());
                        }

                    result = filteredResult;

                    if (result.isEmpty()){
                        BasicDiscordException.CHARACTERPAGE_INACCESSIBLE.throwException(message, this, lg);
                        return;
                    }
                }

                if (result.size() == 1) {

                    Connection.Response response = JSoupManager
                            .getResponse(Translator.getLabel(lg, "game.url") + result.get(0).getUrl());

                    if (!response.url().getPath().endsWith(Translator.getLabel(lg, "whois.request"))) {
                        if (m.group(1) == null)
                            Message.sendEmbed(message.getChannel(), Character.getCharacter(
                                    Translator.getLabel(lg, "game.url") + result.get(0).getUrl(), lg)
                                    .getEmbedObject(lg));
                        else
                            Message.sendEmbed(message.getChannel(), Character.getCharacterStuff(
                                    Translator.getLabel(lg, "game.url") + result.get(0).getUrl()
                                            + Translator.getLabel(lg, "character.stuff.url"), lg)
                                    .getMoreEmbedObject(lg));
                    } else
                        BasicDiscordException.CHARACTER_TOO_OLD.throwException(message, this, lg);
                }
                else if (result.size() > 1)
                    tooMuchCharacters.throwException(message, this, lg,
                            result.stream().map(CharacterQuery::getServer).distinct().collect(Collectors.toList()));
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
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "whois.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return help(lg, prefix)
                + "\n`" + prefix + name + " `*`pseudo`* : " + Translator.getLabel(lg, "whois.help.detailed.1")
                + "\n`" + prefix + name + " `*`pseudo server`* : " + Translator.getLabel(lg, "whois.help.detailed.2")
                + "\n`" + prefix + name + " -more `*`pseudo server`* : " + Translator.getLabel(lg, "whois.help.detailed.3") + "\n";
    }

    private class CharacterQuery {
        private String url;
        private String server;

        private String getUrl(){
            return url;
        }

        private String getServer(){
            return server;
        }

        private CharacterQuery withUrl(String url){
            this.url = url;
            return this;
        }

        private CharacterQuery withServer(String server){
            this.server = server;
            return this;
        }
    }
}

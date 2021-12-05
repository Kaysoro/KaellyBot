package commands.classic;

import commands.model.AbstractCommand;
import data.Alliance;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import enums.Language;
import exceptions.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.JSoupManager;
import util.ServerUtils;
import util.Translator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 04/04/2018.
 */
public class AllianceCommand extends AbstractCommand {

    private final static String forPseudo = "text=";
    private final static String forServer = "alliance_server_id[]=";

    private DiscordException tooMuchAlliances;
    private DiscordException notFoundAlliance;

    public AllianceCommand(){
        super("alliance","\\s+(.+)");
        tooMuchAlliances = new TooMuchDiscordException("alliance");
        notFoundAlliance = new NotFoundDiscordException("alliance");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        String pseudo = m.group(1).trim().toLowerCase();
        String serverName = null;

        if (Pattern.compile("\\s+-serv\\s+").matcher(pseudo).find()){
            String[] split = pseudo.split("\\s+-serv\\s+");
            pseudo = split[0];
            serverName = split[1];
        }

        StringBuilder url;
        try {
            url = new StringBuilder(Translator.getLabel(lg, "game.url"))
                    .append(Translator.getLabel(lg, "alliance.url"))
                    .append("?").append(forPseudo).append(URLEncoder.encode(pseudo, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            ExceptionManager.manageException(e, message, this, lg);
            return;
        }

        if (serverName != null){
            ServerUtils.ServerQuery serverQuery = ServerUtils.getServerDofusFromName(serverName, lg);
            if (serverQuery.hasSucceed()){
                url.append("&").append(forServer).append(serverQuery.getServer());
            }
            else {
                serverQuery.getExceptions().stream()
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
                // on boucle jusqu'à temps de trouver la bonne alliance (ie la plus proche du nom donnée)
                List<String> result = new ArrayList<>();
                List<String> servers = new ArrayList<>();

                for (Element element : elems)
                    if (pseudo.equals(element.child(1).text().trim().toLowerCase())) {
                        result.add(element.child(1).select("a").attr("href"));
                        servers.add(element.child(2).text());
                    }

                if (result.size() == 1) {
                    Alliance alliancePage = Alliance.getAlliance(Translator.getLabel(lg, "game.url")
                            + result.get(0), lg);
                    message.getChannel().flatMap(chan -> chan
                            .createEmbed(alliancePage.decorateEmbedObject(lg)))
                            .subscribe();
                }
                else if (result.size() > 1)
                    tooMuchAlliances.throwException(message, this, lg, servers);
                else
                    notFoundAlliance.throwException(message, this, lg);
            }
            else
                notFoundAlliance.throwException(message, this, lg);
        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, lg, BasicDiscordException.ALLIANCEPAGE_INACCESSIBLE);
        }  catch (Exception e) {
            ExceptionManager.manageException(e, message, this, lg);
        }
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "alliance.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`name`* : " + Translator.getLabel(lg, "alliance.help.detailed.1")
                + "\n`" + prefixe + name + " `*`name`*` -serv `*`server`* : " + Translator.getLabel(lg, "alliance.help.detailed.2") + "\n";
    }
}

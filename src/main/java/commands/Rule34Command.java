package commands;

import data.Constants;
import data.NSFWAuthorization;
import discord.Message;
import exceptions.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import sx.blah.discord.handle.obj.IMessage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class Rule34Command extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(Rule34Command.class);
    protected static String URL = "https://rule34.xxx/index.php?page=dapi&s=post&q=index&limit=1";

    public Rule34Command(){
        super(Pattern.compile("rule34"),
              Pattern.compile("^(" + Constants.prefixCommand + "rule34)(\\s+.+)?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            if (NSFWAuthorization.getNSFWChannels().containsKey(message.getChannel().getID())) {
                String url = URL;

                if (m.group(2) != null)
                    url += "&tags=" + m.group(2).trim();

                try {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new URL(url)
                            .openStream());
                    int count = Integer.parseInt(doc.getElementsByTagName("posts").item(0).
                            getAttributes().getNamedItem("count").getNodeValue());

                    if (count > 0) {
                        url += "&pid=" + new Random().nextInt(count);

                        doc = db.parse(new URL(url)
                                .openStream());

                        String link = "https:" + doc.getElementsByTagName("posts").item(0).getChildNodes()
                                .item(0).getAttributes().getNamedItem("file_url").getNodeValue();

                        Message.send(message.getChannel(), link);
                    } else
                        Message.send(message.getChannel(), "Aucune image ne correspond à `"
                                + m.group(2).trim() + "`.");

                } catch (Exception e) {
                    Reporter.report(e);
                    LOG.error(e.getMessage());
                }
            }
            else
                Message.send(message.getChannel(), message.getChannel().getName()
                        + " n'autorise pas du contenu NSFW. *!help nsfw* pour plus d'informations.");
        }
        return false;
    }

    @Override
    public boolean isUsableInMP() {
        return false;
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "rule34** est une commande NSFW. Elle poste du contenu sexuellement explicite.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "rule34` : poste une image NSFW au hasard."
                + "\n`" + Constants.prefixCommand + "rule34 `*`tag1 tag2 ...`* : poste une image NSFW en rapport avec les tag précisés.\n";
    }
}

package commands;

import discord.Message;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import sx.blah.discord.handle.obj.IMessage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class Rule34Command extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(Rule34Command.class);
    protected static String URL = "https://rule34.xxx/index.php?page=dapi&s=post&q=index&limit=1";

    public Rule34Command(){
        super("rule34", "(\\s+.+)");
        setUsableInMP(false);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            if (message.getChannel().isNSFW()) {
                try {
                    Matcher m = getMatcher(message);
                    m.find();
                    String url = URL + "&tags=" + URLEncoder.encode(m.group(1).trim(), "UTF-8");

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

                        Message.sendText(message.getChannel(), link);

                    } else
                        Message.sendText(message.getChannel(), "Aucune image ne correspond à `"
                                + m.group(1).trim() + "`.");

                } catch(NullPointerException e){
                    new APILimitExceededException().throwException(message, this);
                } catch(IOException e){
                    ExceptionManager.manageIOException(e, message, this, new WebsiteInaccessibleDiscordException());
                } catch (Exception e) {
                    ExceptionManager.manageException(e, message, this);
                }
            }
            else
                new NSFWNotAuthorizedDiscordException().throwException(message, this);
        }
        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** est une commande NSFW. Elle poste du contenu sexuellement explicite.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                //+ "\n`" + prefixe + name + "` : poste une image NSFW au hasard."
                + "\n" + prefixe + "`"  + name + " `*`tag1 tag2 ...`* : poste une image NSFW en rapport avec les tags précisés.\n";
    }
}

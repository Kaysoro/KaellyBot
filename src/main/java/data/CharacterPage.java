package data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

import java.net.URL;
import java.util.Random;

/**
 * Created by steve on 20/04/2017.
 */
public class CharacterPage {

    private final static Logger LOG = LoggerFactory.getLogger(CharacterPage.class);

    private String pseudo;
    private String level;
    private String classe;
    private String server;
    private String score;
    private String progression;
    private String guildName;
    private String guildUrl;
    private String alliName;
    private String alliUrl;
    private String littleSkinURL;
    private String bigSkinURL;
    private String url;

    public CharacterPage(String pseudo, String level, String classe, String server,
                         String score, String progression,
                         String guildName, String guildUrl, String alliName, String alliUrl,
                         String littleSkinURL, String bigSkinURL, String url) {
        this.pseudo = pseudo;
        this.level = level;
        this.classe = classe;
        this.server = server;
        this.score = score;
        this.progression = progression;
        this.guildName = guildName;
        this.guildUrl = guildUrl;
        this.alliName = alliName;
        this.alliUrl = alliUrl;
        this.littleSkinURL = littleSkinURL;
        this.bigSkinURL = bigSkinURL;
        this.url = url;
    }

    public EmbedObject getEmbedObject(){
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(pseudo);
        builder.withUrl(url);
        builder.withDescription("Page perso");

        builder.withColor(new Random().nextInt(16777216));
        builder.withThumbnail(littleSkinURL);
        builder.withImage(bigSkinURL);

        builder.appendField(":star: Niveau :", level, true);
        builder.appendField(":bust_in_silhouette: Classe :", classe, true);
        builder.appendField(":globe_with_meridians: Serveur :", server, true);
        builder.appendField(":trophy: Succès : " + score, "Progression : " + progression, true);

        if (guildName != null)
            builder.appendField(":shield: Guilde", "[" + guildName + "](" + guildUrl + ")", true);
        if (alliName != null)
            builder.appendField(":beginner: Alliance", "[" + alliName + "](" + alliUrl + ")", true);

        return builder.build();
    }

    public static CharacterPage getCharacterPage(String url) throws Exception {
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
        String bigSkinURL = doc.getElementsByClass("ak-entitylook").first().attr("style");
        bigSkinURL = bigSkinURL.substring(bigSkinURL.indexOf("http://"), bigSkinURL.indexOf(")"));
        String littleSkinURL = doc.getElementsByClass("ak-entitylook").last().toString();
        littleSkinURL = littleSkinURL.substring(littleSkinURL.indexOf("http://"), littleSkinURL.indexOf(")"));
        String pseudo = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-directories-level").first().text();
        String classe = doc.getElementsByClass("ak-directories-breed").first().text();
        String server = doc.getElementsByClass("ak-directories-server-name").first().text();
        String score = doc.getElementsByClass("ak-score-text").first().text();
        String progression = doc.getElementsByClass("ak-progress-bar-text").first().text();

        // Optional
        String guildName = null;
        String guildUrl = null;
        String alliName = null;
        String alliUrl = null;

        Elements elem = doc.getElementsByClass("ak-infos-guildname");

        if (!elem.isEmpty()) {
            guildName = elem.first().text();
            guildUrl = Constants.dofusURL + elem.first().select("a").attr("href");

            elem = doc.getElementsByClass("ak-infos-alliancename");

            if (!elem.isEmpty()) {
                alliName = elem.first().text();
                alliUrl = Constants.dofusURL + elem.first().select("a").attr("href");
            }
        }

        return new CharacterPage(pseudo, level, classe, server, score, progression,
                guildName, guildUrl, alliName, alliUrl, littleSkinURL, bigSkinURL, url);
    }
}

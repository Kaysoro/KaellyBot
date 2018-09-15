package data;

import enums.Language;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;
import util.EmojiManager;
import util.JSoupManager;
import util.Translator;

import java.io.IOException;
import java.util.Random;

/**
 * Created by steve on 20/04/2017.
 */
public class Character implements Embedded {

    // Classic values
    private String pseudo;
    private String level;
    private String classe;
    private String server;
    private String score;
    private String guildName;
    private String guildUrl;
    private String alliName;
    private String alliUrl;
    private String littleSkinURL;
    private String bigSkinURL;
    private String url;
    private String ladderXP;
    private String ladderKoli;
    private String ladderSuccess;

    // Stuff values
    private boolean stuffAvailable;
    private String primaire;
    private String secondaire;
    private String dommage;
    private String resistance;

    private Character(String pseudo, String level, String classe, String server, String score,
                      String guildName, String guildUrl, String alliName, String alliUrl,
                      String littleSkinURL, String bigSkinURL, String url, String ladderXP,
                      String ladderKoli, String ladderSuccess) {
        this.pseudo = pseudo;
        this.level = level;
        this.classe = classe;
        this.server = server;
        this.score = score;
        this.guildName = guildName;
        this.guildUrl = guildUrl;
        this.alliName = alliName;
        this.alliUrl = alliUrl;
        this.littleSkinURL = littleSkinURL;
        this.bigSkinURL = bigSkinURL;
        this.url = url;
        this.ladderXP = ladderXP;
        this.ladderKoli = ladderKoli;
        this.ladderSuccess = ladderSuccess;
    }

    private Character(String pseudo, String level, String classe, String server,
                      String littleSkinURL, String bigSkinURL, String url, boolean stuffAvailable,
                      String primaire, String secondaire, String dommage, String resistance) {
        this.pseudo = pseudo;
        this.level = level;
        this.classe = classe;
        this.server = server;
        this.littleSkinURL = littleSkinURL;
        this.bigSkinURL = bigSkinURL;
        this.url = url;
        this.stuffAvailable = stuffAvailable;
        this.primaire = primaire;
        this.secondaire = secondaire;
        this.dommage = dommage;
        this.resistance = resistance;
    }

    @Override
    public EmbedObject getEmbedObject(Language lg){
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(pseudo);
        builder.withUrl(url);
        builder.withDescription(classe);

        builder.withColor(new Random().nextInt(16777216));
        builder.withThumbnail(littleSkinURL);
        builder.withImage(bigSkinURL);
        builder.withFooterText(Translator.getLabel(lg, "whois.server") + " " + server);

        if (ladderXP != null && ! ladderXP.isEmpty())
            builder.appendField(Translator.getLabel(lg, "whois.level") + " " + level, ladderXP, true);
        else
            builder.appendField(Translator.getLabel(lg, "whois.level") + " " + level,
                    Translator.getLabel(lg, "whois.ladder.none"), true);

        if (ladderSuccess != null && ! ladderSuccess.isEmpty())
            builder.appendField(Translator.getLabel(lg, "whois.success") + " " + score,
                    ladderSuccess, true);
        else
            builder.appendField(Translator.getLabel(lg, "whois.success") + " " + score,
                    Translator.getLabel(lg, "whois.ladder.none"), true);

        if (ladderKoli != null && ! ladderKoli.isEmpty())
            builder.appendField(Translator.getLabel(lg, "whois.ladder_koli"), ladderKoli, true);

        if (guildName != null)
            builder.appendField(Translator.getLabel(lg, "whois.guild"), "[" + guildName + "](" + guildUrl + ")", true);
        if (alliName != null)
            builder.appendField(Translator.getLabel(lg, "whois.ally"), "[" + alliName + "](" + alliUrl + ")", true);

        return builder.build();
    }

    @Override
    public EmbedObject getMoreEmbedObject(Language lg) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(pseudo);
        builder.withUrl(url);
        builder.withDescription(classe + ", " + level);
        builder.withColor(new Random().nextInt(16777216));
        builder.withThumbnail(littleSkinURL);
        builder.withFooterText(Translator.getLabel(lg, "whois.server") + " " + server);

        if (stuffAvailable){
            builder.withImage(bigSkinURL);
            builder.appendField(Translator.getLabel(lg, "whois.stuff.primary"), primaire, true);
            builder.appendField(Translator.getLabel(lg, "whois.stuff.secondary"), secondaire, true);
            builder.appendField(Translator.getLabel(lg, "whois.stuff.damages"), dommage, true);
            builder.appendField(Translator.getLabel(lg, "whois.stuff.resistances"), resistance, true);
        }
        else {
            String[] punchlines = Translator.getLabel(lg, "whois.stuff.none.punchlines").split(";");
            String punchline = punchlines[new Random().nextInt(punchlines.length)];
            builder.appendField(Translator.getLabel(lg, "whois.stuff.none.title"), punchline, true);
        }

        return builder.build();
    }

    public static Character getCharacter(String url, Language lg) throws IOException {
        Document doc = JSoupManager.getDocument(url);
        String bigSkinURL = doc.getElementsByClass("ak-entitylook").first().attr("style");
        bigSkinURL = bigSkinURL.substring(bigSkinURL.indexOf("https://"), bigSkinURL.indexOf(")"));
        String littleSkinURL = doc.getElementsByClass("ak-entitylook").last().toString();
        littleSkinURL = littleSkinURL.substring(littleSkinURL.indexOf("https://"), littleSkinURL.indexOf(")"));
        String pseudo = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-directories-level").first().text()
                .replace(Translator.getLabel(lg, "whois.extract.level"), "").trim();
        String classe = doc.getElementsByClass("ak-directories-breed").first().text();
        String server = doc.getElementsByClass("ak-directories-server-name").first().text();
        String score = doc.getElementsByClass("ak-score-text").first().text() + " ("
                + doc.getElementsByClass("ak-progress-bar-text").first().text() + ")";

        // Optional
        String guildName = null;
        String guildUrl = null;
        String alliName = null;
        String alliUrl = null;

        Elements elem = doc.getElementsByClass("ak-infos-guildname");

        if (!elem.isEmpty()) {
            guildName = elem.first().text();
            guildUrl = elem.first().select("a").attr("abs:href");

            elem = doc.getElementsByClass("ak-infos-alliancename");

            if (!elem.isEmpty()) {
                alliName = elem.first().text();
                alliUrl = elem.first().select("a").attr("abs:href");
            }
        }

        StringBuilder ladderXP = new StringBuilder();
        StringBuilder ladderKoli = new StringBuilder();
        StringBuilder ladderSuccess = new StringBuilder();

        elem = doc.getElementsByClass("ak-container ak-table ak-responsivetable");
        if (!elem.isEmpty()) {
            ladderXP.append(doc.getElementsByClass("ak-total-xp").first().text()).append("\n");

            for(Element cote : doc.getElementsByClass("ak-total-kolizeum"))
                if (! cote.text().endsWith("-1"))
                    ladderKoli.append(cote.text().replace(Translator.getLabel(lg, "whois.extract.koli"), "").trim()).append("\n");

            Elements trs = elem.first().getElementsByTag("tbody").first().getElementsByTag("tr");
            for (Element tr : trs) {
                String ladderText = tr.getElementsByTag("td").first().text() + " : ";
                tr.getElementsByTag("td").first().remove();
                if (!tr.getElementsByTag("td").first().text().equals("-"))
                    ladderXP.append(ladderText).append(EmojiManager.getEmojiForLadder(tr.getElementsByTag("td").first().text())).append("\n");
                if (!tr.getElementsByTag("td").get(1).text().equals("-"))
                    ladderKoli.append(ladderText).append(EmojiManager.getEmojiForLadder(tr.getElementsByTag("td").get(1).text())).append("\n");
                if (!tr.getElementsByTag("td").last().text().equals("-"))
                    ladderSuccess.append(ladderText).append(EmojiManager.getEmojiForLadder(tr.getElementsByTag("td").last().text())).append("\n");
            }
        }

        return new Character(pseudo, level, classe, server, score,
                guildName, guildUrl, alliName, alliUrl, littleSkinURL, bigSkinURL, url,
                ladderXP.toString(), ladderKoli.toString(), ladderSuccess.toString());
    }

    public static Character getCharacterStuff(String url, Language lg) throws IOException {
        Document doc = JSoupManager.getDocument(url);
        String bigSkinURL = null;
        String littleSkinURL = doc.getElementsByClass("ak-entitylook").first().toString();
        littleSkinURL = littleSkinURL.substring(littleSkinURL.indexOf("https://"), littleSkinURL.indexOf(")"));
        String pseudo = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-directories-level").first().text();
        String classe = doc.getElementsByClass("ak-directories-breed").first().text();
        String server = doc.getElementsByClass("ak-directories-server-name").first().text();

        boolean stuffAvailable = !doc.getElementsByClass("ak-caracteristics-content").isEmpty();
        String primaire = null;
        String secondaire = null;
        String dommage = null;
        String resistance = null;

        if (stuffAvailable){
            bigSkinURL = doc.getElementsByClass("ak-entitylook").last().attr("style");
            bigSkinURL = bigSkinURL.substring(bigSkinURL.indexOf("https://"), bigSkinURL.indexOf(")"));
            primaire = getStats(doc.getElementsByClass("ak-primary-caracteristics").first(), lg);
            secondaire = getStats(doc.getElementsByClass("ak-secondary-caracteristics").first(), lg);
            dommage = getStats(doc.getElementsByClass("ak-primary-caracteristics").get(1), lg);
            resistance = getStats(doc.getElementsByClass("ak-secondary-caracteristics").get(1), lg);
        }

        return new Character(pseudo, level, classe, server, littleSkinURL, bigSkinURL, url, stuffAvailable,
                    primaire, secondaire, dommage, resistance);
    }

    private static String getStats(Element section, Language lg){
        StringBuilder tmp = new StringBuilder();
        Elements trs = section.getElementsByTag("tbody").first().getElementsByTag("tr");

        for (Element tr : trs) {
            Elements tds = tr.getElementsByTag("td");
            tmp.append(EmojiManager.getEmojiForStat(lg, tds.get(1).text()))
                    .append(tds.last().text()).append(" ").append(tds.get(1).text()).append("\n");
        }
        return tmp.toString();
    }
}

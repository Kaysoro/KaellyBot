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

    @Override
    public EmbedObject getEmbedObject(Language lg){
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(pseudo);
        builder.withUrl(url);
        builder.withDescription(Translator.getLabel(lg, classe));

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
        return getEmbedObject(lg);
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
}

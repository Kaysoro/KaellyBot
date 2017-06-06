package data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * Created by steve on 06/11/2016.
 */
public class Almanax implements Embedded{

    private final static Logger LOG = LoggerFactory.getLogger(Almanax.class);

    private String bonus;
    private String offrande;
    private String day;
    private String quest;
    private String ressourceURL;

    private Almanax(String bonus, String offrande, String day, String quest, String ressourceURL) {
        this.bonus = bonus;
        this.offrande = offrande;
        this.day = day;
        this.quest = quest;
        this.ressourceURL = ressourceURL;
    }

    @Override
    public EmbedObject getEmbedObject() {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle("Almanax du " + day);
        builder.withUrl(Constants.almanaxURL + day);
        builder.withDescription(quest);

        builder.withColor(new Random().nextInt(16777216));
        builder.withImage(ressourceURL);

        builder.appendField(":cyclone: Bonus : ", bonus, true);
        builder.appendField(":moneybag: Offrande : ", offrande, true);

        return builder.build();
    }

    public static Almanax get(String date) throws IOException {
        return gatheringOnlineData(date);
    }

    private static Almanax gatheringOnlineData(String date) throws IOException {
        LOG.info("connecting to " + Constants.almanaxURL + date + " ...");
        Document doc = Jsoup.parse(new URL(Constants.almanaxURL + date).openStream(), "UTF-8",
                Constants.almanaxURL + date);

        String bonus = doc.getElementsByClass("more").first()
                .clone().getElementsByClass("more-infos").empty().parents().first().text();
        String quest = doc.getElementsByClass("more-infos").first().child(0).text();
        String ressourceURL = doc.getElementsByClass("more-infos-content").first().children().attr("src");
        String offrande = doc.getElementsByClass("fleft").get(3).text();

        return new Almanax(bonus, offrande, date, quest, ressourceURL);
    }
}

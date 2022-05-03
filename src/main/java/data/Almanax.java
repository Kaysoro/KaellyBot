package data;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.EmbedData;
import discord4j.discordjson.json.EmbedFieldData;
import discord4j.discordjson.json.EmbedImageData;
import enums.Language;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JSoupManager;
import util.Translator;

import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by steve on 06/11/2016.
 */
public class Almanax implements Embedded{

    public final static DateFormat discordToBot = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    public final static DateFormat botToAlmanax = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
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
    public EmbedCreateSpec decorateEmbedObject(Language lg) {
        return EmbedCreateSpec.builder()
                .title(Translator.getLabel(lg, "almanax.embed.title.1") + " " + day)
                .url(Translator.getLabel(lg, "almanax.url") + day)
                .thumbnail(ressourceURL)
                .addField(Translator.getLabel(lg, "almanax.embed.bonus"), bonus, true)
                .addField(Translator.getLabel(lg, "almanax.embed.offrande"), offrande, true)
                .build();
    }

    @Override
    public EmbedCreateSpec decorateMoreEmbedObject(Language lg) {
        return EmbedCreateSpec.builder()
                .title(Translator.getLabel(lg, "almanax.embed.title.1") + " " + day)
                .url(Translator.getLabel(lg, "almanax.url") + day)
                .description(quest)
                .image(ressourceURL)
                .addField(Translator.getLabel(lg, "almanax.embed.bonus"), bonus, true)
                .addField(Translator.getLabel(lg, "almanax.embed.offrande"), offrande, true)
                .build();
    }

    public EmbedData decorateRestEmbedObject(Language lg){
        return EmbedData.builder()
                .title(Translator.getLabel(lg, "almanax.embed.title.1") + " " + day)
                .url(Translator.getLabel(lg, "almanax.url") + day)
                .description(quest)
                .image(EmbedImageData.builder().url(ressourceURL).build())
                .addField(EmbedFieldData.builder().name(Translator.getLabel(lg, "almanax.embed.bonus")).value(bonus).inline(true).build())
                .addField(EmbedFieldData.builder().name(Translator.getLabel(lg, "almanax.embed.offrande")).value(offrande).inline(true).build())
                .build();
    }

    public static EmbedCreateSpec decorateGroupedObject(Language lg, Date day, int occurrence) throws IOException {
        Date firstDate = DateUtils.addDays(day,1);
        Date lastDate = DateUtils.addDays(day, occurrence);
        String title = Translator.getLabel(lg, "almanax.embed.title.1") + " " + discordToBot.format(firstDate) +
                (occurrence > 1 ? " " + Translator.getLabel(lg, "almanax.embed.title.2") + " " + discordToBot.format(lastDate) : "");

        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .title(title);

        for (int i = 1; i <= occurrence; i++) {
            firstDate = DateUtils.addDays(new Date(), i);
            Almanax almanax = Almanax.get(lg, firstDate);
            builder.addField(discordToBot.format(firstDate), Translator.getLabel(lg, "almanax.embed.bonus")
                    + " " + almanax.getBonus() + "\n" + Translator.getLabel(lg, "almanax.embed.offrande")
                    + " " + almanax.getOffrande(), true);
        }
        return builder.build();
    }

    public static Almanax get(Language lg, String date) throws IOException {
        return gatheringOnlineData(lg, date);
    }

    public static Almanax get(Language lg, Date date) throws IOException {
        return gatheringOnlineData(lg, botToAlmanax.format(date));
    }

    private static Almanax gatheringOnlineData(Language lg, String date) throws IOException {
        LOG.info("connecting to " + Translator.getLabel(lg, "almanax.url") + date + "?game=dofustouch" + " ...");
        Document doc = JSoupManager.getDocument(Translator.getLabel(lg, "almanax.url") + date + "?game=dofustouch");

        String bonus = doc.getElementsByClass("more").first()
                .clone().getElementsByClass("more-infos").empty().parents().first().text();
        String quest = doc.getElementsByClass("more-infos").first().child(0).text();
        String ressourceURL = doc.getElementsByClass("more-infos-content").first().children().attr("src");
        String offrande = doc.getElementsByClass("fleft").get(3).text();

        return new Almanax(bonus, offrande, date, quest, ressourceURL);
    }

    public String getBonus() {
        return bonus;
    }

    public String getOffrande() {
        return offrande;
    }

    public String getDay() {
        return day;
    }

    public String getQuest() {
        return quest;
    }
}

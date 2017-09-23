package data;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by steve on 14/07/2016.
 */
public class Monster implements Embedded {

    private String name;
    private String family;
    private String level;
    private String skinURL;
    private String url;
    private String caracteristics;
    private String resistances;
    private String zones;
    private List<String> butins;
    private List<String> butinsConditionne;

    private Monster(String name, String family, String level, String caracteristics, String skinURL, String url,
                     String resistances, String zones, List<String> butins, List<String> butinsConditionne) {
        this.name = name;
        this.family = family;
        this.level = level;
        this.skinURL = skinURL;
        this.url = url;
        this.caracteristics = caracteristics;
        this.resistances = resistances;
        this.zones = zones;
        this.butins = butins;
        this.butinsConditionne = butinsConditionne;
    }

    @Override
    public EmbedObject getEmbedObject() {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(name);
        builder.withUrl(url);
        builder.withColor(new Random().nextInt(16777216));
        builder.withThumbnail(skinURL);

        if (level != null && ! level.isEmpty())
            builder.appendField(":star: Niveau :", level, true);
        builder.appendField(":family_mwgb: Race :", family, true);

        if (caracteristics != null && ! caracteristics.isEmpty())
            builder.appendField(":cyclone: Caractéristiques", caracteristics, true);

        if (resistances != null && ! resistances.isEmpty())
            builder.appendField(":shield: Résistances", resistances, true);

        return builder.build();
    }

    @Override
    public EmbedObject getMoreEmbedObject() {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(name);
        builder.withUrl(url);
        builder.withColor(new Random().nextInt(16777216));
        builder.withImage(skinURL);

        if (level != null && ! level.isEmpty())
            builder.appendField(":star: Niveau :", level, true);
        builder.appendField(":family_mwgb: Race :", family, true);

        if (caracteristics != null && ! caracteristics.isEmpty())
            builder.appendField(":cyclone: Caractéristiques", caracteristics, true);

        if (resistances != null && ! resistances.isEmpty())
            builder.appendField(":shield: Résistances", resistances, true);

        if (zones != null && ! zones.isEmpty())
            builder.appendField(":map: Zones", zones, true);

        if (! butins.isEmpty())
            for(int i = 0; i < butins.size(); i++)
                builder.appendField(":moneybag: Butins" + (butins.size() > 1?
                                " (" + (i + 1) + "/" + butins.size() + ")" : ""),
                        butins.get(i), true);

        if (! butinsConditionne.isEmpty())
            for(int i = 0; i < butinsConditionne.size(); i++)
                builder.appendField(":moneybag: Butins conditionnés" + (butinsConditionne.size() > 1?
                                " (" + (i + 1) + "/" + butinsConditionne.size() + ")" : ""),
                        butinsConditionne.get(i), true);

        return builder.build();
    }

    public static Monster getMonster(String url) throws IOException {
        Document doc = JSoupManager.getDocument(url);

        String name = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-encyclo-detail-level").first().text();
        String family = doc.getElementsByClass("ak-encyclo-detail-type").last().children().last().text();

        String skinURL = doc.getElementsByClass("ak-encyclo-detail-illu").first()
                .getElementsByTag("img").first().attr("src");

        String caracteristics = null;
        String resistances = null;
        String zones = null;
        List<String> butins = new ArrayList<>();
        List<String> butinsConditionne = new ArrayList<>();

        Elements titles = doc.getElementsByClass("ak-panel-title");
        for (Element title : titles)
            if (title.text().equals("Caractéristiques"))
                caracteristics = extractLinesFromTitle(title);
            else if (title.text().equals("Résistances"))
                resistances = extractLinesFromTitle(title);
            else if (title.text().equals("Zones"))
                zones = title.parent().children().last().text();
            else if (title.text().equals("Butins"))
                extractButins(butins, title.parent().getElementById("ak-encyclo-monster-drops ak-container ak-content-list"));
            else if (title.text().equals("Butins conditionnés"))
                extractButins(butinsConditionne, title.parent());

        return new Monster(name, family, level, caracteristics, skinURL, url, resistances, zones, butins, butinsConditionne);
    }

    private static String extractLinesFromTitle(Element title)
    {
        Elements lines = title.parent().getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(line.text()).append("\n");
        return tmp.toString();
    }

    private static void extractButins(List<String> butins, Element element){
        StringBuilder field = new StringBuilder();
        Elements lines = element.getElementsByClass("ak-column");
        for (Element line : lines) {
            StringBuilder tmp = new StringBuilder();
            tmp.append(line.getElementsByClass("ak-front").text());

            if (! line.getElementsByClass("ak-title").first().children().isEmpty())
                    tmp.append("[")
                    .append(line.getElementsByClass("ak-title").first().text()).append("](")
                    .append(Constants.officialURL)
                    .append(line.getElementsByClass("ak-title").first()
                            .children().first().attr("href")).append(") ");
            else
                tmp.append(line.getElementsByClass("ak-title").first().text()).append(" ");

            tmp.append(line.getElementsByClass("ak-drop-percent").first().text()).append("\n");

            if (field.length() + tmp.length() > EmbedBuilder.FIELD_CONTENT_LIMIT){
                butins.add(field.toString());
                field.setLength(0);
            }
            field.append(tmp.toString());
        }

        if (field.length() > 0)
            butins.add(field.toString());
    }
}
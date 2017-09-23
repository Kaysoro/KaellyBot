package data;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.util.Random;

/**
 * Created by steve on 14/07/2016.
 */
public class Item implements Embedded {

    private String name;
    private String type;
    private String level;
    private String description;
    private String effects;
    private String skinURL;
    private String url;

    // optional
    private String caracteristics;
    private String conditions;
    private String panoplie;
    private String panoplieURL;
    private String recipe;

    private Item(String name, String type, String level, String description, String effects, String skinURL, String url,
                 String caracteristics, String conditions, String panoplie, String panoplieURL, String recipe) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.description = description;
        this.effects = effects;
        this.skinURL = skinURL;
        this.url = url;
        this.caracteristics = caracteristics;
        this.conditions = conditions;
        this.panoplie = panoplie;
        this.panoplieURL = panoplieURL;
        this.recipe = recipe;
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
        builder.appendField(":dagger: Type :", type, true);

        if (effects != null && ! effects.isEmpty())
        builder.appendField(":cyclone: Effets :", effects, true);

        if (caracteristics != null && ! caracteristics.isEmpty())
            builder.appendField(":gear: Caractéristiques", caracteristics, true);

        if (conditions != null && ! conditions.isEmpty())
            builder.appendField(":key: Conditions",conditions, true);

        if (panoplie != null && panoplieURL != null)
            builder.appendField(":link: Panoplie", "[" + panoplie + "](" + panoplieURL + ")", true);

        return builder.build();
    }

    @Override
    public EmbedObject getMoreEmbedObject() {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(name);
        builder.withUrl(url);
        if (description != null && ! description.isEmpty())
            builder.withDescription(description);

        builder.withColor(new Random().nextInt(16777216));
        builder.withImage(skinURL);

        if (level != null && ! level.isEmpty())
            builder.appendField(":star: Niveau :", level, true);
        builder.appendField(":dagger: Type :", type, true);

        if (effects != null && ! effects.isEmpty())
            builder.appendField(":cyclone: Effets :", effects, true);

        if (caracteristics != null && ! caracteristics.isEmpty())
            builder.appendField(":gear: Caractéristiques", caracteristics, true);

        if (conditions != null && ! conditions.isEmpty())
            builder.appendField(":key: Conditions",conditions, true);

        if (panoplie != null && panoplieURL != null)
            builder.appendField(":link: Panoplie", "[" + panoplie + "](" + panoplieURL + ")", true);

        if (recipe != null)
            builder.appendField(":hammer_pick: Recette", recipe, true);

        return builder.build();
    }

    public static Item getItem(String url) throws IOException {
        Document doc = JSoupManager.getDocument(url);

        String name = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-encyclo-detail-level").first().text();
        String type = doc.getElementsByClass("ak-encyclo-detail-type").last().children().last().text();

        String skinURL = doc.getElementsByClass("ak-encyclo-detail-illu").first()
                .getElementsByTag("img").first().attr("src");

        String description = null;
        String effects = null;
        String caracteristics = null;
        String conditions = null;
        String panoplie = null;
        String panoplieURL = null;
        String recipe = null;

        Elements titles = doc.getElementsByClass("ak-panel-title");
        Elements lines;
        StringBuilder tmp;
        for (Element title : titles)
            if (title.text().equals("Description"))
                description = title.parent().getElementsByClass("ak-panel-content").first().text();
            else if (title.text().equals("Effets"))
                effects = extractLinesFromTitle(title);
            else if (title.text().equals("Caractéristiques"))
                caracteristics = extractLinesFromTitle(title);
            else if (title.text().equals("Conditions"))
                conditions = extractLinesFromTitle(title);
            else if (title.text().contains(name + " fait partie de")){
                panoplie = title.getElementsByTag("a").first().text();
                panoplieURL = Constants.officialURL + title.getElementsByTag("a").first().attr("href");
            }
            else if (title.text().equals("Recette")){
                lines = title.parent().getElementsByClass("ak-column");
                tmp = new StringBuilder();
                for (Element line : lines)
                    tmp.append(line.getElementsByClass("ak-front").text()).append(" [")
                            .append(line.getElementsByClass("ak-title").first().text()).append("](")
                            .append(Constants.officialURL)
                            .append(line.getElementsByClass("ak-title").first()
                            .children().first().attr("href")).append(")\n");
                recipe = tmp.toString();
            }

        return new Item(name, type, level, description, effects, skinURL, url,
                caracteristics, conditions, panoplie, panoplieURL, recipe);
    }

    private static String extractLinesFromTitle(Element title)
    {
        Elements lines = title.parent().getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(line.text()).append("\n");
        return tmp.toString();
    }
}


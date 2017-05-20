package data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.net.URL;
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
        builder.withDescription(description);

        builder.withColor(new Random().nextInt(16777216));
        builder.withImage(skinURL);

        builder.appendField(":star: Niveau :", level, true);
        builder.appendField(":dagger: Type :", type, true);
        builder.appendField(":cyclone: Effets :", effects, true);

        if (caracteristics != null)
            builder.appendField(":gear: Caractéristiques", caracteristics, true);

        if (conditions != null)
            builder.appendField(":key: Conditions",conditions, true);

        if (panoplie != null && panoplieURL != null)
            builder.appendField(":link: Panoplie", "[" + panoplie + "](" + panoplieURL + ")", true);

        if (recipe != null)
            builder.appendField(":hammer_pick: Recette", recipe, true);

        return builder.build();
    }

    public static Item getItem(String url) throws IOException {
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);

        String name = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-encyclo-detail-level").first().text();
        String description = doc.getElementsByClass("ak-panel-content").get(3).text();
        String type = doc.getElementsByClass("ak-encyclo-detail-type").last().children().last().text();
        String skinURL = doc.getElementsByClass("ak-encyclo-detail-illu").first()
                .children().first().attr("src");

        Elements lines = doc.getElementsByClass("ak-panel-content").get(5)
                .getElementsByClass("ak-title");

        StringBuilder tmp = new StringBuilder();
        for(Element line : lines)
            tmp.append(line.text()).append("\n");
        String effects = tmp.toString();

        // Optional
        String caracteristics = null;
        String conditions = null;
        String panoplie = null;
        String panoplieURL = null;
        String recipe = null;

        Elements titles = doc.getElementsByClass("ak-panel-title");
        int next = 0;
        for(Element title : titles){
            if (title.text().equals("Caractéristiques")){
                lines = doc.getElementsByClass("ak-panel-content").get(6 + next++)
                        .getElementsByClass("ak-title");
                tmp = new StringBuilder();
                for(Element line : lines)
                    tmp.append(line.text()).append("\n");
                caracteristics = tmp.toString();
            }
            else if (title.text().equals("Conditions")){
                lines = doc.getElementsByClass("ak-panel-content").get(6 + next++)
                        .getElementsByClass("ak-title");
                tmp = new StringBuilder();
                for(Element line : lines)
                    tmp.append(line.text()).append("\n");
                conditions = tmp.toString();
            }
            else if(title.text().contains(name + " fait partie de")){
                panoplie = title.getElementsByTag("a").first().text();
                panoplieURL = Constants.officialURL + title.getElementsByTag("a").first().attr("href");
            }
            else if(title.text().equals("Recette")){
                lines = doc.getElementsByClass("ak-panel-content").get(6 + next++)
                        .getElementsByClass("ak-column");
                tmp = new StringBuilder();
                for(Element line : lines)
                    tmp.append(line.getElementsByClass("ak-front").text()).append(" [")
                            .append(line.getElementsByClass("ak-title").first().text()).append("](")
                            .append(Constants.officialURL)
                            .append(line.getElementsByClass("ak-title").first().children().first()
                                    .attr("href")).append(")\n");
                recipe = tmp.toString();
            }
        }

        return new Item(name, type, level, description, effects, skinURL, url,
                caracteristics, conditions, panoplie, panoplieURL, recipe);
    }
}


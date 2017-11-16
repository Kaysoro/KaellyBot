package data;

import enums.Language;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;
import util.EmojiManager;
import util.JSoupManager;
import util.URLManager;

import java.io.IOException;
import java.util.Random;

public class Resource implements Embedded {

    private String name;
    private String type;
    private String level;
    private String description;
    private String effects;
    private String skinURL;
    private String url;
    private String bonus;
    private String sorts;
    private String recipe;

    private Resource(String name, String type, String level, String description, String effects, String skinURL, String url,
                 String bonus, String sorts, String recipe) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.description = description;
        this.effects = effects;
        this.skinURL = skinURL;
        this.url = url;
        this.bonus = bonus;
        this.sorts = sorts;
        this.recipe = recipe;
    }

    @Override
    public EmbedObject getEmbedObject(Language lg) {
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

        if (bonus != null && ! bonus.isEmpty())
            builder.appendField(":gear: Bonus :", bonus, true);

        if (sorts != null && ! sorts.isEmpty())
            builder.appendField(":key: Sorts :",sorts, true);

        return builder.build();
    }

    @Override
    public EmbedObject getMoreEmbedObject(Language lg) {
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

        if (bonus != null && ! bonus.isEmpty())
            builder.appendField(":gear: Bonus :", bonus, true);

        if (sorts != null && ! sorts.isEmpty())
            builder.appendField(":key: Sorts :",sorts, true);

        if (recipe != null)
            builder.appendField(":hammer_pick: Recette :", recipe, true);

        return builder.build();
    }

    public static Resource getResource(String url) throws IOException {
        Document doc = JSoupManager.getDocument(url);
        doc.setBaseUri(url);
        String name = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-encyclo-detail-level").first().text().replaceAll("Niveau : ", "");;
        String type = doc.getElementsByClass("ak-encyclo-detail-type").last().children().last().text();

        String skinURL = doc.getElementsByClass("ak-encyclo-detail-illu").first()
                .getElementsByTag("img").first().attr("src");

        String description = null;
        String effects = null;
        String bonus = null;
        String sorts = null;
        String recipe = null;

        Elements titles = doc.getElementsByClass("ak-panel-title");
        Elements lines;
        StringBuilder tmp;
        for (Element title : titles)
            if (title.text().equals("Description"))
                description = title.parent().getElementsByClass("ak-panel-content").first().text();
            else if (title.text().equals("Effets"))
                effects = extractStatsFromTitle(title);
            else if (title.text().equals("Bonus"))
                bonus = extractLinesFromTitle(title);
            else if (title.text().equals("Sorts"))
                sorts = title.parent().getElementsByClass("ak-panel-content").first().text();
            else if (title.text().equals("Recette")){
                lines = title.parent().getElementsByClass("ak-column");
                tmp = new StringBuilder();
                for (Element line : lines)
                    tmp.append(line.getElementsByClass("ak-front").text()).append(" [")
                            .append(line.getElementsByClass("ak-title").first().text()).append("](")
                            .append(line.getElementsByClass("ak-title").first()
                                    .children().first().attr("abs:href")).append(")\n");
                recipe = tmp.toString();
            }

        return new Resource(name, type, level, description, effects, URLManager.abs(skinURL), url,
                bonus, sorts, recipe);
    }

    private static String extractLinesFromTitle(Element title)
    {
        Elements lines = title.parent().getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(line.text()).append("\n");
        return tmp.toString();
    }

    private static String extractStatsFromTitle(Element title)
    {
        Elements lines = title.parent().getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(EmojiManager.getEmojiForStat(line.text())).append(line.text()).append("\n");
        return tmp.toString();
    }
}


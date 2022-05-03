package data;

import discord4j.core.object.Embed;
import discord4j.core.spec.EmbedCreateSpec;
import enums.Language;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.EmojiManager;
import util.JSoupManager;
import util.Translator;
import util.URLManager;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> monsterDrop;

    private Resource(String name, String type, String level, String description, String effects, String skinURL, String url,
                 String bonus, String sorts, String recipe, List<String> monsterDrop) {
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
        this.monsterDrop = monsterDrop;
    }

    @Override
    public EmbedCreateSpec decorateEmbedObject(Language lg) {
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .title(name)
                .url(url)
                .thumbnail(skinURL);

        if (level != null && ! level.isEmpty())
            builder.addField(Translator.getLabel(lg, "resource.niveau"), level, true);
        builder.addField(Translator.getLabel(lg, "resource.type"), type, true);

        if (effects != null && ! effects.isEmpty())
            builder.addField(Translator.getLabel(lg, "resource.effets"), effects, true);

        if (bonus != null && ! bonus.isEmpty())
            builder.addField(Translator.getLabel(lg, "resource.bonus"), bonus, true);

        if (sorts != null && ! sorts.isEmpty())
            builder.addField(Translator.getLabel(lg, "resource.sorts"),sorts, true);
        return builder.build();
    }

    @Override
    public EmbedCreateSpec decorateMoreEmbedObject(Language lg) {
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .title(name)
                .url(url)
                .image(skinURL);

        if (description != null && ! description.isEmpty())
            builder.description(description);

        if (level != null && ! level.isEmpty())
            builder.addField(Translator.getLabel(lg, "resource.niveau"), level, true);
        builder.addField(Translator.getLabel(lg, "resource.type"), type, true);

        if (effects != null && ! effects.isEmpty())
            builder.addField(Translator.getLabel(lg, "resource.effets"), effects, true);

        if (bonus != null && ! bonus.isEmpty())
            builder.addField(Translator.getLabel(lg, "resource.bonus"), bonus, true);

        if (sorts != null && ! sorts.isEmpty())
            builder.addField(Translator.getLabel(lg, "resource.sorts"),sorts, true);

        if (recipe != null)
            builder.addField(Translator.getLabel(lg, "resource.recette"), recipe, true);

        if (! monsterDrop.isEmpty())
            for(int i = 0; i < monsterDrop.size(); i++)
                builder.addField(Translator.getLabel(lg, "resource.monster")
                                + (monsterDrop.size() > 1? " (" + (i + 1) + "/" + monsterDrop.size() + ")" : "") + " : ",
                        monsterDrop.get(i), true);
        return builder.build();
    }

    public static Resource getResource(Language lg, String url) throws IOException {
        Document doc = JSoupManager.getDocument(url);
        doc.setBaseUri(url);
        String name = doc.getElementsByClass("ak-return-link").first().text();
        String level = null;
        if (! doc.getElementsByClass("ak-encyclo-detail-level").isEmpty())
            level = doc.getElementsByClass("ak-encyclo-detail-level").first().text()
                .replaceAll(Translator.getLabel(lg, "resource.extract.level") + " ", "");
        String type = doc.getElementsByClass("ak-encyclo-detail-type").last().children().last().text();

        String skinURL = doc.getElementsByClass("ak-encyclo-detail-illu").first()
                .getElementsByTag("img").first().attr("src");

        String description = null;
        String effects = null;
        String bonus = null;
        String sorts = null;
        String recipe = null;
        List<String> monsterDrops = new ArrayList<>();

        Elements titles = doc.getElementsByClass("ak-panel-title");
        Elements lines;
        StringBuilder tmp;
        for (Element title : titles)
            if (title.text().equals(Translator.getLabel(lg, "resource.extract.description")))
                description = title.parent().getElementsByClass("ak-panel-content").first().text();
            else if (title.text().equals(Translator.getLabel(lg, "resource.extract.effets")))
                effects = extractStatsFromTitle(lg, title);
            else if (title.text().equals(Translator.getLabel(lg, "resource.extract.bonus")))
                bonus = extractLinesFromTitle(title);
            else if (title.text().equals(Translator.getLabel(lg, "resource.extract.sorts")))
                sorts = title.parent().getElementsByClass("ak-panel-content").first().text();
            else if (title.text().equals(Translator.getLabel(lg, "resource.extract.monsterDrop")))
                monsterDrops = extractDrops(title.parent());
            else if (title.text().equals(Translator.getLabel(lg, "resource.extract.recette"))){
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
                bonus, sorts, recipe, monsterDrops);
    }

    private static String extractLinesFromTitle(Element title)
    {
        Elements lines = title.parent().getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(line.text()).append("\n");
        return tmp.toString();
    }

    private static String extractStatsFromTitle(Language lg, Element title)
    {
        Elements lines = title.parent().getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(EmojiManager.getEmojiForStat(lg, line.text())).append(line.text()).append("\n");
        return tmp.toString();
    }

    /**
     * @param element Element contenant les drops
     */
    private static List<String> extractDrops(Element element){
        List<String> result = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        Elements lines = element.getElementsByClass("ak-column");
        for (Element line : lines) {
            StringBuilder tmp = new StringBuilder();

            Elements titles = line.getElementsByClass("ak-title");
            if (!titles.isEmpty()) {
                if (!titles.first().children().isEmpty())
                    tmp.append("[").append(titles.first().text()).append("](")
                            .append(titles.first().children().first().attr("abs:href")).append(") ");
                else
                    tmp.append(titles.first().text()).append(" ");

                tmp.append(line.getElementsByClass("ak-aside").first().text()).append("\n");

                if (field.length() + tmp.length() > Embed.Field.MAX_VALUE_LENGTH) {
                    result.add(field.toString());
                    field.setLength(0);
                }
                field.append(tmp.toString());
            }
        }

        if (field.length() > 0)
            result.add(field.toString());

        return result;
    }
}


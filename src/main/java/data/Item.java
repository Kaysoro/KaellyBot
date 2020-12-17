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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

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
    private List<String> drops;
    private boolean error;

    private Item(String name, String type, String level, String description, String effects, String skinURL, String url,
                 String caracteristics, String conditions, String panoplie, String panoplieURL, String recipe,
                 List<String> drops, boolean error) {
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
        this.drops = drops;
        this.error = error;
    }

    @Override
    public void decorateEmbedObject(EmbedCreateSpec spec, Language lg) {
        spec.setTitle(name)
            .setUrl(url)
            .setThumbnail(skinURL);

        if (level != null && ! level.isEmpty())
            spec.addField(Translator.getLabel(lg, "item.niveau"), level, true);
        spec.addField(Translator.getLabel(lg, "item.type"), type, true);

        if (effects != null && ! effects.isEmpty())
        spec.addField(Translator.getLabel(lg, "item.effets"), effects, true);

        if (caracteristics != null && ! caracteristics.isEmpty())
            spec.addField(Translator.getLabel(lg, "item.caracteristiques"), caracteristics, true);

        if (conditions != null && ! conditions.isEmpty())
            spec.addField(Translator.getLabel(lg, "item.conditions"), conditions, true);

        if (panoplie != null && panoplieURL != null)
            spec.addField(Translator.getLabel(lg, "item.panoplie"), "[" + panoplie + "](" + panoplieURL + ")", true);
    }

    @Override
    public void decorateMoreEmbedObject(EmbedCreateSpec spec, Language lg) {
        spec.setTitle(name)
            .setUrl(url)
            .setImage(skinURL);

        if (description != null && ! description.isEmpty())
            spec.setDescription(description);

        if (level != null && ! level.isEmpty())
            spec.addField(Translator.getLabel(lg, "item.niveau"), level, true);
        spec.addField(Translator.getLabel(lg, "item.type"), type, true);

        if (effects != null && ! effects.isEmpty())
            spec.addField(Translator.getLabel(lg, "item.effets"), effects, true);

        if (caracteristics != null && ! caracteristics.isEmpty())
            spec.addField(Translator.getLabel(lg, "item.caracteristiques"), caracteristics, true);

        if (conditions != null && ! conditions.isEmpty())
            spec.addField(Translator.getLabel(lg, "item.conditions"), conditions, true);

        if (panoplie != null && panoplieURL != null)
            spec.addField(Translator.getLabel(lg, "item.panoplie"), "[" + panoplie + "](" + panoplieURL + ")", true);

        if (recipe != null)
            spec.addField(Translator.getLabel(lg, "item.recette"), recipe, true);

        if (! drops.isEmpty())
            for(int i = 0 ; i < drops.size(); i++)
                spec.addField(Translator.getLabel(lg, "resource.drops")
                                + (drops.size() >1? " (" + (i + 1) + "/" + drops.size() + ")" : "") + " : ",
                        drops.get(i), true);

        if (error) spec.setFooter(Translator.getLabel(lg, "monster.error"), "");
    }

    public static Item getItem(Language lg, String url) throws IOException {
        Document doc = JSoupManager.getDocument(url);
        doc.setBaseUri(url);
        String name = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-encyclo-detail-level").first().text()
                .replaceAll(Translator.getLabel(lg, "item.extract.level") + " ", "");
        String type = doc.getElementsByClass("ak-encyclo-detail-type").last().children().last().text();

        String skinURL = doc.getElementsByClass("ak-encyclo-detail-illu").first()
                .getElementsByTag("img").first().attr("src");

        String description = null;
        String effects = null;
        String caracteristics = null;
        String conditions = null;
        String set = null;
        String setURL = null;
        String recipe = null;
        List<String> drops = new ArrayList<>();

        boolean error = false;

        Elements titles = doc.getElementsByClass("ak-panel-title");
        Elements lines;
        StringBuilder tmp;
        for (Element title : titles)
            if (title.text().equals(Translator.getLabel(lg, "item.extract.description")))
                description = title.parent().getElementsByClass("ak-panel-content").first().text();
            else if (title.text().equals(Translator.getLabel(lg, "item.extract.effets")))
                effects = extractStatsFromTitle(lg, title);
            else if (title.text().equals(Translator.getLabel(lg, "item.extract.caracteristiques")))
                caracteristics = extractLinesFromTitle(title);
            else if (title.text().equals(Translator.getLabel(lg, "item.extract.evolution_effects")))
                effects = extractEvolutionEffectsFromTitle(lg, url);
            else if (title.text().equals(Translator.getLabel(lg, "item.extract.conditions")))
                conditions = extractLinesFromTitle(title);
            else if (title.text().contains(Translator.getLabel(lg, "item.extract.panoplie"))) {
                set = title.getElementsByTag("a").first().text();
                setURL = title.getElementsByTag("a").first().attr("abs:href");
            } else if (title.text().equals(Translator.getLabel(lg, "item.extract.recette"))) {
                lines = title.parent().getElementsByClass("ak-column");
                tmp = new StringBuilder();
                for (Element line : lines)
                    tmp.append(line.getElementsByClass("ak-front").text()).append(" [")
                            .append(line.getElementsByClass("ak-title").first().text()).append("](")
                            .append(line.getElementsByClass("ak-title").first()
                                    .children().first().attr("abs:href")).append(")\n");
                recipe = tmp.toString();
            }
            else if (title.text().equals(Translator.getLabel(lg, "resource.extract.drops")))
                error = error || extractDrops(drops, title.parent().getElementsByClass("ak-container ak-content-list ak-displaymode-image-col").first());

        return new Item(name, type, level, description, effects, URLManager.abs(skinURL), url,
                caracteristics, conditions, set, setURL, recipe, drops, error);
    }

    private static String extractLinesFromTitle(Element title)
    {
        Elements lines = title.parent().getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(line.text()).append("\n");
        return tmp.toString();
    }

    private static String extractEvolutionEffectsFromTitle(Language lg, String url) throws IOException
    {
        Map<String, String> header = new HashMap<>();
        header.put("X-PJAX", "true");
        header.put("X-PJAX-Container", ".ak-item-details-container");
        header.put("X-Requested-With", "XMLHttpRequest");
        Map<String, String> data = new HashMap<>();
        data.put("level", "100");
        data.put("_pjax", ".ak-item-details-container");
        Document doc = JSoupManager.postDocument(url, header, data);
        Elements lines = doc.getElementsByClass("ak-list-element");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(EmojiManager.getEmojiForStat(lg, line.text())).append(line.text()).append("\n");
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
     *
     * @param drops Liste des drops
     * @param element Element contenant les drops
     * @return true si c'est en erreur, false le cas échéant
     */
    private static boolean extractDrops(List<String> drops, Element element) {
        boolean error = false;
        StringBuilder field = new StringBuilder();
        Elements lines = element.getElementsByClass("ak-column");
        for (Element line : lines) {
            StringBuilder tmp = new StringBuilder();
            tmp.append(line.getElementsByClass("ak-front").text());

            Elements titles = line.getElementsByClass("ak-title");
            if (!titles.isEmpty()) {
                if (!titles.first().children().isEmpty())
                    tmp.append("[").append(titles.first().text()).append("](")
                            .append(titles.first().children().first().attr("abs:href")).append(") ");
                else
                    tmp.append(titles.first().text()).append(" ");

                tmp.append(line.getElementsByClass("ak-aside").first().text()).append("\n");

                if (field.length() + tmp.length() > Embed.Field.MAX_VALUE_LENGTH) {
                    drops.add(field.toString());
                    field.setLength(0);
                }
                field.append(tmp.toString());
            }
            else error = true;
        }

        if (field.length() > 0)
            drops.add(field.toString());

        return error;
    }
}


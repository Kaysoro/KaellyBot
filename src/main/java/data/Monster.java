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
    private boolean error;

    private Monster(String name, String family, String level, String caracteristics, String skinURL, String url,
                     String resistances, String zones, List<String> butins, List<String> butinsConditionne, boolean error) {
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
        this.error = error;
    }

    @Override
    public EmbedCreateSpec decorateEmbedObject(Language lg) {
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .title(name)
                .url(url)
                .thumbnail(skinURL);

        if (level != null && ! level.isEmpty())
            builder.addField(Translator.getLabel(lg, "monster.level"), level, true);
        builder.addField(Translator.getLabel(lg, "monster.race"), family, true);

        if (caracteristics != null && ! caracteristics.isEmpty())
            builder.addField(Translator.getLabel(lg, "monster.caracteristic"), caracteristics, true);

        if (resistances != null && ! resistances.isEmpty())
            builder.addField(Translator.getLabel(lg, "monster.resistance"), resistances, true);

        if (error) builder.footer(Translator.getLabel(lg, "monster.error"), null);
        return builder.build();
    }

    @Override
    public EmbedCreateSpec decorateMoreEmbedObject(Language lg) {
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .title(name)
                .url(url)
                .image(skinURL);

        if (level != null && ! level.isEmpty())
            builder.addField(Translator.getLabel(lg, "monster.level"), level, true);
        builder.addField(Translator.getLabel(lg, "monster.race"), family, true);

        if (caracteristics != null && ! caracteristics.isEmpty())
            builder.addField(Translator.getLabel(lg, "monster.caracteristic"), caracteristics, true);

        if (resistances != null && ! resistances.isEmpty())
            builder.addField(Translator.getLabel(lg, "monster.resistance"), resistances, true);

        if (zones != null && ! zones.isEmpty())
            builder.addField(Translator.getLabel(lg, "monster.zones"), zones, true);

        if (! butins.isEmpty())
            for(int i = 0; i < butins.size(); i++)
                builder.addField(Translator.getLabel(lg, "monster.butins")
                                + (butins.size() > 1? " (" + (i + 1) + "/" + butins.size() + ")" : "") + " : ",
                        butins.get(i), true);

        if (! butinsConditionne.isEmpty())
            for(int i = 0; i < butinsConditionne.size(); i++)
                builder.addField(Translator.getLabel(lg, "monster.butins_conditionnes")
                                + (butinsConditionne.size() > 1? " (" + (i + 1) + "/"
                                + butinsConditionne.size() + ")" : "") + " : ",
                        butinsConditionne.get(i), true);

        if (error) builder.footer(Translator.getLabel(lg, "monster.error"), null);
        return builder.build();
    }

    public static Monster getMonster(Language lg, String url) throws IOException {
        Document doc = JSoupManager.getDocument(url);

        String name = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-encyclo-detail-level").first().text()
                .replaceAll(Translator.getLabel(lg, "monster.extract.level") + " ", "");
        String family = doc.getElementsByClass("ak-encyclo-detail-type").last().children().last().text();

        Element element = doc.getElementsByClass("ak-encyclo-detail-illu").first().getElementsByTag("img").first();
        String skinURL = element.attr("data-src");

        String caracteristics = null;
        String resistances = null;
        String zones = null;
        List<String> butins = new ArrayList<>();
        List<String> butinsConditionne = new ArrayList<>();

        boolean error = false;

        Elements titles = doc.getElementsByClass("ak-panel-title");
        for (Element title : titles)
            if (title.text().equals(Translator.getLabel(lg, "monster.extract.caracteristic")))
                caracteristics = extractStatsFromTitle(lg, title);
            else if (title.text().equals(Translator.getLabel(lg, "monster.extract.resistance")))
                resistances = extractStatsFromTitle(lg, title);
            else if (title.text().equals(Translator.getLabel(lg, "monster.extract.zones")))
                zones = title.parent().children().last().text();
            else if (title.text().equals(Translator.getLabel(lg, "monster.extract.butins")))
                error = error || extractButins(butins, title.parent()
                        .getElementById("ak-encyclo-monster-drops ak-container ak-content-list"));
            else if (title.text().equals(Translator.getLabel(lg, "monster.extract.butins_conditionnes")))
                error = error || extractButins(butinsConditionne, title.parent());

        return new Monster(name, family, level, caracteristics, URLManager.abs(skinURL), url, resistances, zones, butins, butinsConditionne, error);
    }

    private static String extractStatsFromTitle(Language lg, Element elem)
    {
        Elements lines = elem.parent().getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(EmojiManager.getEmojiForStat(lg, line.text())).append(line.text()).append("\n");
        return tmp.toString();
    }

    /**
     * @param butins Liste des butins
     * @param element Element contenant les butins
     * @return true si c'est en erreur, false le cas échéant
     */
    private static boolean extractButins(List<String> butins, Element element){
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

                tmp.append(line.getElementsByClass("ak-drop-percent").first().text()).append("\n");

                if (field.length() + tmp.length() > Embed.Field.MAX_VALUE_LENGTH) {
                    butins.add(field.toString());
                    field.setLength(0);
                }
                field.append(tmp.toString());
            }
            else error = true;
        }

        if (field.length() > 0)
            butins.add(field.toString());

        return error;
    }
}
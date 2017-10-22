package data;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;
import util.EmojiManager;
import util.JSoupManager;
import util.URLManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by steve on 14/07/2016.
 */
public class Set implements Embedded {

    private String name;
    private String level;
    private String skinURL;
    private String url;
    private List<String> bonusTotal;
    private String composition;
    private String[] bonusPano;
    private List<String> recipeTotal;

    private Set(String name, String level, List<String> bonusTotal, String skinURL, String url, String composition,
                String[] bonusPano, List<String> recipeTotal) {
        this.name = name;
        this.level = level;
        this.skinURL = skinURL;
        this.url = url;
        this.composition = composition;
        this.bonusPano = bonusPano;
        this.bonusTotal = bonusTotal;
        this.recipeTotal = recipeTotal;
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

        if (composition != null && ! composition.isEmpty())
            builder.appendField(":shopping_bags: Composition :", composition, true);

        for(int i = 0; i < bonusPano.length; i++)
            builder.appendField(":diamond_shape_with_a_dot_inside: Bonus avec " + (i + 2) + " items :", bonusPano[i], true);

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

        if (composition != null && ! composition.isEmpty())
            builder.appendField(":shopping_bags: Composition :", composition, true);

        if (bonusTotal != null && ! bonusTotal.isEmpty())
            for(int i = 0; i < bonusTotal.size(); i++)
                builder.appendField(":cyclone: Bonus total de la panoplie complète :" + (bonusTotal.size() > 1?
                            " (" + (i + 1) + "/" + bonusTotal.size() + ")" : "") + " : ",
                    bonusTotal.get(i), true);

        if (! recipeTotal.isEmpty())
            for(int i = 0; i < recipeTotal.size(); i++)
                builder.appendField(":moneybag: liste totale d'ingrédients :" + (recipeTotal.size() > 1?
                                " (" + (i + 1) + "/" + recipeTotal.size() + ")" : "") + " : ",
                        recipeTotal.get(i), true);

        for(int i = 0; i < bonusPano.length; i++)
            builder.appendField(":diamond_shape_with_a_dot_inside: Bonus avec " + (i + 2) + " items :", bonusPano[i], true);


        return builder.build();
    }

    public static Set getSet(String url) throws IOException {
        Document doc = JSoupManager.getDocument(url);

        String name = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-encyclo-detail-level").first().text().replaceAll("Niveau : ", "");

        Element element = doc.getElementsByClass("ak-encyclo-detail-illu").first().getElementsByTag("img").first();
        String skinURL = element.attr("src");

        List<String> bonusTotal = new ArrayList<>();
        String composition = null;
        String[] bonusPano = new String[doc.getElementsByClass("ak-item-list-preview").first().children().size() - 1];
        List<String> recipeTotal = new ArrayList<>();


        Elements titles = doc.getElementsByClass("ak-panel-title");
        for (Element title : titles)
            if (title.text().equals("Bonus total de la panoplie complète"))
                bonusTotal = extractStatsFromTitleToList(title.parent());
            else if (title.text().equals("Bonus de la panoplie")){
                for(int i = 0; i < bonusPano.length; i++)
                    bonusPano[i] = extractStatsFromTitle(title.parent().getElementsByClass("set-bonus-list set-bonus-" + (i + 2)).first());
            }
            else if (title.text().equals("Composition")){
                StringBuilder st = new StringBuilder();
                for(Element tr : title.parent().getElementsByTag("tr")){
                    Element a = tr.getElementsByClass("ak-set-composition-name").first().getElementsByTag("a").first();
                    st.append("[").append(a.text()).append("](").append(a.attr("abs:href")).append(") ")
                            .append(tr.getElementsByClass("ak-set-composition-level").first().text()).append("\n");
                }
                composition = st.toString();
            }
            else if (title.text().equals("liste totale d'ingrédients")){
                Elements lines = title.parent().getElementsByClass("ak-column");
                StringBuilder field = new StringBuilder();

                for (Element line : lines) {
                    StringBuilder tmp = new StringBuilder(line.getElementsByClass("ak-front").text()).append(" [")
                            .append(line.getElementsByClass("ak-title").first().text()).append("](")
                            .append(line.getElementsByClass("ak-title").first()
                                    .children().first().attr("abs:href")).append(")\n");

                    if (field.length() + tmp.length() > EmbedBuilder.FIELD_CONTENT_LIMIT){
                        recipeTotal.add(field.toString());
                        field.setLength(0);
                    }
                    else
                        field.append(tmp.toString());
                }
                if (field.length() > 0)
                    recipeTotal.add(field.toString());
            }

        return new Set(name, level, bonusTotal, URLManager.abs(skinURL), url, composition, bonusPano, recipeTotal);
    }

    private static String extractLinesFromTitle(Element elem)
    {
        Elements lines = elem.getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(line.text()).append("\n");
        return tmp.toString();
    }

    private static String extractStatsFromTitle(Element elem)
    {
        Elements lines = elem.getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines)
            tmp.append(EmojiManager.getEmojiForStat(line.text())).append(line.text()).append("\n");
        return tmp.toString();
    }

    private static List<String> extractStatsFromTitleToList(Element elem)
    {
        List<String> values = new ArrayList<>();
        Elements lines = elem.getElementsByClass("ak-title");
        StringBuilder tmp = new StringBuilder();
        for (Element line : lines) {
            String value = EmojiManager.getEmojiForStat(line.text()) + line.text() + "\n";
            if (value.length() + tmp.length() > EmbedBuilder.FIELD_CONTENT_LIMIT){
                values.add(tmp.toString());
                tmp.setLength(0);
            }
            else
                tmp.append(value);
        }
        if (tmp.length() > 0)
            values.add(tmp.toString());

        return values;
    }
}
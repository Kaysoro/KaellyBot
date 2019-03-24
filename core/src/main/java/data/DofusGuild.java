package data;

import enums.GuildRank;
import enums.Language;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;
import util.JSoupManager;
import util.Translator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by steve on 30/03/2018.
 */
public class DofusGuild implements Embedded {

    private final static Logger LOG = LoggerFactory.getLogger(DofusGuild.class);

    private String name;
    private String level;
    private String server;
    private String creationDate;
    private String membersSize;
    private String alliName;
    private String alliUrl;
    private String littleSkinURL;
    private String bigSkinURL;
    private String url;
    private List<String> mainMembers;

    private DofusGuild(String name, String level, String server,
                       String creationDate, String membersSize, String alliName, String alliUrl,
                       String littleSkinURL, String bigSkinURL, String url, List<String> mainMembers) {
        this.name = name;
        this.level = level;
        this.server = server;
        this.creationDate = creationDate;
        this.membersSize = membersSize;
        this.alliName = alliName;
        this.alliUrl = alliUrl;
        this.littleSkinURL = littleSkinURL;
        this.bigSkinURL = bigSkinURL;
        this.url = url;
        this.mainMembers = mainMembers;
    }

    @Override
    public EmbedObject getEmbedObject(Language lg){
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(name);
        builder.withUrl(url);
        builder.withDescription(Translator.getLabel(lg, "guild.desc"));

        builder.withColor(new Random().nextInt(16777216));
        builder.withThumbnail(littleSkinURL);
        builder.withImage(bigSkinURL);

        builder.appendField(Translator.getLabel(lg, "guild.level"), level, true);
        builder.appendField(Translator.getLabel(lg, "guild.server"), server, true);
        builder.appendField(Translator.getLabel(lg, "guild.creation_date"), creationDate, true);
        builder.appendField(Translator.getLabel(lg, "guild.members_size"), membersSize, true);

        if (alliName != null)
            builder.appendField(Translator.getLabel(lg, "guild.ally"), "[" + alliName + "](" + alliUrl + ")", true);

        if (! mainMembers.isEmpty())
            for(int i = 0; i < mainMembers.size(); i++)
                builder.appendField(Translator.getLabel(lg, "guild.main_members")
                                + (mainMembers.size() > 1? " (" + (i + 1) + "/"
                                + mainMembers.size() + ")" : "") + " : ",
                        mainMembers.get(i), true);

        return builder.build();
    }

    @Override
    public EmbedObject getMoreEmbedObject(Language lg) {
        return getEmbedObject(lg);
    }

    public static DofusGuild getDofusGuild(String url, Language lg) throws IOException {
        Document doc = JSoupManager.getDocument(url);
        String bigSkinURL = doc.getElementsByClass("ak-entitylook").first().attr("style");
        bigSkinURL = bigSkinURL.substring(bigSkinURL.indexOf("https://"), bigSkinURL.indexOf(")"));
        String littleSkinURL = doc.getElementsByClass("ak-emblem").get(0).toString();
        littleSkinURL = littleSkinURL.substring(littleSkinURL.indexOf("https://"), littleSkinURL.indexOf(")"));
        String name = doc.getElementsByClass("ak-return-link").first().text();
        String level = doc.getElementsByClass("ak-directories-level").first().text()
                .replace(Translator.getLabel(lg, "guild.extract.level"), "").trim();
        String server = doc.getElementsByClass("ak-directories-server-name").first().text();
        String creationDate = doc.getElementsByClass("ak-directories-creation-date").first().text()
                .replace(Translator.getLabel(lg, "guild.extract.creation_date"), "").trim();
        String membersSize = doc.getElementsByClass("ak-directories-breed").first().text()
                .replace(Translator.getLabel(lg, "guild.extract.members_size"), "").trim();
        List<String> mainMembers = new ArrayList<>();
        Elements elemMembers = doc.getElementsByClass("ak-guild-member");
        StringBuilder st = new StringBuilder();
        for(Element elem : elemMembers){
            String status = elem.getElementsByClass("ak-member-status").text();
            if (status.equals(GuildRank.LEADER.getLabel(lg)) || status.equals(GuildRank.SECOND.getLabel(lg))){
                String nameMember = elem.getElementsByClass("ak-member-name").text();
                String urlPagePerso = elem.attr("abs:href");
                String line = "[" + nameMember + "](" + urlPagePerso + ") *" + status + "*\n";
                if (st.length() + line.length() > EmbedBuilder.FIELD_CONTENT_LIMIT){
                    mainMembers.add(st.toString());
                    st.setLength(0);
                }
                st.append(line);
            }
        }
        if (st.length() > 0)
            mainMembers.add(st.toString());

        // Optional
        String alliName = null;
        String alliUrl = null;

        Elements elemAlli = doc.getElementsByClass("ak-character-alliances");
        if (!elemAlli.isEmpty()) {
            alliName = elemAlli.last().select("a").text();
            alliUrl = elemAlli.last().select("a").attr("abs:href");
        }

        return new DofusGuild(name, level, server, creationDate, membersSize, alliName, alliUrl, littleSkinURL,
                bigSkinURL, url, mainMembers);
    }
}

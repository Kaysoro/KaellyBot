package data;

import discord4j.core.object.Embed;
import discord4j.core.spec.EmbedCreateSpec;
import enums.Language;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JSoupManager;
import util.Translator;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 30/03/2018.
 */
public class Alliance implements Embedded {

    private final static Logger LOG = LoggerFactory.getLogger(Alliance.class);

    private String name;
    private String server;
    private String creationDate;
    private String membersSize;
    private String guildsSize;
    private String littleSkinURL;
    private String bigSkinURL;
    private String url;
    private List<String> few_members;

    private Alliance(String name, String server, String creationDate, String membersSize, String guildsSize,
                     String littleSkinURL, String bigSkinURL, String url, List<String> few_members) {
        this.name = name;
        this.server = server;
        this.creationDate = creationDate;
        this.membersSize = membersSize;
        this.guildsSize = guildsSize;
        this.littleSkinURL = littleSkinURL;
        this.bigSkinURL = bigSkinURL;
        this.url = url;
        this.few_members = few_members;
    }

    @Override
    public void decorateEmbedObject(EmbedCreateSpec spec, Language lg){
        spec.setTitle(name)
            .setUrl(url)
            .setDescription(Translator.getLabel(lg, "alliance.desc"))
            .setThumbnail(littleSkinURL)
            .setImage(bigSkinURL)

            .addField(Translator.getLabel(lg, "alliance.server"), server, true)
            .addField(Translator.getLabel(lg, "alliance.creation_date"), creationDate, true)
            .addField(Translator.getLabel(lg, "alliance.guilds_size"), guildsSize, true)
            .addField(Translator.getLabel(lg, "alliance.members_size"), membersSize, true);

        if (! few_members.isEmpty())
            for(int i = 0; i < few_members.size(); i++)
                spec.addField(Translator.getLabel(lg, "alliance.few_members")
                                + (few_members.size() > 1? " (" + (i + 1) + "/"
                                + few_members.size() + ")" : "") + " : ",
                        few_members.get(i), true);
    }

    @Override
    public void decorateMoreEmbedObject(EmbedCreateSpec spec, Language lg) {
        decorateEmbedObject(spec, lg);
    }

    public static Alliance getAlliance(String url, Language lg) throws IOException {
        Document doc = JSoupManager.getDocument(url);
        String bigSkinURL = doc.getElementsByClass("ak-entitylook").first().attr("style");
        bigSkinURL = bigSkinURL.substring(bigSkinURL.indexOf("https://"), bigSkinURL.indexOf(")"));
        String littleSkinURL = doc.getElementsByClass("ak-emblem").get(0).toString();
        littleSkinURL = littleSkinURL.substring(littleSkinURL.indexOf("https://"), littleSkinURL.indexOf(")"));
        String name = doc.getElementsByClass("ak-return-link").first().text();
        String server = doc.getElementsByClass("ak-directories-server-name").first().text();
        String creationDate = doc.getElementsByClass("ak-directories-creation-date").first().text()
                .replace(Translator.getLabel(lg, "alliance.extract.creation_date"), "").trim();
        String guildsSize = doc.getElementsByClass("ak-directories-breed").first().text()
                .replace(Translator.getLabel(lg, "alliance.extract.guilds_size"), "").trim();
        String membersSize = doc.getElementsByClass("ak-directories-breed").last().text()
                .replace(Translator.getLabel(lg, "alliance.extract.members_size"), "").trim();
        List<String> mainMembers = new ArrayList<>();
        Elements elemMembers = doc.getElementsByClass("ak-alliance-member");
        StringBuilder st = new StringBuilder();
        for(Element elem : elemMembers){
            String nameMember = elem.getElementsByClass("ak-member-name").text();
            String levelMember = elem.getElementsByClass("ak-member-level").text();
            String numberMember = elem.getElementsByClass("ak-member-status").text();
            String urlPagePerso = elem.attr("abs:href");
            String line = "[" + nameMember + "](" + urlPagePerso + ") *" + levelMember
                    + ", " + numberMember + "*\n";
            if (st.length() + line.length() > Embed.Field.MAX_VALUE_LENGTH){
                mainMembers.add(st.toString());
                st.setLength(0);
            }
            st.append(line);
        }
        if (st.length() > 0)
            mainMembers.add(st.toString());

        return new Alliance(name, server, creationDate, membersSize, guildsSize, littleSkinURL,
                bigSkinURL, url, mainMembers);
    }
}

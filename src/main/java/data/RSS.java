package data;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.*;
import enums.Language;
import exceptions.ExceptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reporter;
import util.Translator;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 12/01/2017.
 */
public class RSS implements Comparable<RSS>, Embedded {

    private final static Logger LOG = LoggerFactory.getLogger(RSS.class);
    private final static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy à HH:mm", Locale.FRANCE);

    private String title;
    private String url;
    private String imageUrl;
    private long date;

    private RSS(String title, String url, String imageUrl, long date) {
        this.title = title;
        this.url = url;
        if (imageUrl != null && !imageUrl.isEmpty())
            this.imageUrl = imageUrl;
        else
            this.imageUrl = Constants.officialLogo;
        this.date = date;
    }

    public static List<RSS> getRSSFeeds(Language lg){
        List<RSS> rss = new ArrayList<>();

        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(Translator.getLabel(lg, "game.url")
                    + Translator.getLabel(lg, "feed.url"))));

            for(SyndEntry entry : feed.getEntries()) {
                Matcher m = Pattern.compile("<img.+src=\"(.*\\.jpg)\".+>").matcher(entry.getDescription().getValue());
                rss.add(new RSS(entry.getTitle(), entry.getLink(), (m.find()? m.group(1) : null),
                        entry.getPublishedDate().getTime()));
            }
        } catch (FeedException e){
            Reporter.report(e);
            LOG.error("getRSSFeeds", e);
        } catch(IOException e){
            ExceptionManager.manageSilentlyIOException(e);
        } catch(Exception e){
            ExceptionManager.manageSilentlyException(e);
        }
        Collections.sort(rss);
        return rss;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public long getDate() {
        return date;
    }

    public String toStringDiscord(){
        return getTitle() + " (" + dateFormat.format(new Date(getDate())) + ")\n" + getUrl();
    }

    @Override
    public String toString(){
        return getTitle() + " (" + dateFormat.format(new Date(getDate())) + ")\n";
    }

    @Override
    public int compareTo(RSS o) {
        return (int) (getDate() - o.getDate());
    }

    @Override
    public EmbedCreateSpec decorateEmbedObject(Language lg) {
        return EmbedCreateSpec.builder()
                .author("Dofus.com", getUrl(), null)
                .title(getTitle())
                .image(imageUrl)
                .thumbnail(Constants.rssIcon)
                .footer(dateFormat.format(new Date(getDate())), null)
                .build();
    }

    public EmbedData decorateRestEmbedObject(Language lg) {
        return EmbedData.builder()
                .author(EmbedAuthorData.builder().name("Dofus.com").url(getUrl()).build())
                .title(getTitle())
                .image(EmbedImageData.builder().url(imageUrl).build())
                .thumbnail(EmbedThumbnailData.builder().url(Constants.rssIcon).build())
                .footer(EmbedFooterData.builder().text(dateFormat.format(new Date(getDate()))).build())
                .build();
    }

    @Override
    public EmbedCreateSpec decorateMoreEmbedObject(Language lg) {
        return decorateEmbedObject(lg);
    }
}

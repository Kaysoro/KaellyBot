package data;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import enums.Language;
import exceptions.ExceptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;
import util.Reporter;
import util.Translator;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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
    public EmbedObject getEmbedObject(Language lg) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withAuthorName("Dofus.com");
        builder.withAuthorUrl(getUrl());

        builder.withTitle(getTitle());
        builder.withColor(16747520);
        builder.withImage(imageUrl);
        builder.withThumbnail(Constants.rssIcon);
        builder.withFooterText(dateFormat.format(new Date(getDate())));

        return builder.build();
    }

    @Override
    public EmbedObject getMoreEmbedObject(Language lg) {
        return getEmbedObject(lg);
    }
}

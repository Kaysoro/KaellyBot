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
import lombok.Getter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JSoupManager;
import util.Reporter;
import util.Translator;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 12/01/2017.
 */
public class RSS implements Comparable<RSS>, Embedded {

    private static final Logger LOG = LoggerFactory.getLogger(RSS.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy à HH:mm", Locale.FRANCE);

    @Getter
    private final String title;
    @Getter
    private final String url;
    private final String imageUrl;
    @Getter
    private final long date;

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

        try (CloseableHttpClient client = HttpClients.custom()
                .setUserAgent(JSoupManager.USER_AGENT)
                .build()) {
            String gameUrl = Translator.getLabel(lg, "game.url");
            String feedUrl = Translator.getLabel(lg, "feed.url");
            HttpUriRequest request = new HttpGet(gameUrl + feedUrl);

            try (CloseableHttpResponse response = client.execute(request);
                 InputStream stream = response.getEntity().getContent()) {
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(stream));

                for(SyndEntry entry : feed.getEntries()) {
                    Matcher m = Pattern.compile("<img.+src=\"(.*\\.jpg)\".+>").matcher(entry.getDescription().getValue());
                    rss.add(new RSS(entry.getTitle(), entry.getLink(), (m.find()? m.group(1) : null),
                            entry.getPublishedDate().getTime()));
                }
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

    @Override
    public String toString(){
        return getTitle() + " (" + DATE_FORMAT.format(new Date(getDate())) + ")\n";
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
                .footer(DATE_FORMAT.format(new Date(getDate())), null)
                .build();
    }

    public EmbedData decorateRestEmbedObject() {
        return EmbedData.builder()
                .author(EmbedAuthorData.builder().name("DofusTouch.com").url(getUrl()).build())
                .title(getTitle())
                .image(EmbedImageData.builder().url(imageUrl).build())
                .thumbnail(EmbedThumbnailData.builder().url(Constants.rssIcon).build())
                .footer(EmbedFooterData.builder().text(DATE_FORMAT.format(new Date(getDate()))).build())
                .build();
    }

    @Override
    public EmbedCreateSpec decorateMoreEmbedObject(Language lg) {
        return decorateEmbedObject(lg);
    }
}

package external;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import data.RSS;
import enums.Language;
import exceptions.ExceptionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RSSAPI {

    private static final Logger LOG = LoggerFactory.getLogger(RSSAPI.class);

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
}

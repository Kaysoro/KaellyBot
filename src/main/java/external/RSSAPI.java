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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reporter;
import util.Translator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RSSAPI {

    private final static Logger LOG = LoggerFactory.getLogger(RSSAPI.class);

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
}

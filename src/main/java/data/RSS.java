package data;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import exceptions.ExceptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by steve on 12/01/2017.
 */
public class RSS implements Comparable<RSS>{
    private final static Logger LOG = LoggerFactory.getLogger(RSS.class);
    private final static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy à HH:mm", Locale.FRANCE);

    private String title;
    private String url;
    private long date;

    private RSS(String title, String url, long date) {
        this.title = title;
        this.url = url;
        this.date = date;
    }

    public static List<RSS> getRSSFeeds(){
        List<RSS> rss = new ArrayList<>();

        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(Constants.officialURL + Constants.feedURL)));

            for(SyndEntry entry : feed.getEntries())
                rss.add(new RSS(entry.getTitle(), entry.getLink(), entry.getPublishedDate().getTime()));
        } catch (FeedException e){
            ClientConfig.setSentryContext(null,null, null, null);
            LOG.error(e.getMessage());
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
        StringBuilder st = new StringBuilder(getTitle()).append(" (")
                .append(dateFormat.format(new Date(getDate()))).append(")\n")
                .append(getUrl());
        return st.toString();
    }

    @Override
    public String toString(){
        StringBuilder st = new StringBuilder(getTitle()).append(" (")
                .append(dateFormat.format(new Date(getDate()))).append(")\n");
        return st.toString();
    }

    @Override
    public int compareTo(RSS o) {
        return (int) (getDate() - o.getDate());
    }
}

package data;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import exceptions.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            Reporter.report(e);
            LOG.error(e.getMessage());
        } catch(IOException e){
            // First we try parsing the exception message to see if it contains the response code
            Matcher exMsgStatusCodeMatcher = Pattern.compile("^Server returned HTTP response code: (\\d+)")
                    .matcher(e.getMessage());
            if(exMsgStatusCodeMatcher.find()) {
                int statusCode = Integer.parseInt(exMsgStatusCodeMatcher.group(1));
                if (statusCode >= 500 && statusCode < 600)
                    LOG.warn(e.getMessage());
                else {
                    Reporter.report(e);
                    LOG.error(e.getMessage());
                }
            } else {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
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

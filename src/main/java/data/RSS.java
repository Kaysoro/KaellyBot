package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        //TODO

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
    public int compareTo(RSS o) {
        return (int) (getDate() - o.getDate());
    }
}

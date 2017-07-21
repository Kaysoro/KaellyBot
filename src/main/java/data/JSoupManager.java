package data;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by steve on 21/07/2017.
 */
public class JSoupManager {

    public static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0")
                .referrer("http://www.google.com")
                .timeout(10000)
                .get();
    }

    public static Connection.Response getResponse(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0")
                .referrer("http://www.google.com")
                .timeout(10000)
                .execute();
    }
}

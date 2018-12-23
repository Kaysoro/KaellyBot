package util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

/**
 * Created by steve on 21/07/2017.
 */
public class JSoupManager {

    public static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0")
                .referrer("http://www.google.com")
                .timeout(10000)
                .validateTLSCertificates(false)
                .get();
    }

    public static Document postDocument(String url, Map<String, String> header, Map<String, String> data) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0")
                .referrer("http://www.google.com")
                .headers(header)
                .data(data)
                .timeout(10000)
                .validateTLSCertificates(false)
                .post();
    }

    public static Connection.Response getResponse(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0")
                .referrer("http://www.google.com")
                .timeout(10000)
                .validateTLSCertificates(false)
                .execute();
    }
}

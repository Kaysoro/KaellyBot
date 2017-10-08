package util;

import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;

public class URLManager {

    public static String abs(String urlPath) {
        URL url = null;
        if (urlPath.contains("/../"))
            try {
                url = new URL(urlPath);
                Deque<String> urlDeque = new ArrayDeque<>();
                urlDeque.add(url.getProtocol() + "://" + url.getHost());
                String[] parts = url.getPath().split("/");

                for (String part : parts)
                    if (! part.isEmpty())
                        if (part.equals(".."))
                            urlDeque.removeLast();
                        else
                            urlDeque.addLast(part);

                StringBuilder st = new StringBuilder();
                while (!urlDeque.isEmpty())
                    st.append(urlDeque.removeFirst()).append("/");
                st.setLength(st.length() - 1);

                return st.toString();
            } catch(MalformedURLException e){
                LoggerFactory.getLogger(URLManager.class).warn(URLManager.class.getName(), e);
                return null;
            }
        return urlPath;
    }
}

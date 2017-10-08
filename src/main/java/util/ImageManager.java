package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageManager {

    private static final long PEREMPTION = 2592000000L; // 1 mois
    private static final Logger LOG = LoggerFactory.getLogger(ImageManager.class);
    private static ImageManager manager;

    public static String manageImage(String name, String url){
        String fileUrl = System.getProperty("user.dir") + File.separator + "images"
                + File.separator + name + ".png";

        File fic = new File(fileUrl);
        if (! fic.exists() || System.currentTimeMillis() - fic.lastModified() > PEREMPTION)
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                conn.addRequestProperty("Referer", "google.com");
                conn.setInstanceFollowRedirects(true);
                HttpURLConnection.setFollowRedirects(true);
                InputStream in = conn.getInputStream();
                Files.copy(in, Paths.get(fileUrl));
            } catch(Exception e){
                LOG.error("ImageManager", e);
                return null;
            }
        return fileUrl;
    }
}

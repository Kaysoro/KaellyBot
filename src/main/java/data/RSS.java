package data;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.*;
import enums.Language;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by steve on 12/01/2017.
 */
public class RSS implements Comparable<RSS>, Embedded {

    private final static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy à HH:mm", Locale.FRANCE);

    private String title;
    private String url;
    private String imageUrl;
    private long date;

    public RSS(String title, String url, String imageUrl, long date) {
        this.title = title;
        this.url = url;
        if (imageUrl != null && !imageUrl.isEmpty())
            this.imageUrl = imageUrl;
        else
            this.imageUrl = Constants.officialLogo;
        this.date = date;
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
        return getTitle() + " (" + dateFormat.format(new Date(getDate())) + ")\n" + getUrl();
    }

    @Override
    public String toString(){
        return getTitle() + " (" + dateFormat.format(new Date(getDate())) + ")\n";
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
                .footer(dateFormat.format(new Date(getDate())), null)
                .build();
    }

    public EmbedData decorateRestEmbedObject(Language lg) {
        return EmbedData.builder()
                .author(EmbedAuthorData.builder().name("Dofus.com").url(getUrl()).build())
                .title(getTitle())
                .image(EmbedImageData.builder().url(imageUrl).build())
                .thumbnail(EmbedThumbnailData.builder().url(Constants.rssIcon).build())
                .footer(EmbedFooterData.builder().text(dateFormat.format(new Date(getDate()))).build())
                .build();
    }

    @Override
    public EmbedCreateSpec decorateMoreEmbedObject(Language lg) {
        return decorateEmbedObject(lg);
    }
}

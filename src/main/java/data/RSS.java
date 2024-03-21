package data;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.*;
import enums.Language;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by steve on 12/01/2017.
 */
@Getter
public class RSS implements Comparable<RSS>, Embedded {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy à HH:mm", Locale.FRANCE);

    private final String title;
    private final String url;
    private final String imageUrl;
    private final long date;

    public RSS(String title, String url, String imageUrl, long date) {
        this.title = title;
        this.url = url;
        if (imageUrl != null && !imageUrl.isEmpty())
            this.imageUrl = imageUrl;
        else
            this.imageUrl = Constants.officialLogo;
        this.date = date;
    }

    @Override
    public String toString(){
        return getTitle() + " (" + DATE_FORMAT.format(new Date(getDate())) + ")\n";
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
                .footer(DATE_FORMAT.format(new Date(getDate())), null)
                .build();
    }

    public EmbedData decorateRestEmbedObject() {
        return EmbedData.builder()
                .title(getTitle())
                .author(EmbedAuthorData.builder().name("Dofus.com").urlOrNull(getUrl()).build())
                .image(EmbedImageData.builder().url(imageUrl).build())
                .thumbnail(EmbedThumbnailData.builder().url(Constants.rssIcon).build())
                .footer(EmbedFooterData.builder().text(DATE_FORMAT.format(new Date(getDate()))).build())
                .build();
    }

    @Override
    public EmbedCreateSpec decorateMoreEmbedObject(Language lg) {
        return decorateEmbedObject(lg);
    }
}

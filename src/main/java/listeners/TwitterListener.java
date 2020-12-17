package listeners;

import discord4j.common.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.EmbedAuthorData;
import discord4j.discordjson.json.EmbedData;
import discord4j.discordjson.json.EmbedImageData;
import discord4j.discordjson.json.EmbedThumbnailData;
import discord4j.rest.entity.RestChannel;
import discord4j.rest.http.client.ClientException;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import util.ClientConfig;
import data.Constants;
import finders.TwitterFinder;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import util.Translator;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TwitterListener extends StatusAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterListener.class);
    private Map<Long, Language> twitterIDs;

    public TwitterListener(){
        super();
        twitterIDs = new HashMap<>();
        for(Language lg : Language.values())
            twitterIDs.put(Long.parseLong(Translator.getLabel(lg, "twitter.id")), lg);
    }

    @Override
    public void onStatus(Status status) {
        Language language = twitterIDs.get(status.getUser().getId());
        if (twitterIDs.containsKey(status.getUser().getId()) && (status.getInReplyToScreenName() == null
                || twitterIDs.containsKey(status.getInReplyToUserId())))
            for (TwitterFinder twitterFinder : TwitterFinder.getTwitterChannels().values())
                try {
                    RestChannel channel = ClientConfig.DISCORD().getChannelById(Snowflake.of(twitterFinder.getChannelId()));
                    if (Translator.getLanguageFrom(channel).equals(language)){
                        channel.createMessage(createEmbedFor(status))
                                .doOnError(error -> {
                                    if (error instanceof ClientException){
                                        LOG.warn("TwitterFinder: no access on " + twitterFinder.getChannelId());
                                        twitterFinder.removeToDatabase();
                                    }
                                    else LOG.error("onStatus", error);
                                })
                                .subscribe();
                    }
                } catch(ClientException e){
                    LOG.warn("TwitterFinder: no access on " + twitterFinder.getChannelId());
                    twitterFinder.removeToDatabase();
                } catch(Exception e){
                    LOG.error("onStatus", e);
                }
    }

    private EmbedData createEmbedFor(Status status){
        return EmbedData.builder()
                .author(EmbedAuthorData.builder()
                        .name("@" + status.getUser().getScreenName())
                        .url("https://twitter.com/")
                        .iconUrl(status.getUser().getMiniProfileImageURL()).build())
                .title("Tweet")
                .url("https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId())
                .image(status.getMediaEntities().length > 0 ? EmbedImageData.builder().url(status.getMediaEntities()[0].getMediaURL()).build() : EmbedImageData.builder().build())
                .description(status.getText())
                .thumbnail(EmbedThumbnailData.builder().url(Constants.twitterIcon).build())
                .build();
    }
}

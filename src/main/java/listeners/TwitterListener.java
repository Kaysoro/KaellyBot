package listeners;

import discord4j.common.util.Snowflake;
import discord4j.discordjson.json.EmbedAuthorData;
import discord4j.discordjson.json.EmbedData;
import discord4j.discordjson.json.EmbedImageData;
import discord4j.discordjson.json.EmbedThumbnailData;
import discord4j.rest.entity.RestChannel;
import discord4j.rest.http.client.ClientException;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import util.ClientConfig;
import data.Constants;
import finders.TwitterFinder;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import util.Translator;
import util.twitter.Tweet;
import util.twitter.TwitterResponse;
import util.twitter.TwitterStreamListener;
import util.twitter.User;

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
    public void onStatus(TwitterResponse status) {
        Language language = twitterIDs.get(Long.parseLong(status.getAuthor().getId()));
        for (TwitterFinder twitterFinder : TwitterFinder.getTwitterChannels().values()) {
            try {
                RestChannel channel = ClientConfig.DISCORD().getChannelById(Snowflake.of(twitterFinder.getChannelId()));
                if (Translator.getLanguageFrom(channel).equals(language)) {
                    channel.createMessage(status.getTweets().get(0).getUrl())
                            .doOnError(error -> {
                                if (error instanceof ClientException) {
                                    LOG.warn("TwitterFinder: no access on " + twitterFinder.getChannelId());
                                    twitterFinder.removeToDatabase();
                                } else LOG.error("onStatus", error);
                            })
                            .subscribe();
                }
            } catch (ClientException e) {
                LOG.warn("TwitterFinder: no access on " + twitterFinder.getChannelId());
                twitterFinder.removeToDatabase();
            } catch (Exception e) {
                LOG.error("onStatus", e);
            }
        }
    }
 }

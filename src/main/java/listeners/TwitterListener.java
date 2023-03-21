package listeners;

import discord4j.common.util.Snowflake;
import discord4j.discordjson.json.*;
import discord4j.rest.entity.RestChannel;
import discord4j.rest.http.client.ClientException;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientConfig;
import data.Constants;
import finders.TwitterFinder;
import util.Translator;
import util.twitter.payload.Entities;
import util.twitter.payload.TweetData;
import util.twitter.payload.TwitterResponse;
import util.twitter.TwitterStreamListener;
import util.twitter.payload.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TwitterListener implements TwitterStreamListener {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterListener.class);
    private final Map<Long, Language> twitterIDs;

    public TwitterListener(){
        super();
        twitterIDs = new HashMap<>();
        for(Language lg : Language.values())
            twitterIDs.put(Long.parseLong(Translator.getLabel(lg, "twitter.id")), lg);
    }

    @Override
    public void onStatus(TwitterResponse status) {
        status.getData().stream()
                .findFirst()
                .ifPresent(data -> status.getIncludes()
                        .getUsers().stream()
                        .filter(user -> user.getId().equals(data.getAuthorId()))
                        .findFirst()
                .ifPresent(author -> processTweet(data, author)));
    }

    private void processTweet(TweetData data, User author){
        Language language = twitterIDs.get(Long.parseLong(author.getId()));
        for (TwitterFinder twitterFinder : TwitterFinder.getTwitterChannels().values()) {
            try {
                RestChannel channel = ClientConfig.DISCORD().getChannelById(Snowflake.of(twitterFinder.getChannelId()));
                if (Translator.getLanguageFrom(channel).equals(language)) {
                    channel.createMessage(createEmbedFor(data, author))
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

    public Map<String, String> getRules(){
        StringBuilder st = new StringBuilder();
        if (Language.values().length > 0){
            st.append("(");
            boolean isFirstElem = true;
            for(Language lg : Language.values()){
                if (isFirstElem){
                    isFirstElem= false;
                } else {
                    st.append(" OR");
                }
                st.append(" from:").append(Translator.getLabel(lg, "twitter.id"));
            }
            st.append(")");
        }

        st.append(" -is:retweet -is:reply");

        return Map.of(st.toString(), "Tweets from Dofus accounts without retweets or replies");
    }

    private EmbedData createEmbedFor(TweetData data, User author){
        return EmbedData.builder()
                .author(EmbedAuthorData.builder()
                        .name("@" + author.getUsername())
                        .url("https://twitter.com/")
                        .iconUrl(author.getProfileImageUrl()).build())
                .title("Tweet")
                .url("https://twitter.com/" + author.getUsername() + "/status/" + data.getId())
                .image(Optional.ofNullable(data.getEntities())
                        .map(Entities::getMedia)
                        .orElse(Collections.emptyList())
                        .stream().findFirst()
                        .map(media -> EmbedImageData.builder().url(media.getUrl()).build())
                        .orElse(EmbedImageData.builder().build()))
                .description(data.getText())
                .thumbnail(EmbedThumbnailData.builder().url(Constants.twitterIcon).build())
                .build();
    }
}

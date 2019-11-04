package listeners;

import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
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
                    ClientConfig.DISCORD()
                            .flatMap(client -> client.getChannelById(Snowflake.of(twitterFinder.getChannelId()))).distinct()
                            .filter(chan -> chan instanceof MessageChannel)
                            .map(chan -> (MessageChannel) chan)
                            .filter(chan -> Translator.getLanguageFrom(chan).equals(language))
                            .flatMap(chan -> chan.createEmbed(spec -> createEmbedFor(spec, status)))
                    .subscribe();

                } catch(Exception e){
                    LOG.error("onStatus", e);
                }
    }

    private void createEmbedFor(EmbedCreateSpec spec, Status status){
        spec.setAuthor("@" + status.getUser().getScreenName(), "https://twitter.com/"
                + status.getUser().getScreenName(), status.getUser().getMiniProfileImageURL())

            .setTitle("Tweet")
            .setUrl("https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId())
            .setColor(new Color(29, 161, 242))
            .setDescription(status.getText())
            .setThumbnail(Constants.twitterIcon);

        if (status.getMediaEntities().length > 0) {
            MediaEntity media = status.getMediaEntities()[0];
            spec.setImage(media.getMediaURL());
        }
    }
}

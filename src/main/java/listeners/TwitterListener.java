package listeners;

import enums.Language;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import util.ClientConfig;
import data.Constants;
import finders.TwitterFinder;
import util.Message;
import sx.blah.discord.util.EmbedBuilder;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import util.Translator;

import java.util.HashMap;
import java.util.Map;

public class TwitterListener extends StatusAdapter {

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
            for (TwitterFinder twitterFinder : TwitterFinder.getTwitterChannels().values()) {
                IChannel chan = ClientConfig.DISCORD().getChannelByID(twitterFinder.getChannelId());

                if (TwitterFinder.getTwitterChannels().containsKey(twitterFinder.getChannelId())
                        && Translator.getLanguageFrom(chan).equals(language))
                    Message.sendEmbed(chan, createEmbedFor(status));
            }
    }

    public EmbedObject createEmbedFor(Status status){
        EmbedBuilder builder = new EmbedBuilder();

        builder.withAuthorName("@" + status.getUser().getScreenName());
        builder.withAuthorIcon(status.getUser().getMiniProfileImageURL());
        builder.withAuthorUrl("https://twitter.com/" + status.getUser().getScreenName());

        builder.withTitle("Tweet");
        builder.withUrl("https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId());
        builder.withColor(1942002);
        builder.withDescription(status.getText());
        builder.withThumbnail(Constants.twitterIcon);

        if (status.getMediaEntities().length > 0) {
            MediaEntity media = status.getMediaEntities()[0];
            builder.withImage(media.getMediaURL());
        }

        return builder.build();
    }
}

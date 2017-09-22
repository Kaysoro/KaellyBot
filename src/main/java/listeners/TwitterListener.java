package listeners;

import data.ClientConfig;
import data.Constants;
import data.TwitterFinder;
import discord.Message;
import sx.blah.discord.util.EmbedBuilder;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusAdapter;

public class TwitterListener extends StatusAdapter {

    @Override
    public void onStatus(Status status) {
        for (TwitterFinder twitterFinder : TwitterFinder.getTwitterChannels().values())
            // In case if channel didn't exist anymore and it is not removed at time
            if (TwitterFinder.getTwitterChannels().containsKey(twitterFinder.getChannelId())){
                if (status.getUser().getId() == Constants.dofusTwitter && status.getInReplyToScreenName() == null) {
                    EmbedBuilder builder = new EmbedBuilder();

                    builder.withAuthorName("@" + status.getUser().getScreenName());
                    builder.withAuthorIcon(status.getUser().getMiniProfileImageURL());
                    builder.withAuthorUrl("https://twitter.com/" + status.getUser().getScreenName());

                    builder.withTitle("Tweet");
                    builder.withUrl("https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId());
                    builder.withColor(1942002);
                    builder.withDescription(status.getText());
                    builder.withThumbnail(Constants.twitterIcon);

                    if(status.getMediaEntities().length > 0){
                        MediaEntity media = status.getMediaEntities()[0];
                        builder.withImage(media.getMediaURL());
                    }

                    Message.sendEmbed(ClientConfig.DISCORD().getChannelByID(twitterFinder.getChannelId()), builder.build());
                }
            }
    }
}

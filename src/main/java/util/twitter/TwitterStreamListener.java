package util.twitter;

import util.twitter.payload.TwitterResponse;

public interface TwitterStreamListener {

    void onStatus(TwitterResponse status);
}

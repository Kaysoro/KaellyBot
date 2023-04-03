package util.twitter;

public interface TwitterStreamListener {

    void onStatus(TwitterResponse status);
}

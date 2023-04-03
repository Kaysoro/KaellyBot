package util.twitter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.Language;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.SM;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StreamUtils;
import util.Translator;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwitterStream {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterStream.class);

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
    private final static long DELTA = 10; // 10min
    private final static String TWITTER_URL = "https://twitter.com";
    private final static String TWITTER_API_URL = "https://twitter.com/i/api/graphql/BeHK76TOCY3P8nO-FWocjA/UserTweets";
    private final static String COOKIE_GUEST_TOKEN = "gt";
    private final static String COOKIE_SEPARATOR = ";";
    private final static String COOKIE_VALUE_SEPARATOR = "=";

    private final static String HEADER_GUEST_TOKEN = "x-guest-token";
    private final static String VARIABLES = "variables";
    private final static String FEATURES = "features";
    private final static int TWEET_COUNT = 40;

    private final String bearerToken;
    private final List<TwitterStreamListener> listeners;
    private boolean isStarted;

    public TwitterStream(String bearerToken) {
        isStarted = false;
        this.bearerToken = bearerToken;
        listeners = new CopyOnWriteArrayList<>();
    }

    public void addListener(TwitterStreamListener listener) {
        listeners.add(listener);
    }

    public synchronized void startStream() {
        if (!isStarted) {
            isStarted = true;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(this::checkTweets, 0, DELTA, TimeUnit.MINUTES);
        }
    }

    private void checkTweets(){
        getGuestToken().ifPresent(guestToken -> Stream.of(Language.values())
                    .map(lg -> Translator.getLabel(lg, "twitter.id"))
                    .map(userId -> getUserTweets(bearerToken, guestToken, userId))
                    .flatMap(Optional::stream)
                    .forEach(tweet -> listeners.forEach(listener -> listener.onStatus(tweet))));
    }

    private Optional<String> getGuestToken() {
        try (CloseableHttpClient httpClient = HttpClients.createMinimal()) {
            HttpResponse response = httpClient.execute(new HttpHead(new URIBuilder(TWITTER_URL).build()));
            if (response.getStatusLine().getStatusCode() == Response.Status.OK.getStatusCode()) {
                return Stream.of(response.getHeaders(SM.SET_COOKIE))
                        .filter(cookie -> cookie.getValue().contains(COOKIE_GUEST_TOKEN))
                        .flatMap(cookie -> Stream.of(cookie.getValue().split(COOKIE_SEPARATOR)))
                        .filter(element -> element.startsWith(COOKIE_GUEST_TOKEN))
                        .map(element -> element.split(COOKIE_VALUE_SEPARATOR)[1])
                        .findFirst();
            } else {
            LOG.warn("Cannot consume twitter API, guest_token could not be retrieved: "
                    + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            LOG.error("Unknown error happened", e);
        }
        return Optional.empty();
    }

    private Optional<TwitterResponse> getUserTweets(String bearerToken, String guestToken, String userId) {
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build()).build()) {

            URI uri = new URIBuilder(TWITTER_API_URL)
                    .addParameter(VARIABLES, getVariables(userId))
                    .addParameter(FEATURES, getFeatures())
                    .build();

            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", bearerToken));
            httpGet.setHeader(HEADER_GUEST_TOKEN, guestToken);

            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == Response.Status.OK.getStatusCode()) {
                return Optional.ofNullable(response.getEntity())
                        .flatMap(this::castResponse);
            } else {
                LOG.warn("Cannot consume twitter API (userId=" + userId + "): " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            LOG.error("Unknown error happened", e);
        }

        return Optional.empty();
    }

    private String getVariables(String userId){
        return "{\"userId\":\"" + userId + "\""
                + ",\"count\":" + TWEET_COUNT
                + ",\"includePromotedContent\":false"
                + ",\"withVoice\":false"
                + ",\"withDownvotePerspective\":false"
                + ",\"withReactionsMetadata\":false"
                + ",\"withReactionsPerspective\":false}";
    }

    private String getFeatures(){
        return "{\"blue_business_profile_image_shape_enabled\":false"
                + ",\"responsive_web_graphql_exclude_directive_enabled\":false"
                + ",\"verified_phone_label_enabled\":false"
                + ",\"responsive_web_graphql_timeline_navigation_enabled\":false"
                + ",\"responsive_web_graphql_skip_user_profile_image_extensions_enabled\":false"
                + ",\"tweetypie_unmention_optimization_enabled\":false"
                + ",\"vibe_api_enabled\":false"
                + ",\"responsive_web_edit_tweet_api_enabled\":false"
                + ",\"graphql_is_translatable_rweb_tweet_is_translatable_enabled\":false"
                + ",\"view_counts_everywhere_api_enabled\":false"
                + ",\"longform_notetweets_consumption_enabled\":false"
                + ",\"tweet_awards_web_tipping_enabled\":false"
                + ",\"freedom_of_speech_not_reach_fetch_enabled\":false"
                + ",\"standardized_nudges_misinfo\":false"
                + ",\"tweet_with_visibility_results_prefer_gql_limited_actions_policy_enabled\":false"
                + ",\"interactive_text_enabled\":false"
                + ",\"responsive_web_text_conversations_enabled\":false"
                + ",\"longform_notetweets_richtext_consumption_enabled\":false"
                + ",\"responsive_web_enhance_cards_enabled\":false}";
    }

    private Optional<TwitterResponse> castResponse(HttpEntity entity){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(entity.getContent());
            List<JsonNode> results = StreamUtils.toStream(rootNode.path("data").path("user").path("result")
                            .path("timeline").path("timeline").path("instructions").elements())
                    .filter(instruction -> instruction.path("type").textValue().equals("TimelineAddEntries"))
                    .flatMap(instruction -> StreamUtils.toStream(instruction.path("entries").elements()))
                    .map(entry -> entry.path("content").path("itemContent").path("tweet_results").path("result"))
                    .collect(Collectors.toList());

            if (! results.isEmpty()){
                JsonNode userData = results.get(0).path("core").path("user_results")
                        .path("result");
                User author = User.builder()
                        .id(userData.path("rest_id").textValue())
                        .name(userData.path("legacy").path("name").textValue())
                        .screenName(userData.path("legacy").path("screen_name").textValue())
                        .iconUrl(userData.path("legacy").path("profile_image_url_https").textValue())
                        .url(TWITTER_URL + "/" + userData.path("legacy").path("screen_name").textValue())
                        .build();

                List<Tweet> tweets = results.stream()
                        .map(result -> result.path("legacy"))
                        .map(tweetData -> Tweet.builder()
                                    .text(tweetData.path("full_text").textValue())
                                    .url(TWITTER_URL + "/" + author.getScreenName() + "/status/" + tweetData.path("conversation_id_str").textValue())
                                    .mediaUrl(StreamUtils
                                            .toStream(tweetData.path("entities").path("media").elements())
                                            .findFirst()
                                            .map(media -> media.path("expanded_url").textValue()).orElse(null))
                                   // .createdAt(Instant.from(FORMATTER.parse(tweetData.path("created_at").textValue())))
                                // TODO fixme
                                    .build())
                        .collect(Collectors.toList());
                return Optional.of(TwitterResponse.builder().author(author).tweets(tweets).build());
            }
        } catch (IOException e) {
            LOG.error("Cannot cast tweet response", e);
        }
        return Optional.empty();
    }
}

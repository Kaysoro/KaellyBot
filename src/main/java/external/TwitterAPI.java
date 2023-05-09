package external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.Language;
import lombok.AllArgsConstructor;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import payloads.TwitterResponse;
import util.StreamUtils;
import util.Translator;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class TwitterAPI {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterAPI.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy")
            .withLocale(Locale.ENGLISH)
            .withZone(ZoneId.of("UTC"));
    private static final String TWITTER_URL = "https://twitter.com";

    private static final String TWITTER_DOMAIN = ".twitter.com";
    private static final String TWITTER_API_URL = "https://twitter.com/i/api/graphql/BeHK76TOCY3P8nO-FWocjA/UserTweets";
    private static final String HEADER_GUEST_ID = "guest_id";
    private static final String HEADER_GUEST_TOKEN = "x-guest-token";
    private static final String COOKIE_GUEST_TOKEN = "gt";
    private static final String COOKIE_VALUE_SEPARATOR = "=";
    private static final Pattern GUEST_IO_COOKIE_PATTERN = Pattern.compile(HEADER_GUEST_ID + COOKIE_VALUE_SEPARATOR + "(.+);");
    private static final Pattern GUEST_TOKEN_COOKIE_PATTERN = Pattern.compile(COOKIE_GUEST_TOKEN + COOKIE_VALUE_SEPARATOR + "(\\d+);");
    private static final String VARIABLES = "variables";
    private static final String FEATURES = "features";
    private static final int TWEET_COUNT = 20;
    private static final String TWITTER_ENTRY_TYPE_TWEET = "Tweet";

    private static final Map<String, Language> TWITTER_IDS = Stream.of(Language.values())
            .collect(Collectors.toMap(lg -> Translator.getLabel(lg, "twitter.id"), Function.identity()));

    private final String bearerToken;

    public Map<Language, TwitterResponse> getTweets(){
        return getGuestToken().stream()
                .flatMap(guestToken -> TWITTER_IDS.keySet().stream()
                        .map(userId -> getUserTweets(bearerToken, guestToken, userId))
                        .flatMap(Optional::stream))
                .collect(Collectors.toMap(response -> TWITTER_IDS.get(response.getAuthor().getId()), Function.identity()));
    }

    private Optional<String> getGuestToken() {
        HttpClientBuilder builder = HttpClientBuilder.create()
                .setRedirectStrategy(new TwitterRedirectStrategy());

        try (CloseableHttpClient httpClient = builder.build()) {
            HttpResponse response = httpClient.execute(new HttpGet(new URIBuilder(TWITTER_URL).build()));
            if (response.getStatusLine().getStatusCode() == Response.Status.OK.getStatusCode()) {
                Matcher m = GUEST_TOKEN_COOKIE_PATTERN.matcher(EntityUtils.toString(response.getEntity()));
                if (m.find()){
                    return Optional.of(m.group(1));
                } else {
                    LOG.warn("Cannot find GT cookie, guest_token could not be retrieved");
                }
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
                    .filter(this::isEntryOriginalTweet)
                    .collect(Collectors.toList());

            if (! results.isEmpty()){
                JsonNode userData = results.get(0).path("core").path("user_results")
                        .path("result");
                TwitterResponse.User author = TwitterResponse.User.builder()
                        .id(userData.path("rest_id").textValue())
                        .screenName(userData.path("legacy").path("screen_name").textValue())
                        .build();

                List<TwitterResponse.Tweet> tweets = results.stream()
                        .map(tweetData -> TwitterResponse.Tweet.builder()
                                    .url(TWITTER_URL + "/" + author.getScreenName() + "/status/" + tweetData.path("rest_id").textValue())
                                    .createdAt(Instant.from(FORMATTER.parse(tweetData.path("legacy").path("created_at").textValue())))
                                    .build())
                        .sorted(Comparator.comparing(TwitterResponse.Tweet::getCreatedAt))
                        .collect(Collectors.toList());
                return Optional.of(TwitterResponse.builder().author(author).tweets(tweets).build());
            }
        } catch (IOException e) {
            LOG.error("Cannot cast tweet response", e);
        }
        return Optional.empty();
    }

    private boolean isEntryOriginalTweet(JsonNode result){
        return TWITTER_ENTRY_TYPE_TWEET.equals(result.path("__typename").textValue())
                && ! result.path("legacy").has("retweeted_status_result");
    }

    private static class TwitterRedirectStrategy implements RedirectStrategy {
        private static final int MAX_REDIRECTIONS = 5;
        private int redirectionNumber;

        public TwitterRedirectStrategy(){
            redirectionNumber = 0;
        }

        @Override
        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
            boolean isRedirected = isRedirected(response.getStatusLine().getStatusCode());
            Matcher m = GUEST_IO_COOKIE_PATTERN.matcher(response.getFirstHeader("set-cookie").getValue());
            if (m.find()){
                BasicClientCookie cookie = new BasicClientCookie(HEADER_GUEST_ID, m.group(1));
                cookie.setDomain(TWITTER_DOMAIN);
                ((CookieStore) context.getAttribute(HttpClientContext.COOKIE_STORE)).addCookie(cookie);
            } else if (isRedirected) {
                LOG.warn("Cannot set cookie during redirection");
            }

            if (isRedirected) {
                redirectionNumber++;
                return redirectionNumber < MAX_REDIRECTIONS;
            }
            return false;
        }

        @Override
        public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) {
            return new HttpGet(request.getRequestLine().getUri());
        }

        private boolean isRedirected(int statusCode){
            return statusCode ==  HttpStatus.SC_MOVED_TEMPORARILY
                    || statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                    || statusCode == HttpStatus.SC_SEE_OTHER
                    || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT;
        }
    }
}

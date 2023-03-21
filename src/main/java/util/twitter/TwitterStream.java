package util.twitter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Resilience;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class TwitterStream {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterStream.class);

    private final List<TwitterStreamListener> listeners;
    private final Thread stream;

    public TwitterStream(String bearerToken, Map<String, String> rules) throws IOException, URISyntaxException {
        listeners = new CopyOnWriteArrayList<>();
        setupRules(bearerToken, rules);
        stream = new Thread(() -> connectStream(bearerToken));
    }

    public void addListener(TwitterStreamListener listener){
        listeners.add(listener);
    }

    public synchronized void startStream(){
        if (!stream.isAlive()){
            stream.start();
        }
    }

    private void connectStream(String bearerToken) {
        long retries = 0;
        while (true){
            try ( CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setCookieSpec(CookieSpecs.STANDARD).build()).build()) {

                URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream?tweet.fields=author_id,id,text,entities,public_metrics&expansions=author_id&user.fields=profile_image_url,username");

                HttpGet httpGet = new HttpGet(uriBuilder.build());
                httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));

                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader((entity.getContent())));
                    String line = reader.readLine();
                    while (line != null) {
                        LOG.debug("Twitter status received: " + line);
                        // TODO map object
                        listeners.forEach(listener -> listener.onStatus(null));

                        line = reader.readLine();
                    }
                }
            } catch (Exception e){
                LOG.error("Unknown error happened", e);
            }

            try {
                long duration = Resilience.nextExponentialBackoff(retries, 1000L);
                LOG.error("Twitter stream cut down, waiting " + duration + " ms - retry n°" + retries);
                retries++;
                Thread.sleep(duration);
            } catch(Exception e){
                LOG.error("Unknown error happened", e);
            }
        }
    }

    /*
     * Helper method to setup rules before streaming data
     * */
    private void setupRules(String bearerToken, Map<String, String> rules) throws IOException, URISyntaxException {
        List<String> existingRules = getRules(bearerToken);
        if (! existingRules.isEmpty()) {
            deleteRules(bearerToken, existingRules);
        }
        if (! rules.isEmpty())
            createRules(bearerToken, rules);
    }

    /*
     * Helper method to create rules for filtering
     * */
    private void createRules(String bearerToken, Map<String, String> rules) throws URISyntaxException, IOException {
        try ( CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build()).build()) {
            URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

            HttpPost httpPost = new HttpPost(uriBuilder.build());
            httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
            httpPost.setHeader("content-type", "application/json");
            StringEntity body = new StringEntity(getAddFormattedString(rules));
            httpPost.setEntity(body);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                LOG.debug(EntityUtils.toString(entity, "UTF-8"));
            }
        } catch(Exception e){
            LOG.error("Unknown error happened", e);
        }
    }

    /*
     * Helper method to get existing rules
     * */
    private List<String> getRules(String bearerToken) throws URISyntaxException, IOException {
        List<String> rules = new ArrayList<>();
        try ( CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build()).build()){
            URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
            httpGet.setHeader("content-type", "application/json");
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                JSONObject json = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
                if (json.length() > 1) {
                    JSONArray array = (JSONArray) json.get("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        rules.add(jsonObject.getString("id"));
                    }
                }
            }
        } catch(Exception e){
            LOG.error("Unknown error happened", e);
        }
        return rules;
    }

    /*
     * Helper method to delete rules
     * */
    private void deleteRules(String bearerToken, List<String> existingRules) throws URISyntaxException, IOException {
        try ( CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build()).build()){
            URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

            HttpPost httpPost = new HttpPost(uriBuilder.build());
            httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
            httpPost.setHeader("content-type", "application/json");
            StringEntity body = new StringEntity(getDeleteFormattedString(existingRules));
            httpPost.setEntity(body);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                LOG.debug(EntityUtils.toString(entity, "UTF-8"));
            }
        } catch(Exception e){
            LOG.error("Unknown error happened", e);
        }
    }

    private static String getDeleteFormattedString(List<String> ids) {
        StringBuilder sb = new StringBuilder();
        if (ids.size() == 1) {
            return String.format("{ \"delete\": { \"ids\": [%s]}}", "\"" + ids.get(0) + "\"");
        } else {
            for (String id : ids) {
                sb.append("\"").append(id).append("\",");
            }
            String result = sb.toString();
            return String.format("{ \"delete\": { \"ids\": [%s]}}", result.substring(0, result.length() - 1));
        }
    }

    private static String getAddFormattedString(Map<String, String> rules) {
        StringBuilder sb = new StringBuilder();
        if (rules.size() == 1) {
            String key = rules.keySet().iterator().next();
            return String.format("{\"add\": [%s]}", "{\"value\": \"" + key + "\", \"tag\": \"" + rules.get(key) + "\"}");
        } else {
            for (Map.Entry<String, String> entry : rules.entrySet()) {
                String value = entry.getKey();
                String tag = entry.getValue();
                sb.append("{\"value\": \"").append(value).append("\", \"tag\": \"").append(tag).append("\"},");
            }
            String result = sb.toString();
            return String.format("{\"add\": [%s]}", result.substring(0, result.length() - 1));
        }
    }
}

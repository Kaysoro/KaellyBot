package data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.EmbedData;
import discord4j.discordjson.json.EmbedFieldData;
import discord4j.discordjson.json.EmbedImageData;
import enums.Language;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import util.ClientConfig;
import util.JSoupManager;
import util.Translator;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by steve on 06/11/2016.
 */
public class Almanax implements Embedded{

    public final static DateFormat discordToBot = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    public final static DateFormat botToAlmanax = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
    private final static Logger LOG = LoggerFactory.getLogger(Almanax.class);

    private String bonus;
    private String offrande;
    private String day;
    private String quest;
    private String ressourceURL;

    private Almanax(String bonus, String offrande, String day, String quest, String ressourceURL) {
        this.bonus = bonus;
        this.offrande = offrande;
        this.day = day;
        this.quest = quest;
        this.ressourceURL = ressourceURL;
    }

    @Override
    public EmbedCreateSpec decorateEmbedObject(Language lg) {
        return EmbedCreateSpec.builder()
                .title(Translator.getLabel(lg, "almanax.embed.title.1") + " " + day)
                .url(Translator.getLabel(lg, "almanax.url") + day)
                .thumbnail(ressourceURL)
                .addField(Translator.getLabel(lg, "almanax.embed.bonus"), bonus, true)
                .addField(Translator.getLabel(lg, "almanax.embed.offrande"), offrande, true)
                .build();
    }

    @Override
    public EmbedCreateSpec decorateMoreEmbedObject(Language lg) {
        return EmbedCreateSpec.builder()
                .title(Translator.getLabel(lg, "almanax.embed.title.1") + " " + day)
                .url(Translator.getLabel(lg, "almanax.url") + day)
                .description(quest)
                .image(ressourceURL)
                .addField(Translator.getLabel(lg, "almanax.embed.bonus"), bonus, true)
                .addField(Translator.getLabel(lg, "almanax.embed.offrande"), offrande, true)
                .build();
    }

    public EmbedData decorateRestEmbedObject(Language lg){
        return EmbedData.builder()
                .title(Translator.getLabel(lg, "almanax.embed.title.1") + " " + day)
                .url(Translator.getLabel(lg, "almanax.url") + day)
                .description(quest)
                .image(EmbedImageData.builder().url(ressourceURL).build())
                .addField(EmbedFieldData.builder().name(Translator.getLabel(lg, "almanax.embed.bonus")).value(bonus).inline(true).build())
                .addField(EmbedFieldData.builder().name(Translator.getLabel(lg, "almanax.embed.offrande")).value(offrande).inline(true).build())
                .build();
    }

    public static EmbedCreateSpec decorateGroupedObject(Language lg, Date day, int occurrence) throws IOException {
        Date firstDate = DateUtils.addDays(day,1);
        Date lastDate = DateUtils.addDays(day, occurrence);
        String title = Translator.getLabel(lg, "almanax.embed.title.1") + " " + discordToBot.format(firstDate) +
                (occurrence > 1 ? " " + Translator.getLabel(lg, "almanax.embed.title.2") + " " + discordToBot.format(lastDate) : "");

        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .title(title);

        for (int i = 1; i <= occurrence; i++) {
            firstDate = DateUtils.addDays(new Date(), i);
            Almanax almanax = Almanax.get(lg, firstDate);
            builder.addField(discordToBot.format(firstDate), Translator.getLabel(lg, "almanax.embed.bonus")
                    + " " + almanax.getBonus() + "\n" + Translator.getLabel(lg, "almanax.embed.offrande")
                    + " " + almanax.getOffrande(), true);
        }
        return builder.build();
    }

    public static Almanax get(Language lg, String date) throws IOException {
        return gatheringData(lg, date);
    }

    public static Almanax get(Language lg, Date date) throws IOException {
        return gatheringData(lg, botToAlmanax.format(date));
    }

    public static Almanax cacheAndReturn(Language lg, String date, Almanax almanax, Jedis jedis) {
        String key = date + "/" + lg.getAbrev();
        Pipeline pipeline = jedis.pipelined();
        pipeline.hset(key, "bonus", almanax.bonus);
        pipeline.hset(key, "day", almanax.day);
        pipeline.hset(key, "offrande", almanax.offrande);
        pipeline.hset(key, "quest", almanax.quest);
        pipeline.hset(key, "resourceURL", almanax.ressourceURL);
        pipeline.sync();
        jedis.expire(key, 3600 * Constants.almanaxCacheHoursTTL);

        return almanax;
    }

    public static Optional<Almanax> getFromCache(Language lg, String date, Jedis jedis) {
        String key = date + "/" + lg.getAbrev();
        Pipeline pipeline = jedis.pipelined();
        Response<String> _bonus = pipeline.hget(key, "bonus");
        Response<String> _day = pipeline.hget(key, "day");
        Response<String> _offrande = pipeline.hget(key, "offrande");
        Response<String> _quest = pipeline.hget(key, "quest");
        Response<String> _resourceURL = pipeline.hget(key, "resourceURL");
        pipeline.sync();

        if (_day.get() == null) {
            return Optional.empty();
        }

        return Optional.of(new Almanax(_bonus.get(), _offrande.get(), _day.get(), _quest.get(), _resourceURL.get()));
    }

    private static Almanax gatheringWebsiteData(Language lg, String date) throws IOException {
        LOG.info("Connecting to " + Translator.getLabel(lg, "almanax.url") + date + " ...");
        Document doc = JSoupManager.getDocument(Translator.getLabel(lg, "almanax.url") + date);

        String bonus = doc.getElementsByClass("more").first()
                .clone().getElementsByClass("more-infos").empty().parents().first().text();
        String quest = doc.getElementsByClass("more-infos").first().child(0).text();
        String ressourceURL = doc.getElementsByClass("more-infos-content").first().children().attr("src");
        String offrande = doc.getElementsByClass("fleft").get(3).text();
        return new Almanax(bonus, offrande, date, quest, ressourceURL);
    }

    private static Almanax gatheringData(Language lg, String date) throws IOException {
        Jedis jedis = new Jedis(HostAndPort.from(ClientConfig.KAELLY_CACHE_URL()));
        jedis.auth(ClientConfig.KAELLY_CACHE_PASSWORD());

        Optional<Almanax> cached = getFromCache(lg, date, jedis);
        if (cached.isPresent()) {
            LOG.info("Serving cached " + date + " in lang " + lg);
            return cached.get();
        }

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        URI requestUri = UriBuilder.fromPath(Constants.almanaxURL
                        .replace("{game}", "dofus2")
                        .replace("{language}", lg.getAbrev().toLowerCase(Locale.ROOT))
                        .replace("{date}", date)
        ).build();

        HttpGet request = new HttpGet(requestUri);
        request.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
        request.addHeader(HttpHeaders.ACCEPT_CHARSET, "utf-8");
        request.addHeader(HttpHeaders.USER_AGENT, "KaellyBot");

        LOG.info("Fetching " + date + " in language " + lg + " from API...");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            // fallback on API fail to scrape in default language
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK || response.getEntity() == null) {
                return cacheAndReturn(lg, date, gatheringWebsiteData(lg, date), jedis);
            }

            // parse data from successful API response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(EntityUtils.toString(response.getEntity()));

            JsonNode bonusNode = rootNode.get("bonus");
            JsonNode bonusTypeNode = bonusNode.get("type");

            String bonusDescription = bonusNode.get("description").asText();
            String bonusType = bonusTypeNode.get("name").asText();

            JsonNode tributeNode = rootNode.get("tribute");
            JsonNode itemNode = tributeNode.get("item");

            String itemQuantity = tributeNode.get("quantity").asText();
            String itemName = itemNode.get("name").asText();

            JsonNode itemImagesNode = itemNode.get("image_urls");
            JsonNode itemImageSd = itemImagesNode.get("sd");
            JsonNode itemImageIcon = itemImagesNode.get("icon");

            String itemImage = itemImageSd == null ? itemImageIcon.asText() : itemImageSd.asText();

            String offrande = itemName + " x" + itemQuantity;
            String bonus = "~ **" + bonusType + "** ~\n" + bonusDescription;
            String quest = ""; // quest is ignored in the API since it does not include relevant information

            return cacheAndReturn(lg, date, new Almanax(bonus, offrande, date, quest, itemImage), jedis);
        } catch (Throwable e) {
            return cacheAndReturn(lg, date, gatheringWebsiteData(lg, date), jedis);
        }
    }

    public String getBonus() {
        return bonus;
    }

    public String getOffrande() {
        return offrande;
    }

    public String getDay() {
        return day;
    }

    public String getQuest() {
        return quest;
    }
}

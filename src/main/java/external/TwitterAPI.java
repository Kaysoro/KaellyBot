package external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import payloads.TweetDto;
import util.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class TwitterAPI {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterAPI.class);
    private static final String API_BASE_URL = "/tweets";
    private final Client client;

    public TwitterAPI() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        client = ClientBuilder.newClient().register(new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS));
    }

    public Map<Language, List<TweetDto>> getTweets(){
        try {
            return client.target(ClientConfig.TWITTER_URL() + API_BASE_URL)
                    .request(MediaType.APPLICATION_JSON)
                    .get()
                    .readEntity(new GenericType<>() {});
        }catch (Exception e){
            LOG.error("Error occurred while calling Twitter API, returning empty collection...", e);
        }
        return Collections.emptyMap();
    }
}

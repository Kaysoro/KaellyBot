package external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import data.ServerDofus;
import enums.Dimension;
import payloads.PortalDto;
import util.Authenticator;
import util.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class DofusPortalsAPI {

    private static final String API_BASE_URL = "/external/v1/";
    private static final String SERVER_DOMAIN = "servers/";
    private static final String PORTAL_DOMAIN = "/portals/";
    private final Client client;

    public DofusPortalsAPI() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        client = ClientBuilder.newClient().register(new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS))
                .register(new Authenticator(ClientConfig.DOFUS_PORTALS_TOKEN()));
    }

    public List<PortalDto> getPositions(ServerDofus server) {
        return client.target(ClientConfig.DOFUS_PORTALS_URL() + API_BASE_URL + SERVER_DOMAIN
                + server.getDofusPortalsId() + PORTAL_DOMAIN)
                .request(MediaType.APPLICATION_JSON)
                .get()
                .readEntity(new GenericType<>() {});
    }

    public PortalDto getPosition(ServerDofus server, Dimension dimension) {
        return client.target(ClientConfig.DOFUS_PORTALS_URL() + API_BASE_URL+ SERVER_DOMAIN
                + server.getDofusPortalsId() + PORTAL_DOMAIN + dimension.name().toLowerCase())
                .request(MediaType.APPLICATION_JSON)
                .get(PortalDto.class);
    }
}

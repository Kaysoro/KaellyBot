package finders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import data.Constants;
import data.ServerDofus;
import enums.Dimension;
import enums.Language;
import payloads.PortalDto;
import util.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class PortalFinder {

    private static final String API_BASE_URL = "/api/";
    private Client client;

    public PortalFinder() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        client = ClientBuilder.newClient().register(new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS));
    }

    public List<PortalDto> getPositions(ServerDofus server, Language lg) {
        return client.target(ClientConfig.KAELLY_PORTALS_URL() + API_BASE_URL + server.getName() + "/portals")
                .request(MediaType.APPLICATION_JSON)
                .acceptLanguage(lg != Language.ES ? lg.getAbrev() : Constants.defaultLanguage.getAbrev())
                .get()
                .readEntity(new GenericType<List<PortalDto>>() {});
    }

    public PortalDto getPosition(ServerDofus server, Dimension dimension, Language lg) {
        return client.target(ClientConfig.KAELLY_PORTALS_URL() + API_BASE_URL
                + server.getName() + "/portals?dimension=" + dimension.name())
                .request(MediaType.APPLICATION_JSON)
                .acceptLanguage(lg != Language.ES ? lg.getAbrev() : Constants.defaultLanguage.getAbrev())
                .get(PortalDto.class);
    }
}

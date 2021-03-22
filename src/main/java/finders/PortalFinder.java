package finders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import data.ServerDofus;
import enums.Dimension;
import enums.Language;
import payloads.PortalDto;
import util.Authenticator;
import util.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class PortalFinder {

    private static final String API_BASE_URL = "/api/";
    private static final String SERVER_DOMAIN = "servers/";
    private static final String PORTAL_DOMAIN = "/portals/";
    private final Client client;

    public PortalFinder() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        client = ClientBuilder.newClient().register(new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS))
                .register(new Authenticator(ClientConfig.KAELLY_PORTALS_LOGIN(), ClientConfig.KAELLY_PORTALS_PASSWORD()));
    }

    public List<PortalDto> getPositions(ServerDofus server, Language lg) {
        return client.target(ClientConfig.KAELLY_PORTALS_URL() + API_BASE_URL + SERVER_DOMAIN
                + server.getName().replaceAll("-","_").replaceAll("\\s+","_").toUpperCase() + PORTAL_DOMAIN)
                .request(MediaType.APPLICATION_JSON)
                .acceptLanguage(lg.getAbrev())
                .get()
                .readEntity(new GenericType<List<PortalDto>>() {});
    }

    public PortalDto getPosition(ServerDofus server, Dimension dimension, Language lg) {
        return client.target(ClientConfig.KAELLY_PORTALS_URL() + API_BASE_URL+ SERVER_DOMAIN
                + server.getName().replaceAll("-","_").replaceAll("\\s+","_").toUpperCase() + PORTAL_DOMAIN + dimension.name().toUpperCase())
                .request(MediaType.APPLICATION_JSON)
                .acceptLanguage(lg.getAbrev())
                .get(PortalDto.class);
    }
}

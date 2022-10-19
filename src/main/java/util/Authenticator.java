package util;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

import java.io.IOException;

public class Authenticator implements ClientRequestFilter {

    private final String token;

    public Authenticator(String token) {
        this.token = token;
    }

    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        headers.add("token", token);
    }
}
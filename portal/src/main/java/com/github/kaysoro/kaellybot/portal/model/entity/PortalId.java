package com.github.kaysoro.kaellybot.portal.model.entity;

public class PortalId {

    private String server;
    private String dimension;

    public String getServer() {
        return server;
    }

    public PortalId withServer(String server) {
        this.server = server;
        return this;
    }

    public String getDimension() {
        return dimension;
    }

    public PortalId withDimension(String dimension) {
        this.dimension = dimension;
        return this;
    }
}

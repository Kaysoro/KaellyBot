package com.github.kaysoro.kaellybot.portal.model.entity;


public class Portal {

    private String server;
    private String dimension;
    private Position position;
    private Transport nearestZaap;
    private Transport nearestTransportLimited;
    private boolean transportLimitedNearest;

    public String getServer() {
        return server;
    }

    public String getDimension() {
        return dimension;
    }

    public Transport getnearestZaap() {
        return nearestZaap;
    }

    public Transport getNearestTransportLimited() {
        return nearestTransportLimited;
    }

    public boolean isTransportLimitedNearest() {
        return transportLimitedNearest;
    }

    public Position getPosition() {
        return position;
    }
}

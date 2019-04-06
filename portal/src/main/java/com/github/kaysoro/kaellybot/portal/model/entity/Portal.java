package com.github.kaysoro.kaellybot.portal.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "portals")
public class Portal {

    @Id
    private PortalId portalId;
    private Position position;
    private Transport nearestZaap;
    private Transport nearestTransportLimited;
    private boolean transportLimitedNearest;

    public String getServer() {
        return portalId.getServer();
    }

    public String getDimension() {
        return portalId.getDimension();
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

    public Portal withPortalId(PortalId portalId) {
        this.portalId = portalId;
        return this;
    }

    public Portal withPosition(Position position) {
        this.position = position;
        return this;
    }

    public Portal withNearestZaap(Transport nearestZaap) {
        this.nearestZaap = nearestZaap;
        return this;
    }

    public Portal withNearestTransportLimited(Transport nearestTransportLimited) {
        this.nearestTransportLimited = nearestTransportLimited;
        return this;
    }

    public Portal withTransportLimitedNearest(boolean transportLimitedNearest) {
        this.transportLimitedNearest = transportLimitedNearest;
        return this;
    }
}

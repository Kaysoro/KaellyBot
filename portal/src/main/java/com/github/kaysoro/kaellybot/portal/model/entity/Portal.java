package com.github.kaysoro.kaellybot.portal.model.entity;

import com.github.kaysoro.kaellybot.portal.model.constants.Transport;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Document(collection = "portals")
public class Portal {

    @Id
    private PortalId portalId;
    private Position position;
    private Integer utilisation;
    private Instant creationDate;
    private Author creationAuthor;
    private Instant lastUpdateDate;
    private Author lastAuthorUpdate;
    private Transport nearestZaap;
    private Transport nearestTransportLimited;
    private boolean transportLimitedNearest;
    private boolean isAvailable;
    private boolean isUpdated;

    public PortalId getPortalId() {
        return portalId;
    }

    public Position getPosition() {
        return position;
    }

    public Integer getUtilisation() {
        return utilisation;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Author getCreationAuthor() {
        return creationAuthor;
    }

    public Instant getLastUpdateDate() {
        return lastUpdateDate;
    }

    public Author getLastAuthorUpdate() {
        return lastAuthorUpdate;
    }

    public Transport getNearestZaap() {
        return nearestZaap;
    }

    public Transport getNearestTransportLimited() {
        return nearestTransportLimited;
    }

    public boolean isTransportLimitedNearest() {
        return transportLimitedNearest;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isUpdated() {
        return isUpdated;
    }
}

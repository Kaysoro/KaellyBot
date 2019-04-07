package com.github.kaysoro.kaellybot.portal.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public class PortalDto {

    private String dimension;
    private PositionDto position;
    private Boolean isAvailable;
    private Integer utilisation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Instant creationDate;
    private AuthorDto creationAuthor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Instant lastUpdateDate;
    private AuthorDto lastAuthorUpdate;
    private TransportDto nearestZaap;
    private TransportDto nearestTransportLimited;

    public String getDimension() {
        return dimension;
    }

    public PortalDto withDimension(String dimension) {
        this.dimension = dimension;
        return this;
    }

    public PositionDto getPosition() {
        return position;
    }

    public PortalDto withPosition(PositionDto position) {
        this.position = position;
        return this;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public PortalDto withAvailable(Boolean available) {
        isAvailable = available;
        return this;
    }

    public Integer getUtilisation() {
        return utilisation;
    }

    public PortalDto withUtilisation(Integer utilisation) {
        this.utilisation = utilisation;
        return this;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public PortalDto withCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public AuthorDto getCreationAuthor() {
        return creationAuthor;
    }

    public PortalDto withCreationAuthor(AuthorDto creationAuthor) {
        this.creationAuthor = creationAuthor;
        return this;
    }

    public Instant getLastUpdateDate() {
        return lastUpdateDate;
    }

    public PortalDto withLastUpdateDate(Instant lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    public AuthorDto getLastAuthorUpdate() {
        return lastAuthorUpdate;
    }

    public PortalDto withLastAuthorUpdate(AuthorDto lastAuthorUpdate) {
        this.lastAuthorUpdate = lastAuthorUpdate;
        return this;
    }

    public TransportDto getNearestZaap() {
        return nearestZaap;
    }

    public PortalDto withNearestZaap(TransportDto nearestZaap) {
        this.nearestZaap = nearestZaap;
        return this;
    }

    public TransportDto getNearestTransportLimited() {
        return nearestTransportLimited;
    }

    public PortalDto withNearestTransportLimited(TransportDto nearestTransportLimited) {
        this.nearestTransportLimited = nearestTransportLimited;
        return this;
    }
}

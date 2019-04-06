package com.github.kaysoro.kaellybot.portal.model.dto;

public class ExternalPortalDto {

    private PositionDto position;
    private int utilisation;

    public PositionDto getPosition() {
        return position;
    }

    public ExternalPortalDto setPosition(PositionDto position) {
        this.position = position;
        return this;
    }

    public int getUtilisation() {
        return utilisation;
    }

    public ExternalPortalDto setUtilisation(int utilisation) {
        this.utilisation = utilisation;
        return this;
    }
}

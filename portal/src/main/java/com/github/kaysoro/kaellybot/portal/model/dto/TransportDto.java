package com.github.kaysoro.kaellybot.portal.model.dto;

public class TransportDto {
    private String type;
    private String area;
    private String subArea;
    private PositionDto position;
    private boolean isAvailableUnderConditions;

    public String getType() {
        return type;
    }

    public TransportDto withType(String type) {
        this.type = type;
        return this;
    }

    public String getArea() {
        return area;
    }

    public TransportDto withArea(String area) {
        this.area = area;
        return this;
    }

    public String getSubArea() {
        return subArea;
    }

    public TransportDto withSubArea(String subArea) {
        this.subArea = subArea;
        return this;
    }

    public PositionDto getPosition() {
        return position;
    }

    public TransportDto withPosition(PositionDto position) {
        this.position = position;
        return this;
    }

    public boolean isAvailableUnderConditions() {
        return isAvailableUnderConditions;
    }

    public TransportDto withAvailableUnderConditions(boolean availableUnderConditions) {
        isAvailableUnderConditions = availableUnderConditions;
        return this;
    }
}

package com.github.kaysoro.kaellybot.portal.model.dto;

public class PositionDto {

    private int x;
    private int y;
    private boolean isUnknown;

    public PositionDto withX(int x) {
        this.x = x;
        return this;
    }

    public PositionDto withY(int y) {
        this.y = y;
        return this;
    }

    public PositionDto withUnknown(boolean unknown) {
        isUnknown = unknown;
        return this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isUnknown() {
        return isUnknown;
    }
}

package com.github.kaysoro.kaellybot.portal.model.dto;

public class PositionDto {

    private int x;
    private int y;

    public PositionDto withX(int x) {
        this.x = x;
        return this;
    }

    public PositionDto withY(int y) {
        this.y = y;
        return this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

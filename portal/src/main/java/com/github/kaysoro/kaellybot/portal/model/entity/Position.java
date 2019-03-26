package com.github.kaysoro.kaellybot.portal.model.entity;

public class Position {

    private int x;
    private int y;
    private boolean isUnknown;

    public static Position of(int x, int y){
        return new Position()
                .withX(x)
                .withY(y)
                .withUnknown(false);
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

    public Position withX(int x) {
        this.x = x;
        return this;
    }

    public Position withY(int y) {
        this.y = y;
        return this;
    }

    public Position withUnknown(boolean unknown) {
        isUnknown = unknown;
        return this;
    }

    @Override
    public String toString(){
        if (! isUnknown)
            return "[" + x + "," + y + "]";
        return "Unknown";
    }
}

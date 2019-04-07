package com.github.kaysoro.kaellybot.portal.model.entity;

public class Position {

    private int x;
    private int y;

    public static Position of(int x, int y){
        return new Position()
                .withX(x)
                .withY(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position withX(int x) {
        this.x = x;
        return this;
    }

    public Position withY(int y) {
        this.y = y;
        return this;
    }

    @Override
    public String toString(){
        return "[" + x + ", " + y + "]";
    }
}

package data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Position {
    private int x;
    private int y;
    private boolean isNull;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.isNull = false;
    }

    public Position() {
        isNull = true;
    }

    public double getDistance(Position position){
        if (!isNull() && ! position.isNull())
            return Math.hypot(getX() - position.getX(), getY() - position.getY());
        return Double.MAX_VALUE;
    }

    @Override
    public String toString(){
        if (! isNull())
            return "[" + getX() + "," + getY() + "]";
        return "";
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof Position){
            Position pos = (Position) object;
            if (isNull() && pos.isNull())
                return true;
            if (! isNull() && pos.isNull() || isNull() && ! pos.isNull())
                return false;
            return pos.getX() == getX() && pos.getY() == getY();
        }
        return false;
    }

    public boolean isNull(){
        return isNull;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Position parse(String format){
        if (format == null)
            return new Position();
        Matcher m = Pattern.compile("\\[(-?\\d{1,2}),\\s*(-?\\d{1,2})\\]").matcher(format);
        if (m.matches()){
            int posX = Integer.parseInt(m.group(1));
            int posY = Integer.parseInt(m.group(2));
            return new Position(posX, posY);
        }
        return new Position();
    }
}

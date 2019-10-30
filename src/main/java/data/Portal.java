package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by steve on 07/11/2016.
 */
public class Portal {

    private final static Logger LOG = LoggerFactory.getLogger(Portal.class);

    private String name;
    private String url;
    private Color color;

    private Portal(String name, String url, Color color) {
        this.name = name;
        this.url = url;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return url;
    }

    public Color getColor() {
        return color;
    }
}
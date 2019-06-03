package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by steve on 07/11/2016.
 */
public class Portal {

    private final static Logger LOG = LoggerFactory.getLogger(Portal.class);
    private static List<Portal> portals;

    private String name;
    private String url;
    private int color;

    private Portal(String name, String url, int color) {
        this.name = name;
        this.url = url;
        this.color = color;
    }

    private synchronized static List<Portal> getPortals(){
        if (portals == null){
            portals = new ArrayList<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT name, url, color FROM Portal");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()) {
                    Portal portal = new Portal(resultSet.getString("name"),
                            resultSet.getString("url"),
                            resultSet.getInt("color"));
                    portals.add(portal);
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error("getPortals", e);
            }
        }

        return portals;
    }

    public static Portal getPortal(String name) throws Exception {
        final String NORMALIZED_NAME = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("\\W+", "").toLowerCase().trim();
        return getPortals().stream()
                .filter(portal -> Normalizer.normalize(portal.getName(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("\\W+", "").toLowerCase().trim()
                        .equals(NORMALIZED_NAME))
                .findFirst()
                .orElseThrow(() -> new Exception("No portal found for " + name));
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return url;
    }

    public int getColor() {
        return color;
    }
}
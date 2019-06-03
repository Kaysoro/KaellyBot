package data;

import enums.Language;
import enums.Transport;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;
import util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Position implements Embedded {
    
    public static final String NOT_PRESENT = "NOT_PRESENT";
    private final static Logger LOG = LoggerFactory.getLogger(Position.class);
    private final static Pattern sweetDateFormat = Pattern.compile("(\\d+\\s+j\\s+)?(\\d+\\s+h\\s+)?(\\d+\\s+min)");
    private final static long LIMIT = 172800000;
    private final static int NB_UTILISATION_MAX = 128;
    
    private Portal portal;
    private ServerDofus server;
    private Coordinate coordinate;
    private int utilisation;
    private long creation;
    private long lastUpdate;
    private String creationSource;
    private String updateSource;
    private Transport zaap;
    private Transport transportLimited;
    
    private Position(Portal portal, ServerDofus server, Coordinate coordinate, int utilisation,
                     long creation, long lastUpdate, String source) {
        this(portal, server, coordinate, utilisation, creation, lastUpdate, source, source);
    }

    private Position(Portal portal, ServerDofus server, Coordinate coordinate, int utilisation,
                     long creation, long lastUpdate, String creationSource, String updateSource) {
        this.portal = portal;
        this.server = server;
        this.coordinate = coordinate;
        this.utilisation = utilisation;
        this.creation = creation;
        this.lastUpdate = lastUpdate;
        this.creationSource = creationSource;
        this.updateSource = updateSource;
        determineTransports();
    }

    private void determineTransports() {
        if(isValid()) {
            double minDist = Double.MAX_VALUE;
            double minDistLimited = Double.MAX_VALUE;
            for (Transport transport : Transport.values()) {
                double tmp = transport.getCoordinate().getDistance(coordinate);
                if (transport.isFreeAccess() && (zaap == null || minDist > tmp)){
                    zaap = transport;
                    minDist = tmp;
                }
                if (! transport.isFreeAccess() && (transportLimited == null || minDistLimited > tmp)){
                    transportLimited = transport;
                    minDistLimited = tmp;
                }
            }

            if (minDist < minDistLimited)
                transportLimited = null;
        }
        else {
            zaap = null;
            transportLimited = null;
        }
    }
    
    public void setUtilisation(int utilisation, String source) {
        setUtilisation(utilisation, System.currentTimeMillis(), source);
    }

    public synchronized void setUtilisation(int utilisation, long lastUpdate, String source) {
        this.utilisation = utilisation;
        this.lastUpdate = lastUpdate;
        this.updateSource = source;

        if (utilisation == 0)
            reset();
        else if (this.utilisation > NB_UTILISATION_MAX)
            this.utilisation = NB_UTILISATION_MAX;
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Portal_Position SET utilisation = ?, last_update = ?, update_source = ?"
                            + "WHERE name_portal = ? AND name_server = ?;");
            preparedStatement.setInt(1, utilisation);
            preparedStatement.setLong(2, lastUpdate);
            preparedStatement.setString(3, updateSource);
            preparedStatement.setString(4, portal.getName());
            preparedStatement.setString(5, server.getName());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error("setUtilisation", e);
        }
    }
    
    public void setCoordinate(Coordinate coordinate, String source) {
        setCoordinate(coordinate, System.currentTimeMillis(), source);
    }

    public synchronized void setCoordinate(Coordinate coordinate, long creation, String source) {
        if (! this.coordinate.equals(coordinate)){
            this.coordinate = coordinate;
            this.creation = creation;
            this.utilisation = -1;
            this.lastUpdate = -1;
            this.creationSource = source;
            this.updateSource = "";
            determineTransports();
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Portal_Position SET pos = ?, creation = ?, utilisation = ?, last_update = ?," +
                                " creation_source = ?, update_source = ?"
                                + "WHERE name_portal = ? AND name_server = ?;");
                preparedStatement.setString(1, coordinate.toString());
                preparedStatement.setLong(2, creation);
                preparedStatement.setInt(3, utilisation);
                preparedStatement.setLong(4, lastUpdate);
                preparedStatement.setString(5, creationSource);
                preparedStatement.setString(6, updateSource);
                preparedStatement.setString(7, portal.getName());
                preparedStatement.setString(8, server.getName());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error("setCoordinate", e);
            }
        }
    }

    /**
     *
     * @param position new position
     * @return true if the coordinate has changed
     */
    public boolean merge(Position position){
        boolean result = false;
        if (Normalizer.normalize(position.portal.getName(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase()
                .equals(Normalizer.normalize(portal.getName(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase())
                && ! position.coordinate.isNull()) {
            // Si les coordonnées sont différentes et que la date de création est plus récente
            result = ! coordinate.equals(position.coordinate) && creation < position.creation && !coordinate.isNull()
                    || coordinate.isNull() && position.isValid();

            // Si le nombre d'utilisation a changé ou que les coordonnées de base sont nulles
            if (coordinate.equals(position.coordinate) && utilisation > position.utilisation || coordinate.isNull()) {
                setCoordinate(position.coordinate, position.creation, position.creationSource);
                setUtilisation(position.utilisation, position.lastUpdate, position.updateSource);
            }

            // Si la coordonnée de base n'est pas nulle, que les positions sont différentes et que la date de création est plus récente
            else if (!coordinate.isNull() && !coordinate.equals(position.coordinate) && creation < position.creation) {
                setCoordinate(position.coordinate, position.creation, position.creationSource);
                setUtilisation(position.utilisation, position.lastUpdate, position.updateSource);
            }
        }

        return result;
    }

    public void reset() {
        setCoordinate(new Coordinate(), "");
    }

    public boolean isValid(){
        return System.currentTimeMillis() - creation <= LIMIT
                && !coordinate.isNull();
    }

    @Override
    public EmbedObject getEmbedObject(Language lg) {
        return getEmbedBuilder(lg).build();
    }

    public EmbedObject getEmbedObjectChange(Language lg) {
        return getEmbedBuilder(lg)
                .withTitle(":new: " + portal.getName())
                .build();
    }

    @Override
    public EmbedObject getMoreEmbedObject(Language lg) {
        return getEmbedObject(lg);
    }

    private EmbedBuilder getEmbedBuilder(Language lg){
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(portal.getName());
        builder.withColor(portal.getColor());
        builder.withThumbnail(portal.getUrl());

        if (! isValid()) {
            coordinate = new Coordinate();
            utilisation = -1;
            builder.withDescription(Translator.getLabel(lg, "portal.unknown"));
        } else {
            builder.appendField(Translator.getLabel(lg, "portal.position"), "**" + coordinate + "**", true);
            if (utilisation != -1)
                builder.appendField(Translator.getLabel(lg, "portal.utilisation.title"), utilisation
                        + " " + Translator.getLabel(lg, "portal.utilisation.desc")
                        + (utilisation > 1 ? "s" : ""), true);
            if (transportLimited != null)
                builder.appendField(Translator.getLabel(lg, "portal.private_zaap"), transportLimited.toDiscordString(lg), false);
            builder.appendField(Translator.getLabel(lg, "portal.zaap"), zaap.toDiscordString(lg), false);
            builder.withFooterText(getDateInformation(creationSource, updateSource, lg));
        }
        return builder;
    }

    public static List<Position> getSweetPositions(ServerDofus server) throws Exception {
        String source = "dofus-portals.fr";
        List<Position> positions = new ArrayList<>();

        Document doc = JSoupManager.getDocument(Constants.sweetPortals + server.getSweetId());
        Elements dimensions = doc.getElementsByClass("portal");

        for(Element dim : dimensions){
            Portal portal = Portal.getPortal(dim.getElementsByTag("h2").get(0).text());
            String coordinateText = dim.getElementsByTag("h2").get(1).text();
            Coordinate coordinate;
            int utilisation = -1;
            long creation = 0;

            if (! coordinateText.equals("Position Inconnue")) {
                coordinateText = dim.getElementsByTag("h3").get(0).text();
                coordinate = Coordinate.parse(coordinateText);
                utilisation = Integer.parseInt(dim.getElementsByTag("h3")
                        .get(1).getElementsByTag("b").get(0).text());

                Matcher m = sweetDateFormat.matcher(dim.getElementsByTag("h3").get(3).text());
                m.find();
                long timeToRemove = 0;
                if (m.group(1) != null)
                    timeToRemove +=  86400000 * Integer.parseInt(m.group(1).replaceAll("\\s+j\\s+", ""));
                if (m.group(2) != null)
                    timeToRemove += 3600000 * Integer.parseInt(m.group(2).replaceAll("\\s+h\\s+", ""));
                timeToRemove += 60000 * Integer.parseInt(m.group(3).replaceAll("\\s+min", ""));

                creation = System.currentTimeMillis() - timeToRemove;
            }
            else
                coordinate = new Coordinate();
            long lastUpdate = creation;
            positions.add(new Position(portal, server, coordinate, utilisation, creation, lastUpdate, source));
        }

        return positions;
    }

    public synchronized static List<Position> getPositions(ServerDofus server){
        List<Position> positions = new ArrayList<>();

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement("SELECT name_portal, pos, utilisation,"
                    + "creation, last_update, creation_source, update_source FROM Portal_Position WHERE name_server = (?);");
            query.setString(1, server.getName());
            ResultSet resultSet = query.executeQuery();

            String name;
            int utilisation;
            long creation;
            long lastUpdate;
            String creationSource;
            String updateSource;

            while (resultSet.next()) {
                name = resultSet.getString("name_portal");
                String pos = resultSet.getString("pos");
                utilisation = resultSet.getInt("utilisation");
                creation = resultSet.getLong("creation");
                lastUpdate = resultSet.getLong("last_update");
                creationSource = resultSet.getString("creation_source");
                updateSource = resultSet.getString("update_source");
                positions.add(new Position(Portal.getPortal(name), server, Coordinate.parse(pos), utilisation, creation,
                        lastUpdate, creationSource, updateSource));
            }

        } catch (Exception e) {
            Reporter.report(e);
            LOG.error("getPositions", e);
        }

        return positions;
    }
    
    private String getDateInformation(String creationSource, String updateSource, Language lg){
        StringBuilder st = new StringBuilder(Translator.getLabel(lg, "portal.date.added")).append(" ");

        long deltaCreation = System.currentTimeMillis() - creation;
        st.append(getLabelTimeAgo(lg, deltaCreation)).append(" ").append(Translator.getLabel(lg, "portal.date.by"))
                .append(" ").append(creationSource);

        if (lastUpdate != -1 && ! DateUtils.truncatedEquals(new Date(creation), new Date(lastUpdate), Calendar.SECOND)){
            long deltaUpdate = System.currentTimeMillis() - lastUpdate;
            st.append(" - ").append(Translator.getLabel(lg, "portal.date.edited")).append(" ")
                    .append(getLabelTimeAgo(lg, deltaUpdate)).append(" ").append(Translator.getLabel(lg, "portal.date.by"))
                    .append(" ").append(updateSource);
        }
        return st.toString();
    }

    private String getLabelTimeAgo(Language lg, long time){
        if (time < DateUtils.MILLIS_PER_MINUTE)
            return Translator.getLabel(lg, "portal.date.now");
        else if (time < DateUtils.MILLIS_PER_HOUR)
            return Translator.getLabel(lg, "portal.date.minutes_ago")
                    .replace("{time}", String.valueOf(time / DateUtils.MILLIS_PER_MINUTE));
        else
            return Translator.getLabel(lg, "portal.date.hours_ago")
                    .replace("{time}", String.valueOf(time / DateUtils.MILLIS_PER_HOUR));
    }

    public Portal getPortal(){
        return portal;
    }
}

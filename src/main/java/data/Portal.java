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
import util.ClientConfig;
import util.Connexion;
import util.JSoupManager;
import util.Translator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 07/11/2016.
 */
public class Portal implements Embedded{

    private final static Logger LOG = LoggerFactory.getLogger(Portal.class);
    private final static long LIMIT = 172800000;
    private final static Pattern sweetDateFormat = Pattern.compile("(\\d+\\s+j\\s+)?(\\d+\\s+h\\s+)?(\\d+\\s+min)");

    private static Map<String, Portal> portals;

    private String name;
    private String url;
    private Position coordonate;
    private int utilisation;
    private long creation;
    private long lastUpdate;
    private int color;
    private Guild guild;
    private Transport zaap;
    private Transport transportLimited;

    private Portal(String name, String url, int color) {
        this.name = name;
        this.url = url;
        this.color = color;
    }

    private Portal(String name, Position coordonate, int utilisation, long creation, long lastUpdate) {
        this.name = name;
        this.coordonate = coordonate;
        this.utilisation = utilisation;
        this.creation = creation;
        this.lastUpdate = lastUpdate;
    }

    private Portal(Portal classicPortal, Position coordonate, int utilisation, long creation, long lastUpdate, Guild guild) {
        this.name = classicPortal.getName();
        this.url = classicPortal.getUrl();
        this.color = classicPortal.getColor();
        this.coordonate = coordonate;
        this.utilisation = utilisation;
        this.creation = creation;
        this.lastUpdate = lastUpdate;
        this.guild = guild;
        determineTransports();
    }

    private void determineTransports() {
        if(isValid()) {
            double minDist = Double.MAX_VALUE;
            double minDistLimited = Double.MAX_VALUE;
            for (Transport transport : Transport.values()) {
                double tmp = transport.getPosition().getDistance(coordonate);
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

    public synchronized static Map<String, Portal> getPortals(){
        if (portals == null){
            portals = new ConcurrentHashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT name, url, color FROM Portal");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()) {
                    Portal portal = new Portal(resultSet.getString("name"),
                            resultSet.getString("url"),
                            resultSet.getInt("color"));
                    portals.put(portal.getName(), portal);
                }
            } catch (SQLException e) {
                ClientConfig.setSentryContext(null,null, null, null);
                LOG.error(e.getMessage());
            }
        }

        return portals;
    }

    public synchronized static List<Portal> getPortals(Guild g){
        List<Portal> portals = new ArrayList<>();

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement("SELECT name_portal, pos, utilisation,"
                    + "creation, last_update FROM Portal_Guild WHERE id_guild = (?);");
            query.setString(1, g.getId());
            ResultSet resultSet = query.executeQuery();

            String name;
            int utilisation;
            long creation;
            long lastUpdate;

            while (resultSet.next()) {
                name = resultSet.getString("name_portal");
                String pos = resultSet.getString("pos");
                utilisation = resultSet.getInt("utilisation");
                creation = resultSet.getLong("creation");
                lastUpdate = resultSet.getLong("last_update");
                portals.add(new Portal(Portal.getPortals().get(name), Position.parse(pos), utilisation, creation, lastUpdate, g));
            }

        } catch (SQLException e) {
            ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(g.getId())),
                    null, null, null);
            LOG.error(e.getMessage());
        }

        return portals;
    }

    public String getName() {
        return this.name;
    }

    public void setUtilisation(int utilisation) {
        setUtilisation(utilisation, System.currentTimeMillis());
    }

    public synchronized void setUtilisation(int utilisation, long lastUpdate) {
        this.utilisation = utilisation;
        this.lastUpdate = lastUpdate;

        if (utilisation == 0)
            reset();
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Portal_Guild SET utilisation = ?, last_update = ?"
                            + "WHERE name_portal = ? AND id_guild = ?;");
            preparedStatement.setInt(1, utilisation);
            preparedStatement.setLong(2, lastUpdate);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, guild.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(guild.getId())),
                    null, null, null);
            LOG.error(e.getMessage());
        }
    }

    public void setCoordonate(Position coordonate) {
        setCoordonate(coordonate, System.currentTimeMillis());
    }

    public synchronized void setCoordonate(Position coordonate, long creation) {
        if (! this.coordonate.equals(coordonate)){
            this.coordonate = coordonate;
            this.creation = creation;
            this.utilisation = -1;
            this.lastUpdate = -1;
            determineTransports();
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Portal_Guild SET pos = ?, creation = ?, utilisation = ?, last_update = ?"
                                + "WHERE name_portal = ? AND id_guild = ?;");
                preparedStatement.setString(1, coordonate.toString());
                preparedStatement.setLong(2, creation);
                preparedStatement.setInt(3, utilisation);
                preparedStatement.setLong(4, lastUpdate);
                preparedStatement.setString(5, name);
                preparedStatement.setString(6, guild.getId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(guild.getId())),
                        null, null, null);
                LOG.error(e.getMessage());
            }
        }
    }

    public void merge(Portal portal){

        if (Normalizer.normalize(portal.getName(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase()
                        .equals(Normalizer.normalize(getName(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase())
                && ! portal.coordonate.isNull())
            if (coordonate.equals(portal.coordonate) && utilisation > portal.utilisation || coordonate.isNull()){
                setCoordonate(portal.coordonate, portal.creation);
                setUtilisation(portal.utilisation, portal.lastUpdate);
            }
            else if (! coordonate.isNull() && ! coordonate.equals(portal.coordonate) && creation < portal.creation){
                setCoordonate(portal.coordonate, portal.creation);
                setUtilisation(portal.utilisation, portal.lastUpdate);
            }
    }

    public void reset() {
        setCoordonate(new Position());
    }

    public boolean isValid(){
        return System.currentTimeMillis() - creation <= LIMIT
                && !coordonate.isNull();
    }

    @Override
    public EmbedObject getEmbedObject(Language lg) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(name);
        builder.withColor(getColor());
        builder.withThumbnail(url);

        if (! isValid()) {
            coordonate = new Position();
            utilisation = -1;
            builder.withDescription(Translator.getLabel(lg, "portal.unknown"));
        } else {
            builder.appendField(Translator.getLabel(lg, "portal.position"), "**" + coordonate + "**", true);
            if (utilisation != -1)
                builder.appendField(Translator.getLabel(lg, "portal.utilisation.title"), utilisation
                        + " " + Translator.getLabel(lg, "portal.utilisation.desc")
                        + (utilisation > 1 ? "s" : ""), true);
            if (transportLimited != null)
                builder.appendField(Translator.getLabel(lg, "portal.private_zaap"), transportLimited.toDiscordString(lg), false);
            builder.appendField(Translator.getLabel(lg, "portal.zaap"), zaap.toDiscordString(lg), false);
            builder.withFooterText(getDateInformation(lg));
        }

        return builder.build();
    }

    @Override
    public EmbedObject getMoreEmbedObject(Language lg) {
        return getEmbedObject(lg);
    }

    public static List<Portal> getSweetPortals(ServerDofus server) throws IOException {
        List<Portal> portals = new ArrayList<>();

        Document doc = JSoupManager.getDocument(Constants.sweetPortals + server.getSweetId());
        Elements dimensions = doc.getElementsByClass("row");

        for(Element dim : dimensions){
            String name = dim.getElementsByTag("h2").get(0).text();
            String coordonateText = dim.getElementsByTag("h2").get(1).text();
            Position coordonate;
            int utilisation = -1;
            long creation = 0;

            if (! coordonateText.equals("Position Inconnue")) {
                coordonateText = dim.getElementsByTag("h3").get(0).text();
                coordonate = Position.parse(coordonateText);
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
                coordonate = new Position();
            long lastUpdate = creation;
            portals.add(new Portal(name, coordonate, utilisation, creation, lastUpdate));
        }

        return portals;
    }

    private String getDateInformation(Language lg){
        StringBuilder st = new StringBuilder(Translator.getLabel(lg, "portal.date.added")).append(" ");

        long deltaCreation = System.currentTimeMillis() - creation;
        st.append(getLabelTimeAgo(lg, deltaCreation));

        if (lastUpdate != -1 && ! DateUtils.truncatedEquals(new Date(creation), new Date(lastUpdate), Calendar.SECOND)){
            long deltaUpdate = System.currentTimeMillis() - lastUpdate;
            st.append(" - ").append(Translator.getLabel(lg, "portal.date.edited")).append(" ")
            .append(getLabelTimeAgo(lg, deltaUpdate));
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

    public String getUrl() {
        return url;
    }

    public int getColor() {
        return color;
    }
}
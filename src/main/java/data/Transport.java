package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Transport {
    private static final Logger LOG = LoggerFactory.getLogger(Transport.class);
    private static List<Transport> transports;
    private String type;
    private String zone;
    private String sousZone;
    private Position position;
    private boolean isFreeAccess;

    public Transport(String type, String zone, String sousZone, Position position, boolean isFreeAccess) {
        this.type = type;
        this.zone = zone;
        this.sousZone = sousZone;
        this.position = position;
        this.isFreeAccess = isFreeAccess;
    }

    public static List<Transport> getTransports(){
        if (transports == null) {
            transports = new ArrayList<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT type, zone, sous_zone, free_access, pos"
                        + " FROM Transport;");
                ResultSet resultSet = query.executeQuery();

                String type;
                String zone;
                String sousZone;
                String pos;
                boolean isFreeAccess;

                while (resultSet.next()) {
                    type = resultSet.getString("type");
                    zone = resultSet.getString("zone");
                    sousZone = resultSet.getString("sous_zone");
                    pos = resultSet.getString("pos");
                    isFreeAccess = resultSet.getBoolean("free_access");
                    transports.add(new Transport(type, zone, sousZone, Position.parse(pos), isFreeAccess));
                }

            } catch (SQLException e) {
                ClientConfig.setSentryContext(null, null, null, null);
                LOG.error(e.getMessage());
            }
        }

        return transports;
    }

    public String getType() {
        return type;
    }

    public String getZone() {
        return zone;
    }

    public String getSousZone() {
        return sousZone;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isFreeAccess() {
        return isFreeAccess;
    }

    @Override
    public String toString(){
        return getZone() + " (" + getSousZone() + ") **" + getPosition() + "**";
    }
}

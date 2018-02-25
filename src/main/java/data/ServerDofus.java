package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientConfig;
import util.Connexion;
import util.Reporter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve on 20/04/2017.
 */
public class ServerDofus {

    private final static Logger LOG = LoggerFactory.getLogger(ServerDofus.class);
    private static List<ServerDofus> servers;
    private static Map<String, ServerDofus> serversMap;
    private static boolean initialized = false;
    private String name;
    private String id;
    private String sweetId;
    private List<Portal> sweetPortals;
    private long lastSweetRefresh;

    public ServerDofus(String name, String id, String sweetId) {
        this.name = name;
        this.id = id;
        this.sweetId = sweetId;
        sweetPortals = new ArrayList<>();
        lastSweetRefresh = 0;
    }

    private synchronized static void initialize(){
        initialized = true;
        servers = new ArrayList<>();
        serversMap = new HashMap<>();

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement("SELECT name, id_dofus, id_sweet FROM Server");
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ServerDofus sd = new ServerDofus(resultSet.getString("name"),
                        resultSet.getString("id_dofus"),
                        resultSet.getString("id_sweet"));
                servers.add(sd);
                serversMap.put(sd.getName(), sd);
            }
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error("initialize", e);
        }
    }

    public synchronized static Map<String, ServerDofus> getServersMap(){
        if (! initialized)
            initialize();
        return serversMap;
    }

    public synchronized static List<ServerDofus> getServersDofus(){
        if (! initialized)
            initialize();
        return servers;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getSweetId(){
        return sweetId;
    }

    public List<Portal> getSweetPortals(){
        return sweetPortals;
    }

    public void setSweetPortals(List<Portal> sweetPortals) {
        this.lastSweetRefresh = System.currentTimeMillis();
        this.sweetPortals = sweetPortals;
    }

    public long getLastSweetRefresh(){
        return lastSweetRefresh;
    }
}

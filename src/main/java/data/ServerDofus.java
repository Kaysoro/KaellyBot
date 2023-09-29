package data;

import enums.Game;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Connexion;
import util.Reporter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
    private String dofusPortalsId;
    private Map<Language, String> labels;
    private Game game;

    public ServerDofus(String name, String id, String dofusPortalsId, Game game) {
        this.name = name;
        this.id = id;
        this.dofusPortalsId = dofusPortalsId;
        this.game = game;
        this.labels = new HashMap<>();
    }

    private synchronized static void initialize(){
        initialized = true;
        servers = new ArrayList<>();
        serversMap = new HashMap<>();

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement("SELECT name, id_dofus, game FROM Server");
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ServerDofus sd = new ServerDofus(resultSet.getString("name"),
                        resultSet.getString("id_dofus"),
                        resultSet.getString("id_sweet"),
                        Game.valueOf(resultSet.getString("game")));
                servers.add(sd);
                serversMap.put(sd.getName(), sd);
            }

            query = connection.prepareStatement("SELECT server, language, label FROM Server_Label");
            resultSet = query.executeQuery();
            while (resultSet.next()) {
                ServerDofus sd = serversMap.get(resultSet.getString("server"));
                sd.labels.put(Language.valueOf(resultSet.getString("language")), resultSet.getString("label"));
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

    public String getLabel(Language lang){
        return Optional.ofNullable(labels.get(lang)).orElse(getName());
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDofusPortalsId() {
        return dofusPortalsId;
    }

    public Game getGame(){
        return game;
    }

    @Override
    public String toString(){
        return getName();
    }
}

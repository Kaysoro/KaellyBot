package data;

import exceptions.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve on 31/07/2016.
 */
public class Guild {

    private final static Logger LOG = LoggerFactory.getLogger(Guild.class);
    private static Map<String, Guild> guilds;
    private String id;
    private String name;
    private List<Portal> portals;

    public Guild(String id, String name){
        this.id = id;
        this.name = name;

        portals = Portal.getPortals(this);
    }

    public void addToDatabase(){
        if (! getGuilds().containsKey(id)){
            getGuilds().put(id, this);

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement request = connection.prepareStatement("INSERT INTO"
                        + " Guild(id, name) VALUES (?, ?);");
                request.setString(1, id);
                request.setString(2, name);
                request.executeUpdate();

                for(String portal : Portal.getPortals()) {
                    request = connection.prepareStatement("INSERT INTO Portal_Guild"
                            + "(name_portal, id_guild) VALUES (?, ?);");
                    request.setString(1, portal);
                    request.setString(2, id);
                    request.executeUpdate();
                }

            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }

        portals = Portal.getPortals(this);

        if (! User.getUsers().containsKey(id))
            User.getUsers().put(id, new HashMap<String, User>());
    }

    public void removeToDatabase() {
        if (getGuilds().containsKey(id)) {
            getGuilds().remove(id);

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement request = connection.prepareStatement("DELETE FROM Guild WHERE ID = ?;");
                request.setString(1, id);
                request.executeUpdate();

            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }

        if (User.getUsers().containsKey(id))
            User.getUsers().remove(id);
    }

    public void setName(String name){
        this.name = name;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Guild SET name = ? WHERE id = ?;");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error(e.getMessage());
        }
    }

    public static Map<String, Guild> getGuilds(){
        if (guilds == null){
            guilds = new HashMap<String, Guild>();
            String id;
            String name;

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id, name FROM Guild");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()) {
                    id = resultSet.getString("id");
                    name = resultSet.getString("name");
                    guilds.put(id, new Guild(id, name));
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }
        return guilds;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public List<Portal> getPortals(){
        return portals;
    }
}

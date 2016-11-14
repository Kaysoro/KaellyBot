package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve on 31/07/2016.
 */
public class Guild {

    private final static Logger LOG = LoggerFactory.getLogger(Guild.class);
    private static Map<String, Guild> guilds;
    private String id;
    private String name;

    public Guild(String id, String name){
        this.id = id;
        this.name = name;
    }

    public void addToDatabase(){
        if (! getGuild().containsKey(id)){
            getGuild().put(id, this);

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement request = connection.prepareStatement("INSERT INTO"
                        + " Guild(id, name) VALUES (?, ?);");
                request.setString(1, id);
                request.setString(2, name);
                request.executeUpdate();

            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    public void setName(String name){
        this.name = name;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Guild SET name = ?;");
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public static Map<String, Guild> getGuild(){
        if (guilds == null){
            guilds = new HashMap<String, Guild>();
            String id;
            String name;

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement attributionClasse = connection.prepareStatement("SELECT id, name FROM Guild");
                ResultSet resultSet = attributionClasse.executeQuery();

                while (resultSet.next()) {
                    id = resultSet.getString("id");
                    name = resultSet.getString("name");
                    guilds.put(id, new Guild(id, name));
                }
            } catch (SQLException e) {
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
}

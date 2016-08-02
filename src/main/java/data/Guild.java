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

    public Guild(String id){
        this.id = id;
    }

    public void addToDatabase(){
        if (! getGuild().containsKey(id)){
            getGuild().put(id, this);
            User.getUsers().put(id, new HashMap<String, User>());
            //TODO
        }
    }

    public static Map<String, Guild> getGuild(){
        if (guilds == null){
            guilds = new HashMap<String, Guild>();
            String id;

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement attributionClasse = connection.prepareStatement("SELECT id FROM Guild");
                ResultSet resultSet = attributionClasse.executeQuery();

                while (resultSet.next()) {
                    id = resultSet.getString("id");
                    guilds.put(id, new Guild(id));
                    User.getUsers().put(id, new HashMap<String, User>());
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
}

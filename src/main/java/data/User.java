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
 * Created by steve on 28/07/2016.
 */
public class User {
    private final static Logger LOG = LoggerFactory.getLogger(User.class);
    public static int RIGHT_INVITE = 0;
    public static int RIGHT_MEMBER = 1;
    public static int RIGHT_MODERATOR = 2;
    public static int RIGHT_ADMIN = 3;

    private static Map<String, Map<String, User>> users;
    private String id;
    private int rights;
    private Guild guild;

    public User(String id, int rights, Guild guild){
        this.id = id;
        this.rights = rights;
        this.guild = guild;
    }

    public void addToDatabase(){
        if (!getUsers().containsKey(guild)) {
            getUsers().put(guild.getId(), new HashMap<String, User>());
        }
        getUsers().get(guild.getId()).put(id, this);

        //TODO SQL Part
    }

    public void changeRight(int right){
        this.rights = right;

        //TODO SQL Part
    }

    public static Map<String, Map<String, User>> getUsers(){
        if (users == null){
            users = new HashMap<String, Map<String, User>>();

            String id;
            int right;
            Guild guildId;

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement attributionClasse = connection.prepareStatement("SELECT id_user, id_guild, rights FROM User_Guild");
                ResultSet resultSet = attributionClasse.executeQuery();

                while (resultSet.next()) {
                    id = resultSet.getString("id_user");
                    right = resultSet.getInt("rights");
                    guildId = Guild.getGuild().get(resultSet.getString("id_guild"));

                    if (!users.containsKey(guildId.getId()))
                        users.put(guildId.getId(), new HashMap<String, User>());
                    users.get(guildId.getId()).put(id, new User(id, right, guildId));
                }
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }

        return users;
    }

    public int getRights() {
        return rights;
    }

    public Guild getGuild() {
        return guild;
    }

    public String getId() {
        return id;
    }
}

package data;

import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;
import util.ClientConfig;
import util.Connexion;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 31/07/2016.
 */
public class Guild {

    private final static Logger LOG = LoggerFactory.getLogger(Guild.class);
    private static Map<String, Guild> guilds;
    private String id;
    private String name;
    private Map<String, CommandForbidden> commands;
    private String prefixe;
    private Language language;

    public Guild(String id, String name, Language lang){
        this(id, name, Constants.prefixCommand, lang);
    }

    private Guild(String id, String name, String prefixe, Language lang){
        this.id = id;
        this.name = name;
        this.prefixe = prefixe;
        commands = CommandForbidden.getForbiddenCommands(this);
        this.language = lang;
    }

    public synchronized void addToDatabase(){
        if (! getGuilds().containsKey(id)){
            getGuilds().put(id, this);

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement request = connection.prepareStatement("INSERT INTO"
                        + " Guild(id, name, prefixe) VALUES (?, ?, ?);");
                request.setString(1, id);
                request.setString(2, name);
                request.setString(3, prefixe);
                request.executeUpdate();

            } catch (SQLException e) {
                ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(getId())), null, null, null);
                LOG.error(e.getMessage());
            }
        }


            if (! User.getUsers().containsKey(id))
            User.getUsers().put(id, new ConcurrentHashMap<>());
    }

    public synchronized void removeToDatabase() {
        if (getGuilds().containsKey(id)) {
            getGuilds().remove(id);

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement request = connection.prepareStatement("DELETE FROM Guild WHERE ID = ?;");
                request.setString(1, id);
                request.executeUpdate();

            } catch (SQLException e) {
                ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(getId())), null, null, null);
                LOG.error(e.getMessage());
            }
        }

        if (User.getUsers().containsKey(id))
            User.getUsers().remove(id);
    }

    public synchronized void setName(String name){
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
            ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(getId())), null, null, null);
            LOG.error(e.getMessage());
        }
    }

    public synchronized void setPrefixe(String prefixe){
        this.prefixe = prefixe;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Guild SET prefixe = ? WHERE id = ?;");
            preparedStatement.setString(1, prefixe);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(getId())), null, null, null);
            LOG.error(e.getMessage());
        }
    }

    public synchronized void setLanguage(Language lang){
        this.language = lang;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Guild SET lang = ? WHERE id = ?;");
            preparedStatement.setString(1, lang.getAbrev());
            preparedStatement.setString(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(getId())), null, null, null);
            LOG.error(e.getMessage());
        }
    }

    public synchronized static Map<String, Guild> getGuilds(){
        if (guilds == null){
            guilds = new ConcurrentHashMap<>();
            String id;
            String name;
            String prefixe;
            String lang;

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id, name, prefixe, lang FROM Guild");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()) {
                    id = resultSet.getString("id");
                    name = resultSet.getString("name");
                    prefixe = resultSet.getString("prefixe");
                    lang = resultSet.getString("lang");

                    guilds.put(id, new Guild(id, name, prefixe, Language.valueOf(lang)));
                }
            } catch (SQLException e) {
                ClientConfig.setSentryContext(null, null, null, null);
                LOG.error(e.getMessage());
            }
        }
        return guilds;
    }

    public static Guild getGuild(IGuild guild){
        return getGuild(guild, true);
    }

    public synchronized static Guild getGuild(IGuild discordGuild, boolean forceCache){
        Guild guild = getGuilds().get(discordGuild.getStringID());

        if (guild == null && forceCache){
            guild = new Guild(discordGuild.getStringID(), discordGuild.getName(), Constants.defaultLanguage);
            guild.addToDatabase();
        }

        return guild;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getPrefixe(){ return prefixe; }

    public Language getLanguage() {
        return language;
    }

    public Map<String, CommandForbidden> getForbiddenCommands(){
        return commands;
    }
}

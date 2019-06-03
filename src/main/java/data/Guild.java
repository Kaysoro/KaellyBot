package data;

import enums.Language;
import finders.PortalTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;
import util.Connexion;
import util.Reporter;

import java.sql.*;
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
    private Map<String, PortalTracker> portalTrackers;
    private String prefix;
    private ServerDofus server;
    private Language language;

    public Guild(String id, String name, Language lang){
        this(id, name, Constants.prefixCommand, lang, null);
    }

    private Guild(String id, String name, String prefix, Language lang, String serverDofus){
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        commands = CommandForbidden.getForbiddenCommands(this);
        this.language = lang;
        this.server = ServerDofus.getServersMap().get(serverDofus);
        portalTrackers = PortalTracker.getPortalTrackers(this);
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
                request.setString(3, prefix);
                request.executeUpdate();
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }
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
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }
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
            Reporter.report(e);
            LOG.error(e.getMessage());
        }
    }

    public synchronized void setPrefix(String prefix){
        this.prefix = prefix;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Guild SET prefixe = ? WHERE id = ?;");
            preparedStatement.setString(1, prefix);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error(e.getMessage());
        }
    }

    public synchronized void setServer(ServerDofus server){
        this.server = server;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Guild SET server_dofus = ? WHERE id = ?;");
            if (server != null) preparedStatement.setString(1, server.getName());
            else preparedStatement.setNull(1, Types.VARCHAR);
            preparedStatement.setString(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e);
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
            Reporter.report(e);
            LOG.error(e.getMessage());
        }
    }

    public synchronized static Map<String, Guild> getGuilds(){
        if (guilds == null){
            guilds = new ConcurrentHashMap<>();
            String id;
            String name;
            String prefix;
            String server;
            String lang;

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id, name, prefixe, server_dofus, lang FROM Guild");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()) {
                    id = resultSet.getString("id");
                    name = resultSet.getString("name");
                    prefix = resultSet.getString("prefixe");
                    server = resultSet.getString("server_dofus");
                    lang = resultSet.getString("lang");

                    guilds.put(id, new Guild(id, name, prefix, Language.valueOf(lang), server));
                }
            } catch (SQLException e) {
                Reporter.report(e);
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

    public String getPrefix(){ return prefix; }

    public Language getLanguage() {
        return language;
    }

    public Map<String, CommandForbidden> getForbiddenCommands(){
        return commands;
    }

    public Map<String, PortalTracker> getPortalTrackers(){
        return portalTrackers;
    }

    public ServerDofus getServerDofus() {
        return server;
    }
}
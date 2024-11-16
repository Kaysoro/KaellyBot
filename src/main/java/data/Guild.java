package data;

import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Connexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private String prefix;
    private Language language;

    public Guild(String id, String name, Language lang){
        this(id, name, Constants.prefixCommand, lang);
    }

    private Guild(String id, String name, String prefix, Language lang){
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.language = lang;
    }

    public synchronized static Map<String, Guild> getGuilds(){
        if (guilds == null){
            guilds = new ConcurrentHashMap<>();
            String id;
            String name;
            String prefix;
            String lang;

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id, name, prefixe, lang FROM Guild");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()) {
                    id = resultSet.getString("id");
                    name = resultSet.getString("name");
                    prefix = resultSet.getString("prefixe");
                    lang = resultSet.getString("lang");

                    guilds.put(id, new Guild(id, name, prefix, Language.valueOf(lang)));
                }
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }
        return guilds;
    }

    public synchronized static Guild getGuild(discord4j.core.object.entity.Guild discordGuild){
        Guild guild = getGuilds().get(discordGuild.getId().asString());

        if (guild == null){
            guild = new Guild(discordGuild.getId().asString(), discordGuild.getName(), Constants.defaultLanguage);
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
}
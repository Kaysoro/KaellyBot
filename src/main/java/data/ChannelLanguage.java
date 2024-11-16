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
 * Created by steve on 12/01/2017.
 */
public class ChannelLanguage {
    private final static Logger LOG = LoggerFactory.getLogger(ChannelLanguage.class);
    private static Map<Long, ChannelLanguage> channelLanguages;
    private Language lang;
    private long channelId;

    public ChannelLanguage(Language lang, long channelId) {
        this.lang = lang;
        this.channelId = channelId;
    }

    public synchronized static Map<Long, ChannelLanguage> getChannelLanguages(){
        if (channelLanguages == null) {
            channelLanguages = new ConcurrentHashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT lang, id_chan FROM Channel_Language");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    long idChan = Long.parseLong(resultSet.getString("id_chan"));
                    Language lang = Language.valueOf(resultSet.getString("lang"));
                    channelLanguages.put(idChan, new ChannelLanguage(lang, idChan));
                }
            } catch (SQLException e) {
                LOG.error("getChannelLanguages", e);
            }
        }
        return channelLanguages;
    }

    public Long getChannelId(){
        return channelId;
    }

    public Language getLang() {
        return lang;
    }
}

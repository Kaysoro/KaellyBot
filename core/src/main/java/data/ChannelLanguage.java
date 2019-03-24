package data;

import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import util.ClientConfig;
import util.Connexion;
import util.Reporter;

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
                    IChannel chan = ClientConfig.DISCORD().getChannelByID(idChan);
                    if (chan != null && ! chan.isDeleted())
                        channelLanguages.put(chan.getLongID(), new ChannelLanguage(lang, idChan));
                    else {
                        new ChannelLanguage(lang, idChan).removeToDatabase();
                        LOG.info("Chan deleted : " + idChan);
                    }
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error("getChannlLanguages", e);
            }
        }
        return channelLanguages;
    }

    public synchronized void addToDatabase(){
        if (! getChannelLanguages().containsKey(getChannelId())) {
            getChannelLanguages().put(getChannelId(), this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Channel_Language(id_chan, lang) VALUES(?, ?);");
                preparedStatement.setString(1, String.valueOf(getChannelId()));
                preparedStatement.setString(2, getLang().getAbrev());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Reporter.report(e, ClientConfig.DISCORD().getChannelByID(getChannelId()).getGuild(),
                        ClientConfig.DISCORD().getChannelByID(getChannelId()));
                LOG.error("addToDatabase", e);
            }
        }
    }

    public synchronized void setLanguage(Language lang){
        this.lang = lang;
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Channel_Language SET lang = ? WHERE id_chan = ?;");
            preparedStatement.setString(1, getLang().getAbrev());
            preparedStatement.setString(2, String.valueOf(getChannelId()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e, ClientConfig.DISCORD().getChannelByID(getChannelId()).getGuild(),
                    ClientConfig.DISCORD().getChannelByID(getChannelId()));
            LOG.error("setLanguage", e);
        }
    }

    public synchronized void removeToDatabase() {
        getChannelLanguages().remove(getChannelId());

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM Channel_Language WHERE id_chan = ?;");
            request.setString(1, String.valueOf(getChannelId()));
            request.executeUpdate();

        } catch (SQLException e) {
            Reporter.report(e, ClientConfig.DISCORD().getChannelByID(getChannelId()).getGuild(),
                    ClientConfig.DISCORD().getChannelByID(getChannelId()));
            LOG.error("removeToDatabase", e);
        }
    }

    public Long getChannelId(){
        return channelId;
    }

    public Language getLang() {
        return lang;
    }
}

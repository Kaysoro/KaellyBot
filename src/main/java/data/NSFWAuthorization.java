package data;

import exceptions.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve on 12/01/2017.
 */
public class NSFWAuthorization {
    private final static Logger LOG = LoggerFactory.getLogger(NSFWAuthorization.class);
    protected static Map<String, NSFWAuthorization> nsfwChannels;
    private String channelId;

    public NSFWAuthorization(String channelId) {
        this.channelId = channelId;
    }

    public static Map<String, NSFWAuthorization> getNSFWChannels(){
        if (nsfwChannels == null) {
            nsfwChannels = new HashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id_chan FROM NSFW_Authorization");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    IChannel chan = ClientConfig.CLIENT().getChannelByID(resultSet.getString("id_chan"));
                    nsfwChannels.put(chan.getID(), new NSFWAuthorization(chan.getID()));
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }
        return nsfwChannels;
    }

    public void addToDatabase(){
        if (! getNSFWChannels().containsKey(getChannelId())) {
            getNSFWChannels().put(getChannelId(), this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO NSFW_Authorization(id_chan) VALUES(?);");
                preparedStatement.setString(1, getChannelId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }
    }

    public void removeToDatabase() {
        getNSFWChannels().remove(getChannelId());

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM NSFW_Authorization WHERE id_chan = ?;");
            request.setString(1, getChannelId());
            request.executeUpdate();

        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error(getChannelId() + " : " + e.getMessage());
        }
    }

    public String getChannelId(){
        return channelId;
    }
}

package data;

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
 * Created by steve on 22/05/2019.
 */
public class ChannelServer {
    private final static Logger LOG = LoggerFactory.getLogger(ChannelServer.class);
    private static Map<Long, ChannelServer> channelServers;
    private ServerDofus serverDofus;
    private long channelId;

    public ChannelServer(ServerDofus serverDofus, long channelId) {
        this.serverDofus = serverDofus;
        this.channelId = channelId;
    }

    public synchronized static Map<Long, ChannelServer> getChannelServers(){
        if (channelServers == null) {
            channelServers = new ConcurrentHashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT server, id_chan FROM Channel_Server");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    long idChan = Long.parseLong(resultSet.getString("id_chan"));
                    ServerDofus server = ServerDofus.getServersMap().get(resultSet.getString("server"));
                    IChannel chan = ClientConfig.DISCORD().getChannelByID(idChan);
                    if (chan != null && ! chan.isDeleted())
                        channelServers.put(chan.getLongID(), new ChannelServer(server, idChan));
                    else {
                        new ChannelServer(server, idChan).removeToDatabase();
                        LOG.info("Chan deleted : " + idChan);
                    }
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error("getChannelServers", e);
            }
        }
        return channelServers;
    }

    public synchronized void addToDatabase(){
        if (! getChannelServers().containsKey(getChannelId())) {
            getChannelServers().put(getChannelId(), this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Channel_Server(id_chan, server) VALUES(?, ?);");
                preparedStatement.setString(1, String.valueOf(getChannelId()));
                preparedStatement.setString(2, getServer().getName());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Reporter.report(e, ClientConfig.DISCORD().getChannelByID(getChannelId()).getGuild(),
                        ClientConfig.DISCORD().getChannelByID(getChannelId()));
                LOG.error("addToDatabase", e);
            }
        }
    }

    public synchronized void setLanguage(ServerDofus server){
        this.serverDofus = server;
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Channel_Server SET server = ? WHERE id_chan = ?;");
            preparedStatement.setString(1, getServer().getName());
            preparedStatement.setString(2, String.valueOf(getChannelId()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e, ClientConfig.DISCORD().getChannelByID(getChannelId()).getGuild(),
                    ClientConfig.DISCORD().getChannelByID(getChannelId()));
            LOG.error("setServer", e);
        }
    }

    public synchronized void removeToDatabase() {
        getChannelServers().remove(getChannelId());

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM Channel_Server WHERE id_chan = ?;");
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

    public ServerDofus getServer() {
        return serverDofus;
    }
}

package finders;

import data.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortalTracker {
    private final static Logger LOG = LoggerFactory.getLogger(PortalTracker.class);
    private String idGuild;
    private String chan;

    public PortalTracker(String idGuild, String chan) {
        this.idGuild = idGuild;
        this.chan = chan;
    }

    public synchronized void addToDatabase(){
        if (! Guild.getGuilds().get(getGuildId()).getPortalTrackers().containsKey(getChan())) {
            Guild.getGuilds().get(getGuildId()).getPortalTrackers().put(getChan(), this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Portal_Tracker(id_guild, id_chan) VALUES(?, ?);");
                preparedStatement.setString(1, getGuildId());
                preparedStatement.setString(2, getChan());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Reporter.report(e, ClientConfig.DISCORD().getGuildByID(Long.parseLong(getGuildId())),
                        ClientConfig.DISCORD().getChannelByID(Long.parseLong(getChan())));
                LOG.error("addToDatabase", e);
            }
        }
    }

    public synchronized void removeToDatabase() {
        Guild.getGuilds().get(getGuildId()).getPortalTrackers().remove(getChan());

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM Portal_Tracker WHERE id_chan = ?;");
            request.setString(1, getChan());
            request.executeUpdate();

        } catch (SQLException e) {
            Reporter.report(e, ClientConfig.DISCORD().getGuildByID(Long.parseLong(getGuildId())),
                    ClientConfig.DISCORD().getChannelByID(Long.parseLong(getChan())));
            LOG.error("removeToDatabase", e);
        }
    }

    private static synchronized void removeToDatabase(String guildId, String channelId) {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM Portal_Tracker WHERE id_chan = ?;");
            request.setString(1, channelId);
            request.executeUpdate();

        } catch (SQLException e) {
            Reporter.report(e, ClientConfig.DISCORD().getGuildByID(Long.parseLong(guildId)),
                    ClientConfig.DISCORD().getChannelByID(Long.parseLong(channelId)));
            LOG.error("removeToDatabase", e);
        }
    }

    public synchronized static Map<String, PortalTracker> getPortalTrackers(Guild guild){
        Map<String, PortalTracker> portalTrackers = new HashMap<>();
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement query = connection
                    .prepareStatement("SELECT id_guild, id_chan FROM Portal_Tracker WHERE id_guild = ?;");
            query.setString(1, guild.getId());
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()){
                String idChan = resultSet.getString("id_chan");
                String idGuild = resultSet.getString("id_guild");

                IChannel chan = ClientConfig.DISCORD().getChannelByID(Long.parseLong(idChan));

                if (chan != null && ! chan.isDeleted())
                    portalTrackers.put(chan.getStringID(), new PortalTracker(chan.getGuild().getStringID(), chan.getStringID()));
                else {
                    PortalTracker.removeToDatabase(idGuild, idChan);
                    LOG.info("Chan deleted : " + idChan);
                }
            }
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error("getPortalTrackers", e);
        }

        return portalTrackers;
    }

    public static List<PortalTracker> getPortalTrackers(){
        List<PortalTracker> portalTrackers = new ArrayList<>();
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            ResultSet resultSet = connection
                    .prepareStatement("SELECT id_guild, id_chan FROM Portal_Tracker;")
                    .executeQuery();

            while (resultSet.next()){
                String idChan = resultSet.getString("id_chan");
                String idGuild = resultSet.getString("id_guild");
                IChannel chan = ClientConfig.DISCORD().getChannelByID(Long.parseLong(idChan));

                if (chan != null && ! chan.isDeleted())
                    portalTrackers.add(new PortalTracker(chan.getGuild().getStringID(), chan.getStringID()));
                else {
                    PortalTracker.removeToDatabase(idGuild, idChan);
                    LOG.info("Chan deleted : " + idChan);
                }
            }
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error("getPortalTrackers", e);
        }

        return portalTrackers;
    }

    public String getChan() {
        return chan;
    }

    public String getGuildId() {
        return idGuild;
    }
}
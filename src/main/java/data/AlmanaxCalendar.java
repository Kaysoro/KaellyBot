package data;

import discord.Message;
import exceptions.ExceptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlmanaxCalendar {
    private final static Logger LOG = LoggerFactory.getLogger(RSSFinder.class);
    private static boolean isStarted = false;

    private static Map<String, AlmanaxCalendar> almanaxCalendar = null;
    private String idGuild;
    private String chan;

    public AlmanaxCalendar(String idGuild, String chan) {
        this.idGuild = idGuild;
        this.chan = chan;
    }

    public static void start(){
        if (!isStarted){
            isStarted = true;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            long firstDelay = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay().plusMinutes(1), ChronoUnit.MINUTES);
            long period = TimeUnit.DAYS.toMinutes(1);

            scheduler.scheduleAtFixedRate(() -> {
                boolean success = false;

                while(!success)
                    try {
                        Almanax almanax = Almanax.get(new Date());

                        for(AlmanaxCalendar calendar : getAlmanaxCalendars().values()){
                            IChannel chan = ClientConfig.DISCORD().getChannelByID(Long.parseLong(calendar.getChan()));
                            if (chan != null && ! chan.isDeleted())
                                Message.sendEmbed(chan, almanax.getMoreEmbedObject());
                        }
                        success = true;
                    } catch (IOException e) {
                        ExceptionManager.manageSilentlyIOException(e);
                        try { Thread.sleep(300000); // 5min
                        } catch (InterruptedException e1) {}
                    }
            }, firstDelay, period, TimeUnit.MINUTES);
        }
    }

    public void addToDatabase(){
        if (! getAlmanaxCalendars().containsKey(getChan())) {
            getAlmanaxCalendars().put(getChan(), this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Almanax_Calendar(id_guild, id_chan) VALUES(?, ?);");
                preparedStatement.setString(1, getGuildId());
                preparedStatement.setString(2, getChan());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(getGuildId())),
                        null, ClientConfig.DISCORD().getChannelByID(Long.parseLong(getChan())),
                        null);
                LOG.error(e.getMessage());
            }
        }
    }

    public void removeToDatabase() {
        getAlmanaxCalendars().remove(getChan());

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM Almanax_Calendar WHERE id_chan = ?;");
            request.setString(1, getChan());
            request.executeUpdate();

        } catch (SQLException e) {
            ClientConfig.setSentryContext(ClientConfig.DISCORD().getGuildByID(Long.parseLong(getGuildId())),
                    null, ClientConfig.DISCORD().getChannelByID(Long.parseLong(getChan())),
                    null);
            LOG.error(getChan() + " : " + e.getMessage());
        }
    }

    public static Map<String, AlmanaxCalendar> getAlmanaxCalendars(){
        if (almanaxCalendar == null){
            almanaxCalendar = new HashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id_guild, id_chan FROM Almanax_Calendar");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    String idChan = resultSet.getString("id_chan");
                    String idGuild = resultSet.getString("id_guild");

                    IChannel chan = ClientConfig.DISCORD().getChannelByID(Long.parseLong(idChan));

                    if (chan != null && ! chan.isDeleted())
                        almanaxCalendar.put(chan.getStringID(), new AlmanaxCalendar(chan.getGuild().getStringID(), chan.getStringID()));
                    else {
                        new RSSFinder(idGuild, idChan).removeToDatabase();
                        LOG.info("Chan deleted : " + idChan);
                    }
                }
            } catch (SQLException e) {
                ClientConfig.setSentryContext(null,null, null, null);
                LOG.error(e.getMessage());
            }
        }

        return almanaxCalendar;
    }

    public String getChan() {
        return chan;
    }

    public String getGuildId() {
        return idGuild;
    }
}
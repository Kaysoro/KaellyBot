package finders;

import data.Almanax;
import discord4j.common.util.Snowflake;
import discord4j.discordjson.json.EmbedData;
import discord4j.rest.entity.RestChannel;
import discord4j.rest.http.client.ClientException;
import enums.Language;
import util.*;
import exceptions.ExceptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlmanaxCalendar {
    private final static Logger LOG = LoggerFactory.getLogger(AlmanaxCalendar.class);
    private static boolean isStarted = false;

    private static Map<String, AlmanaxCalendar> almanaxCalendar = null;
    private final String idGuild;
    private final String chan;

    public AlmanaxCalendar(String idGuild, String chan) {
        this.idGuild = idGuild;
        this.chan = chan;
    }

    public static void start(){
        if (!isStarted){
            LOG.info("Lancement du calendrier Almanax...");
            isStarted = true;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            long firstDelay = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay().plusMinutes(1), ChronoUnit.MINUTES);
            long period = TimeUnit.DAYS.toMinutes(1);

            scheduler.scheduleAtFixedRate(() -> {
                boolean success = false;
                Map<Language, EmbedData> almanax = new HashMap<>();

                while(!success)
                    try {
                        almanax.clear();
                        for(Language lg : Language.values())
                            almanax.put(lg, Almanax.get(lg, new Date()).decorateRestEmbedObject(lg));
                        success = true;
                    } catch (IOException e) {
                        ExceptionManager.manageSilentlyIOException(e);
                        try { Thread.sleep(300000); // 5min
                        } catch (InterruptedException e1) {
                            LOG.error("start", e1);
                        }
                    }

                for(AlmanaxCalendar calendar : getAlmanaxCalendars().values()) {
                    try {
                        RestChannel chan = ClientConfig.DISCORD().getChannelById(Snowflake.of(calendar.chan));
                        Language lg = Translator.getLanguageFrom(chan);
                        chan.createMessage(almanax.get(lg))
                                .doOnError(error -> {
                                    if (error instanceof ClientException) {
                                        LOG.warn("AlmanaxCalendar: no access on " + calendar.getChan());
                                    } else LOG.error("AlmanaxCalendar", error);
                                })
                                .subscribe();
                    } catch (ClientException e) {
                        LOG.warn("AlmanaxCalendar: no access on " + calendar.getChan());
                    } catch (Exception e) {
                        LOG.error("AlmanaxCalendar", e);
                    }
                }

            }, firstDelay, period, TimeUnit.MINUTES);
        }
    }

    public synchronized void addToDatabase(){
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
                LOG.error("addToDatabase", e);
            }
        }
    }

    public synchronized void removeToDatabase() {
        getAlmanaxCalendars().remove(getChan());

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement request = connection.prepareStatement("DELETE FROM Almanax_Calendar WHERE id_chan = ?;");
            request.setString(1, getChan());
            request.executeUpdate();

        } catch (SQLException e) {
            LOG.error("removeToDatabase", e);
        }
    }

    public synchronized static Map<String, AlmanaxCalendar> getAlmanaxCalendars(){
        if (almanaxCalendar == null){
            almanaxCalendar = new ConcurrentHashMap<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT id_guild, id_chan FROM Almanax_Calendar");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()){
                    String idChan = resultSet.getString("id_chan");
                    String idGuild = resultSet.getString("id_guild");
                    almanaxCalendar.put(idChan, new AlmanaxCalendar(idGuild, idChan));
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error("getAlmanaxCalendars", e);
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
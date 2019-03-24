package stats;

import commands.model.Command;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Connexion;
import util.Reporter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by steve on 11/09/2018.
 */
public class CommandStatistics {
    private final static Logger LOG = LoggerFactory.getLogger(CommandStatistics.class);
    private String commandName;
    private int use;
    private Date date;

    private CommandStatistics(String commandName, int use) {
        this.commandName = commandName;
        this.use = use;
    }

    private CommandStatistics(Date date, int use) {
        this.date = date;
        this.use = use;
    }

    public static List<CommandStatistics> getStatisticsPerCommand(long period){
        List<CommandStatistics> result = new ArrayList<>();
        long dateMin = System.currentTimeMillis() - period;
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement("SELECT name_command, count(*) AS use"
                    + " FROM Command_Statistics WHERE instant > ? GROUP BY name_command");
            query.setLong(1, dateMin);
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()){
                String command = resultSet.getString("name_command");
                int use = resultSet.getInt("use");
                result.add(new CommandStatistics(command, use));
            }
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error("getStatisticsPerCommand", e);
        }

        return result;
    }

    public static List<CommandStatistics> getStatisticsPerCommandForbidden(){
        List<CommandStatistics> result = new ArrayList<>();
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement("SELECT name_command, count(*) AS use"
                    + " FROM Command_Guild GROUP BY name_command");
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()){
                String command = resultSet.getString("name_command");
                int use = resultSet.getInt("use");
                result.add(new CommandStatistics(command, use));
            }
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error("getStatisticsPerCommandForbidden", e);
        }

        return result;
    }

    public static List<CommandStatistics> getStatistics(long period){
        List<CommandStatistics> result = new ArrayList<>();
        long dateMin = System.currentTimeMillis() - period;
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement("SELECT count(*) AS use, instant,"
                    + " strftime('%s', date(instant / 1000, 'unixepoch', 'localtime')) as date"
                    + " FROM Command_Statistics WHERE instant > ? GROUP BY date");
            query.setLong(1, dateMin);
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()){
                long date = resultSet.getLong("date") * 1000;
                int use = resultSet.getInt("use");
                result.add(new CommandStatistics(new Date(date), use));
            }
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error("getStatistics", e);
        }

        return result;
    }

    public static void addStatsToDatabase(Command command){
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Command_Statistics(name_command, instant) VALUES(?, ?);");
            preparedStatement.setString(1, command.getName());
            preparedStatement.setLong(2, System.currentTimeMillis());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error("addStatsToDatabase", e);
        }
    }

    public static InputStream purge() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();
        StringBuilder st = new StringBuilder();

        try {
            PreparedStatement query = connection.prepareStatement("SELECT name_command, instant FROM Command_Statistics");
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next())
                st.append("INSERT INTO Command_Statistics(name_command, instant) VALUES(\"")
                        .append(resultSet.getString("name_command")).append("\", ")
                        .append(resultSet.getString("instant")).append(");\n");
            PreparedStatement request = connection.prepareStatement("DELETE FROM Command_Statistics;");
            request.executeUpdate();

            return IOUtils.toInputStream(st.toString(), StandardCharsets.UTF_8);

        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error("purge", e);
        }

        return null;
    }

    public String getCommandName() {
        return commandName;
    }

    public int getUse() {
        return use;
    }

    public Date getDate() {
        return date;
    }
}

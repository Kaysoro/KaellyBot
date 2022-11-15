package data;

import commands.model.LegacyCommand;
import commands.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Connexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 07/11/2016.
 */
public class CommandForbidden {

    private final static Logger LOG = LoggerFactory.getLogger(CommandForbidden.class);

    private LegacyCommand command;
    private Guild guild;
    private boolean isSaved;

    public CommandForbidden(LegacyCommand command, Guild guild) {
        this(command, guild, false);
    }

    public CommandForbidden(LegacyCommand command, Guild guild, boolean isSaved) {
        this.command = command;
        this.guild = guild;
        this.isSaved = isSaved;
    }

    public synchronized static Map<String, CommandForbidden> getForbiddenCommands(Guild g){
        Map<String, CommandForbidden> commands = new ConcurrentHashMap<>();

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement(
                    "SELECT name_command FROM Command_Guild WHERE id_guild = (?);");
            query.setString(1, g.getId());
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                String commandName = resultSet.getString("name_command");
                if (CommandManager.getCommand(commandName) != null) {
                    CommandForbidden tmp = new CommandForbidden(CommandManager.getCommand(commandName), g, true);
                    commands.put(tmp.getCommand().getName(), tmp);
                }
            }

        } catch (SQLException e) {
            LOG.error("getForbiddenCommands", e);
        }

        return commands;
    }

    public synchronized void addToDatabase(){
        if (! isSaved) {
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Command_Guild(id_guild, name_command) VALUES(?, ?);");
                preparedStatement.setString(1, getGuild().getId());
                preparedStatement.setString(2, getCommand().getName());
                preparedStatement.executeUpdate();

                getGuild().getForbiddenCommands().put(getCommand().getName(), this);
                isSaved = true;
            } catch (SQLException e) {
                LOG.error("addToDatabase", e);
            }
        }
    }

    public synchronized void removeToDatabase() {
        if (isSaved) {
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement request = connection.prepareStatement(
                        "DELETE FROM Command_Guild WHERE id_guild = ? AND name_command = ?;");
                request.setString(1, getGuild().getId());
                request.setString(2, getCommand().getName());
                request.executeUpdate();

                getGuild().getForbiddenCommands().remove(getCommand().getName());
                isSaved = false;

            } catch (SQLException e) {
                LOG.error("removeToDatabase", e);
            }
        }
    }

    public LegacyCommand getCommand(){
        return command;
    }

    public Guild getGuild(){
        return guild;
    }
}

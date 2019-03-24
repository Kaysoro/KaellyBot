package commands.admin;

import commands.model.AbstractCommand;
import data.Constants;
import enums.Language;
import exceptions.BasicDiscordException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.*;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;

/**
 * Created by steve on 23/12/2017.
 */
public class VacuumCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(VacuumCommand.class);

    public VacuumCommand(){
        super("vacuum","");
        setAdmin(true);
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        String oldSize = FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(new File(Constants.database)));

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("VACUUM;");
            preparedStatement.executeUpdate();
            String newSize = FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(new File(Constants.database)));
            Message.sendText(message.getChannel(), "OK : " + oldSize + " -> " + newSize);
        } catch (SQLException e) {
            Reporter.report(e, message.getGuild(), message.getChannel(), message.getAuthor(), message);
            BasicDiscordException.UNKNOWN_ERROR.throwException(message, this, lg);
            LOG.error("request", e);
        }
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "vacuum.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe);
    }
}

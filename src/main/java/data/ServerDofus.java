package data;

import exceptions.Reporter;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 20/04/2017.
 */
public class ServerDofus {

    private static List<ServerDofus> servers;
    private String name;
    private String id;

    public ServerDofus(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public static List<ServerDofus> getServersDofus(){
        if (servers == null){
            servers = new ArrayList<>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT name, id_dofus FROM Server");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next())
                    servers.add(new ServerDofus(resultSet.getString("name"),
                            resultSet.getString("id_dofus")));
            } catch (SQLException e) {
                Reporter.report(e);
                LoggerFactory.getLogger(ServerDofus.class).error(e.getMessage());
            }
        }

        return servers;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}

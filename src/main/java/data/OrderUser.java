package data;

import enums.City;
import enums.Order;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientConfig;
import util.Connexion;
import util.Quadruple;
import util.Reporter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 19/02/2018.
 */
public class OrderUser {

    private final static Logger LOG = LoggerFactory.getLogger(OrderUser.class);
    private static Map<Quadruple<Long, ServerDofus, City, Order>, OrderUser> orders;
    private City city;
    private Order order;
    private long idUser;
    private int level;
    private ServerDofus server;


    public OrderUser(long idUser, ServerDofus server, City city, Order order, int level){
        this.city = city;
        this.order = order;
        if (level > 100)
            level = 100;
        this.level = level;
        this.idUser = idUser;
        this.server = server;
    }

    public synchronized void setLevel(int level){
        if (level > 100)
            level = 100;
        this.level = level;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement;
            if (level > 0) {
                preparedStatement = connection.prepareStatement(
                        "UPDATE Order_User SET level = ?"
                                + "WHERE name_city = ? AND name_order = ? AND id_user = ? AND server_dofus = ?;");
                preparedStatement.setInt(1, level);
                preparedStatement.setString(2, city.getName());
                preparedStatement.setString(3, order.getName());
                preparedStatement.setString(4, String.valueOf(idUser));
                preparedStatement.setString(5, server.getName());
            }
            else {
                preparedStatement = connection.prepareStatement(
                        "DELETE FROM Order_User "
                                + "WHERE name_city = ? AND name_order = ? AND id_user = ? AND server_dofus = ?;");
                preparedStatement.setString(1, city.getName());
                preparedStatement.setString(2, order.getName());
                preparedStatement.setString(3, String.valueOf(idUser));
                preparedStatement.setString(4, server.getName());
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Reporter.report(e);
            LOG.error(e.getMessage());
        }
    }

    /**
     * Ajoute à la base de donnée l'objet si celui-ci n'y est pas déjà.
     */
    public synchronized void addToDatabase(){
        if (! getOrders().containsKey(Quadruple.of(idUser, server, city, order)) && level > 0) {
            getOrders().put(Quadruple.of(idUser, server, city, order), this);
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Order_User(id_user, server_dofus, name_city, name_order, level) "
                                + "VALUES(?, ?, ?, ?, ?);");
                preparedStatement.setString(1, String.valueOf(idUser));
                preparedStatement.setString(2, server.getName());
                preparedStatement.setString(3, city.getName());
                preparedStatement.setString(4, order.getName());
                preparedStatement.setInt(5, level);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public ServerDofus getServer() {
        return server;
    }

    public static synchronized Map<Quadruple<Long, ServerDofus, City, Order>, OrderUser> getOrders(){
        if(orders == null){
            orders = new ConcurrentHashMap<>();
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement(
                        "SELECT id_user, server_dofus, name_city, name_order, level FROM Order_User;");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()) {
                    Long idUser = resultSet.getLong("id_user");
                    ServerDofus server = ServerDofus.getServersMap().get(resultSet.getString("server_dofus"));
                    City city = City.getCity(resultSet.getString("name_city"));
                    Order order = Order.getOrder(resultSet.getString("name_order"));
                    int level = resultSet.getInt("level");
                    orders.put(Quadruple.of(idUser, server, city, order), new OrderUser(idUser, server, city, order, level));
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error(e.getMessage());
            }
        }

        return orders;
    }
}

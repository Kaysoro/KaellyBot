package data;

import enums.City;
import enums.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IUser;
import util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 19/02/2018.
 */
public class OrderUser {

    private final static Logger LOG = LoggerFactory.getLogger(OrderUser.class);
    private static MultiKeySearch<OrderUser> orders;
    private static final int NUMBER_FIELD = 4;
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
            Reporter.report(e, ClientConfig.DISCORD().getUserByID(idUser));
            LOG.error("setLevel", e);
        }
    }

    /**
     * Ajoute à la base de donnée l'objet si celui-ci n'y est pas déjà.
     */
    public synchronized void addToDatabase(){
        if (! getOrders().containsKeys(idUser, server, city, order) && level > 0) {
            getOrders().add(this, idUser, server, city, order);
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
                Reporter.report(e, ClientConfig.DISCORD().getUserByID(idUser));
                LOG.error("addToDatabase", e);
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public ServerDofus getServer() {
        return server;
    }

    public static synchronized MultiKeySearch<OrderUser> getOrders(){
        if(orders == null){
            orders = new MultiKeySearch<>(NUMBER_FIELD);
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
                    orders.add(new OrderUser(idUser, server, city, order, level), idUser, server, city, order);
                }
            } catch (SQLException e) {
                Reporter.report(e);
                LOG.error("getOrders", e);
            }
        }
        return orders;
    }

    /**
     * @param user Joueur de la guilde
     * @param server Serveur dofus
     * @return Liste des résultats de la recherche
     */
    public static List<OrderUser> getOrdersFromUser(IUser user, ServerDofus server){
        return getOrders().get(user.getLongID(), server, null, null);
    }

    /**
     * @param users Joueurs de la guilde
     * @param server Serveur dofus
     * @param level Niveau pallier
     * @return Liste des résultats de la recherche
     */
    public static List<OrderUser> getOrdersFromLevel(List<IUser> users, ServerDofus server, int level){
        List<OrderUser> result = new ArrayList<>();
        for(IUser user : users) {
            List<OrderUser> potentials = getOrders().get(user.getLongID(), server, null, null);
            for(OrderUser order : potentials)
                if (order.getLevel() >= level)
                    result.add(order);
        }
        return result;
    }

    /**
     * @param users Joueurs de la guilde
     * @param server Serveur dofus
     * @param city Cité (Brakmar ou Bonta)
     * @param order Ordre (coeur, oeil, esprit)
     * @return Liste des résultats de la recherche
     */
    public static List<OrderUser> getOrdersFromCityOrOrder(List<IUser> users, ServerDofus server, City city, Order order){
        List<OrderUser> result = new ArrayList<>();
        for(IUser user : users) {
            List<OrderUser> potentials = getOrders().get(user.getLongID(), server, city, order);
            result.addAll(potentials);
        }
        return result;
    }
}

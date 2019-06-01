package data;

import enums.City;
import enums.Language;
import enums.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by steve on 19/02/2018.
 */
public class OrderUser extends ObjectUser {

    private final static Logger LOG = LoggerFactory.getLogger(OrderUser.class);
    private static final String ORDER_PREFIX = "align";
    private static MultiKeySearch<OrderUser> orders;
    private static final int NUMBER_FIELD = 4;
    private static final int LEVEL_MAX = 100;
    private City city;
    private Order order;

    public OrderUser(long idUser, ServerDofus server, City city, Order order, int level){
        this.city = city;
        this.order = order;
        if (level > LEVEL_MAX)
            level = LEVEL_MAX;
        this.level = level;
        this.idUser = idUser;
        this.server = server;
    }

    public synchronized void setLevel(int level){
        if (level > LEVEL_MAX)
            level = LEVEL_MAX;
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
                getOrders().remove(idUser, server, city, order);
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

    private City getCity() {
        return city;
    }

    private Order getOrder() {
        return order;
    }

    @Override
    protected String displayLine(IGuild guild, Language lg) {
        IUser user = ClientConfig.DISCORD().getUserByID(idUser);
        return EmojiManager.getEmojiForPresence(user.getPresence().getStatus()) + " "
                + city.getLogo() + " " + order.getLabel(lg) + ", " + level + " : **"
                + user.getDisplayName(guild) + "**\n";
    }

    private static synchronized MultiKeySearch<OrderUser> getOrders(){
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
     * if JobUser instance contains keys
     * @param user User id
     * @param server Dofus server
     * @param city City (eg. BONTA or BRAKMAR)
     * @param order Order (eg. HEART, SPIRIT, EYES)
     * @return true if it contains
     */
    public static boolean containsKeys(long user, ServerDofus server, City city, Order order){
        return getOrders().containsKeys(user, server, city, order);
    }

    /**
     * getData with all the keys
     * @param user User id
     * @param server Dofus server
     * @param city City (eg. BONTA or BRAKMAR)
     * @param order Order (eg. HEART, SPIRIT, EYES)
     * @return List of JobUser
     */
    public static List<OrderUser> get(long user, ServerDofus server, City city, Order order){
        return getOrders().get(user, server, city, order);
    }

    /**
     * @param user Joueur de la guilde
     * @param server Serveur dofus
     * @return Liste des résultats de la recherche
     */
    public static List<EmbedObject> getOrdersFromUser(IUser user, ServerDofus server, IGuild guild, Language lg){
        List<OrderUser> result = getOrders().get(user.getLongID(), server, null, null);
        result.sort(OrderUser::compare);
        List<EmbedObject> embed = new ArrayList<>();

        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle(Translator.getLabel(lg, "align.user").replace("{user}", user.getDisplayName(guild)));
        builder.withThumbnail(user.getAvatarURL());
        builder.withColor(new Random().nextInt(16777216));

        if (! result.isEmpty()) {
            StringBuilder st = new StringBuilder();
            for (OrderUser orderUser : result)
                st.append(orderUser.city.getLogo()).append(orderUser.order.getLabel(lg)).append(" : ")
                        .append(orderUser.level).append("\n");
            builder.appendField(Translator.getLabel(lg, "align.aligns"), st.toString(), true);
        }
        else
            builder.withDescription(Translator.getLabel(lg, "align.empty"));
        builder.withFooterText(server.getName());
        embed.add(builder.build());
        return embed;
    }

    /**
     * @param users Joueurs de la guilde
     * @param server Serveur dofus
     * @param level Niveau pallier
     * @return Liste des résultats de la recherche
     */
    public static List<EmbedObject> getOrdersFromLevel(List<IUser> users, ServerDofus server, int level,
                                                       IGuild guild, Language lg){
        List<OrderUser> result = new ArrayList<>();
        for(IUser user : users)
            if (! user.isBot()){
                List<OrderUser> potentials = getOrders().get(user.getLongID(), server, null, null);
                for(OrderUser order : potentials)
                    if (order.getLevel() >= level)
                        result.add(order);
            }
        result.sort(OrderUser::compare);
        return getPlayersList(result, guild, server, lg, ORDER_PREFIX);
    }

    /**
     * @param users Joueurs de la guilde
     * @param server Serveur dofus
     * @param city Cité (Brakmar ou Bonta)
     * @param order Ordre (coeur, oeil, esprit)
     * @return Liste des résultats de la recherche
     */
    public static List<EmbedObject> getOrdersFromCityOrOrder(List<IUser> users, ServerDofus server, City city,
                                                             Order order, IGuild guild, Language lg){
        List<OrderUser> result = new ArrayList<>();
        for(IUser user : users)
            if (! user.isBot()){
                List<OrderUser> potentials = getOrders().get(user.getLongID(), server, city, order);
                result.addAll(potentials);
            }
        result.sort(OrderUser::compare);
        return getPlayersList(result, guild, server, lg, ORDER_PREFIX);
    }

    private static int compare(OrderUser o1, OrderUser o2) {
        return Comparator.comparingInt(OrderUser::getLevel).reversed()
                .thenComparing(OrderUser::getCity)
                .thenComparing(OrderUser::getOrder)
                .compare(o1, o2);
    }
}
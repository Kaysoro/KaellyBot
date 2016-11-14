package data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve on 06/11/2016.
 */
public class Almanax {

    private final static Logger LOG = LoggerFactory.getLogger(Almanax.class);
    private static Map<String, Almanax> calendar;

    private String bonus;
    private String offrande;
    private String day;

    private Almanax(String bonus, String offrande, String day) {
        this.bonus = bonus;
        this.offrande = offrande;
        this.day = day;
    }

    public static Almanax get(String date) {

        if (! getCalendar().containsKey(date)){
            // We have to search on the official website
            Almanax almanax = gatheringOnlineData(date);

            if (almanax != null)
                almanax.addToDatabase();
            return almanax;
        }

        return getCalendar().get(date);
    }

    public static Map<String, Almanax> getCalendar(){
        if (calendar == null) {
            calendar = new HashMap<String, Almanax>();

            String date;
            String offrande;
            String bonus;

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT date, offrande, bonus FROM Almanax");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next()) {
                    date = resultSet.getString("date");
                    offrande = resultSet.getString("offrande");
                    bonus = resultSet.getString("bonus");
                    calendar.put(date, new Almanax(date, offrande, bonus));
                }
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }

        return calendar;
    }

    private void addToDatabase() {
        if (! getCalendar().containsKey(day)) {
            getCalendar().put(day, this);

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement request = connection.prepareStatement("INSERT INTO"
                        + " Almanax(date, offrande, bonus) VALUES (?, ?, ?);");
                request.setString(1, day);
                request.setString(2, offrande);
                request.setString(3, bonus);
                request.executeUpdate();

            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    private static Almanax gatheringOnlineData(String date){
        try {
            LOG.info("connecting to " + Constants.almanaxURL + date + " ...");
            Document doc = Jsoup.parse(new URL(Constants.almanaxURL + date).openStream(), "UTF-8",
                    Constants.almanaxURL + date);

            Element elem = doc.select("div.more").first();
            String offrande = elem.select("p.fleft").first().text();

            elem.children().get(elem.children().size() - 1).remove();
            String bonus = elem.text();

            return new Almanax(bonus, offrande, date);

        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public String getBonus(){
        return "*Bonus* : " + bonus;
    }

    public String getOffrande(){
        return "*Offrande* : " + offrande;
    }
}

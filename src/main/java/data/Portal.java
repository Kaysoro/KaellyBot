package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by steve on 07/11/2016.
 */
public class Portal {

    private final static Logger LOG = LoggerFactory.getLogger(Portal.class);
    private final static long LIMIT = 172800000;
    private final static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy à HH:mm", Locale.FRANCE);

    private static List<String> portals;

    private String name;
    private String coordonate;
    private int utilisation;
    private long creation;
    private long lastUpdate;
    private Guild guild;

    private Portal(String name, String coordonate, int utilisation, long creation, long lastUpdate, Guild guild) {
        this.name = name;
        this.coordonate = coordonate;
        this.utilisation = utilisation;
        this.creation = creation;
        this.lastUpdate = lastUpdate;
        this.guild = guild;
    }

    public static List<String> getPortals(){
        if (portals == null){
            portals = new ArrayList<String>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement query = connection.prepareStatement("SELECT name FROM Portal");
                ResultSet resultSet = query.executeQuery();

                while (resultSet.next())
                    portals.add(resultSet.getString("name"));
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }

        return portals;
    }

    public static List<Portal> getPortals(Guild g){
        List<Portal> portals = new ArrayList<Portal>();

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement("SELECT name_portal, pos, utilisation, "
                    + "creation, last_update FROM Portal_Guild WHERE id_guild = (?);");
            query.setString(1, g.getId());
            ResultSet resultSet = query.executeQuery();

            String name;
            String pos;
            int utilisation;
            long creation;
            long lastUpdate;

            while (resultSet.next()) {
                name = resultSet.getString("name");
                pos = resultSet.getString("pos");
                utilisation = resultSet.getInt("utilisation");
                creation = resultSet.getLong("creation");
                lastUpdate = resultSet.getLong("last_update");
                portals.add(new Portal(name, pos, utilisation, creation, lastUpdate, g));
            }

        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }

        return portals;
    }

    public String getName() {
        return this.name;
    }

    public void setUtilisation(int utilisation) {
        this.utilisation = utilisation;
        this.lastUpdate = System.currentTimeMillis();
        //TODO Database
    }

    public void setCoordonate(String coordonate) {
        if (! coordonate.equals(this.coordonate)){
            this.coordonate = coordonate;
            this.creation = System.currentTimeMillis();
            this.utilisation = -1;
            this.lastUpdate = -1;
            //TODO Database
        }

    }

    @Override
    public String toString(){
        StringBuilder st = new StringBuilder("*")
                .append(getName())
                .append( "* : ");
        if (System.currentTimeMillis() - creation > LIMIT
                ||coordonate == null) {
            coordonate = null;
            utilisation = -1;
            st.append("Aucune position récente trouvée.\n");
        }
        else {
            st.append("**").append(coordonate).append("**");

            if (utilisation != -1)
                st.append(", ").append(utilisation).append(" utilisations");
            st.append(" *(ajouté le ").append(dateFormat.format(new Date(creation)));
            if (lastUpdate != -1)
                    st.append(" - édité le ").append(dateFormat.format(new Date(lastUpdate)));
            st.append(")*\n");
        }

        return st.toString();
    }
}

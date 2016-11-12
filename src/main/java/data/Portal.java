package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static List<Portal> portals;

    private String name;
    private String coordonate;
    private int utilisation;
    private long creation;
    private long lastUpdate;

    private Portal(String name, String coordonate, int utilisation, long creation, long lastUpdate) {
        this.name = name;
        this.coordonate = coordonate;
        this.utilisation = utilisation;
        this.creation = creation;
        this.lastUpdate = lastUpdate;
    }

    public static List<Portal> getPortals(){
        if (portals == null){
            portals = new ArrayList<Portal>();
            portals.add(new Portal("Enutrosor", null, -1, System.currentTimeMillis(), -1));
            portals.add(new Portal("Srambad", null, -1, System.currentTimeMillis(), System.currentTimeMillis()));
            portals.add(new Portal("Xélorium", null, -1, System.currentTimeMillis(), -1));
            portals.add(new Portal("Ecaflipus", null, -1, System.currentTimeMillis(), System.currentTimeMillis()));
            //TODO Gathering data from database
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

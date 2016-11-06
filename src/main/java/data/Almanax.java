package data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve on 06/11/2016.
 */
public class Almanax {

    private static Map<String, Almanax> calendar;

    private String bonus;
    private String offrande;
    private String day;

    public Almanax(String bonus, String offrande, String day) {
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

            //TODO Gathering data from database
        }

        return calendar;
    }

    private void addToDatabase() {
        if (! getCalendar().containsKey(day))
            getCalendar().put(day, this);

        //TODO call to database
    }

    private static Almanax gatheringOnlineData(String date){
        //TODO
        return null;
    }

    public String getBonus(){
        return "Bonus : " + bonus;
    }

    public String getOffrande(){
        return "Offrande : " + offrande;
    }
}

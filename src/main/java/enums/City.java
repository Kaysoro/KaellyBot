package enums;

import util.Translator;

import java.util.HashMap;
import java.util.Map;

public enum City {

    BONTA("city.bonta", "<:bonta:411236938771857431>"), BRAKMAR("city.brakmar", "<:brakmar:411237228736675860>");

    private static Map<String, City> cities;
    private String name;
    private String logo;

    City(String name, String logo){ this.name = name; this.logo = logo;}

    public String getName(){
        return name;
    }

    public String getLogo(){
        return logo;
    }

    public String getLabel(Language lg){
        return Translator.getLabel(lg, getName());
    }

    public static City getCity(String name){
        if (cities == null){
            cities = new HashMap<>();
            for(City city : City.values())
                cities.put(city.getName(), city);
        }

        if (cities.containsKey(name))
            return cities.get(name);
        throw new IllegalArgumentException("No city found for \"" + name + "\".");
    }
}

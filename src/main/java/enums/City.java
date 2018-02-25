package enums;

import util.Translator;

import java.util.HashMap;
import java.util.Map;

public enum City {

    BONTA("city.bonta"), BRAKMAR("city.brakmar");

    private static Map<String, City> cities;
    private String name;

    City(String name){ this.name = name;}

    public String getName(){
        return name;
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
        throw new IllegalArgumentException("Aucune cité trouvée pour \"" + name + "\".");
    }
}

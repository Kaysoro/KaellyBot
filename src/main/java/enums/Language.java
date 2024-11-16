package enums;

import java.util.List;

/**
 * Created by steve on 07/06/2017.
 */
public enum Language {

    FR("Français", "FR", List.of("fr")),
    EN("English", "EN", List.of("en-US", "en-GB")),
    ES("Español", "ES", List.of("es-ES"));

    private final String name;
    private final String abrev;
    private final List<String> locales;

    Language(String name, String abrev, List<String> locales){
        this.name = name;
        this.abrev = abrev;
        this.locales = locales;
    }

    public String getName() {
        return name;
    }

    public String getAbrev() {
        return abrev;
    }

    public List<String> getLocales() {
        return locales;
    }

    @Override
    public String toString(){
        return name;
    }
}

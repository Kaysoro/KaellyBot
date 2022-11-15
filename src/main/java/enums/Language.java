package enums;

import java.util.List;

/**
 * Created by steve on 07/06/2017.
 */
public enum Language {

    FR("Français", "FR", List.of("fr")),
    EN("English", "EN", List.of("en-US", "en-GB")),
    ES("Español", "ES", List.of("es-ES"));

    private String name;
    private String abrev;

    private List<String> locale;

    Language(String name, String abrev, List<String> locale){
        this.name = name;
        this.abrev = abrev;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public String getAbrev() {
        return abrev;
    }

    public List<String> getLocales() {
        return locale;
    }

    @Override
    public String toString(){
        return name;
    }
}

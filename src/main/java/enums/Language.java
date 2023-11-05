package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Created by steve on 07/06/2017.
 */
@Getter
@AllArgsConstructor
public enum Language {

    FR("Français", "FR", List.of("fr")),
    EN("English", "EN", List.of("en-US", "en-GB")),
    ES("Español", "ES", List.of("es-ES"));

    private final String name;
    private final String abrev;
    private final List<String> locales;

    @Override
    public String toString(){
        return name;
    }
}

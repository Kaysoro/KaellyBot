package enums;

import util.Translator;

/**
 * Created by steve on 26/06/2017.
 */
public enum SuperTypeEquipment implements SuperType {
    WEAPON("weapon.url"),
    EQUIPMENT("equipment.url"),
    PET("pet.url"),
    MONTURE("monture.url");

    private String url;

    SuperTypeEquipment(String url){this.url = url;}

    public String getUrl(Language lg){
        return Translator.getLabel(lg, url);
    }
}

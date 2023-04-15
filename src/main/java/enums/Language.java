package enums;

/**
 * Created by steve on 07/06/2017.
 */
public enum Language {

    FR("Français", "FR"), EN("English", "EN"), ES("Español", "ES");

    private String name;
    private String abrev;

    Language(String name, String abrev){
        this.name = name;
        this.abrev = abrev;
    }

    public String getName() {
        return name;
    }

    public String getAbrev() {
        return abrev;
    }

    @Override
    public String toString(){
        return name;
    }
}

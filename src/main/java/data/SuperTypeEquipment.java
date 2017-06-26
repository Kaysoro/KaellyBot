package data;

/**
 * Created by steve on 26/06/2017.
 */
public enum SuperTypeEquipment {
    WEAPON(Constants.weaponPageURL),
    EQUIPMENT(Constants.equipementPageURL),
    PET(Constants.petPageURL),
    MONTURE(Constants.monturePageURL);

    private String url;

    SuperTypeEquipment(String url){this.url = url;}

    public String getUrl(){
        return url;
    }
}

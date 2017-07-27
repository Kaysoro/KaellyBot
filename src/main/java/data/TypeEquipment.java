package data;

import static data.SuperTypeEquipment.*;

/**
 * Created by steve on 07/06/2017.
 */
public enum TypeEquipment {

    AMULETTE("&type_id[]=1", new String[]{"Amulette", "Collier", "Talisman", "Pendentif", "Médaille", "Torque"}, EQUIPMENT),
    ARC("&type_id[]=2", new String[]{"Arc"}, WEAPON),
    BAGUETTE("&type_id[]=3", new String[]{"Baguette", "Bâtonnet"}, WEAPON),
    BATON("&type_id[]=4", new String[]{"Bâton", "Sceptre", "Canne", "Racine"}, WEAPON),
    DAGUE("&type_id[]=5", new String[]{"Dague"}, WEAPON),
    EPEE("&type_id[]=6", new String[]{"Epée", "Épée", "Sabres", "Sabre", "Glaive"}, WEAPON),
    MARTEAU("&type_id[]=7", new String[]{"Marteau", "Masse"}, WEAPON),
    PELLE("&type_id[]=8", new String[]{"Pelle", "Bêche"}, WEAPON),
    ANNEAU("&type_id[]=9", new String[]{"Anneau", "Alliance", "Gant", "Chevalière", "Bracelet", "Bague"}, EQUIPMENT),
    CEINTURE("&type_id[]=10", new String[]{"Ceinture", "Sangle", "Pagne", "Slip"}, EQUIPMENT),
    BOTTES("&type_id[]=11", new String[]{"Botte", "Sandale", "Sabot", "Pantoufle", "Tongue", "Bottine"}, EQUIPMENT),

    CHAPEAU("&type_id[]=16", new String[]{"Chapeau", "Coiffe", "Casque", "Masque", "Capuche", "Couronne"}, EQUIPMENT),
    CAPE("&type_id[]=17", new String[]{"Cape"}, EQUIPMENT),
    HACHE("&type_id[]=19", new String[]{"Hache"}, WEAPON),
    OUTIL("&type_id[]=20", new String[]{"Outil"}, WEAPON),
    PIOCHE("&type_id[]=21", new String[]{"Pioche"}, WEAPON),
    FAUX("&type_id[]=22", new String[]{"Faux"}, WEAPON),
    DOFUS("&type_id[]=23", new String[]{"Dofus"}, EQUIPMENT),

    SACADOS("&type_id[]=81", new String[]{"Sac"}, EQUIPMENT),
    BOUCLIER("&type_id[]=82", new String[]{"Bouclier"}, EQUIPMENT),
    PIERREDAME("&type_id[]=83", new String[]{"Pierre"}, WEAPON),

    FAMILIER("&type_id[]=121", new String[]{"Familier", "Montilier"}, PET),
    TROPHEE("&type_id[]=151", new String[]{"Trophée", "Majeur", "Mineur", "Majeure", "Mineure"}, EQUIPMENT),
    DRAGODINDE("&model_family_id[]=1", new String[]{"Dragodinde"}, MONTURE);

    private String typeID;
    private String[] names;
    private SuperTypeEquipment superType;

    TypeEquipment(String typeID, String[] names, SuperTypeEquipment superType){
        this.typeID = typeID;
        this.names = names;
        this.superType = superType;
    }

    public String[] getNames(){ return names;}

    public SuperTypeEquipment getType(){
        return superType;
    }

    public String getTypeID(){
        return typeID;
    }
}

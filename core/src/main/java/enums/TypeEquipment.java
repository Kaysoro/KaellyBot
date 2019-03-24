package enums;

import util.Translator;

import static enums.SuperTypeEquipment.*;

/**
 * Created by steve on 07/06/2017.
 */
public enum TypeEquipment implements Type {

    AMULETTE("&type_id[]=1", "equip.amulette", EQUIPMENT),
    ARC("&type_id[]=2", "equip.arc", WEAPON),
    BAGUETTE("&type_id[]=3", "equip.baguette", WEAPON),
    BATON("&type_id[]=4", "equip.baton", WEAPON),
    DAGUE("&type_id[]=5", "equip.dague", WEAPON),
    EPEE("&type_id[]=6", "equip.epee", WEAPON),
    MARTEAU("&type_id[]=7", "equip.marteau", WEAPON),
    PELLE("&type_id[]=8", "equip.pelle", WEAPON),
    ANNEAU("&type_id[]=9", "equip.anneau", EQUIPMENT),
    CEINTURE("&type_id[]=10", "equip.ceinture", EQUIPMENT),
    BOTTES("&type_id[]=11", "equip.bottes", EQUIPMENT),
    CHAPEAU("&type_id[]=16", "equip.chapeau", EQUIPMENT),
    CAPE("&type_id[]=17", "equip.cape", EQUIPMENT),
    HACHE("&type_id[]=19", "equip.hache", WEAPON),
    OUTIL("&type_id[]=20", "equip.outil", WEAPON),
    PIOCHE("&type_id[]=21", "equip.pioche", WEAPON),
    FAUX("&type_id[]=22", "equip.faux", WEAPON),
    DOFUS("&type_id[]=23", "equip.dofus", EQUIPMENT),
    SACADOS("&type_id[]=81", "equip.sacados", EQUIPMENT),
    BOUCLIER("&type_id[]=82", "equip.bouclier", EQUIPMENT),
    PIERREDAME("&type_id[]=83", "equip.pierredame", WEAPON),
    FAMILIER("&type_id[]=121", "equip.familier", PET),
    TROPHEE("&type_id[]=151", "equip.trophee", EQUIPMENT),
    DRAGODINDE("&model_family_id[]=1", "equip.dragodinde", MONTURE),
    MULDO("&model_family_id[]=5", "equip.muldo", MONTURE),
    VOLKORNE("&model_family_id[]=6", "equip.volkorne", MONTURE);

    private String typeID;
    private String names;
    private SuperType superType;

    TypeEquipment(String typeID, String names, SuperTypeEquipment superType){
        this.typeID = typeID;
        this.names = names;
        this.superType = superType;
    }

    public String[] getNames(Language lg){ return Translator.getLabel(lg, names).split(";");}

    public SuperType getType(){
        return superType;
    }

    public String getTypeID(){
        return typeID;
    }
}

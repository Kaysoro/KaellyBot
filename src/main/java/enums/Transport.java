package enums;

import data.Coordinate;
import util.Translator;

public enum Transport {

    // Zaaps
    BORD_DE_LA_FORET_MALEFIQUE("transport.type.zaap", "transport.zone.amakna", "transport.souszone.bord_foret_malefique", new Coordinate(-1, 13), true),
    CHATEAU_D_AMAKNA("transport.type.zaap", "transport.zone.amakna", "transport.souszone.chateau_amakna", new Coordinate(3,-5), true),
    COIN_DES_BOUFTOUS("transport.type.zaap", "transport.zone.amakna", "transport.souszone.coin_bouftous", new Coordinate(5,7), true),
    MONTAGNE_DES_CRAQUELEURS("transport.type.zaap", "transport.zone.amakna", "transport.souszone.montagne_craqueleurs", new Coordinate(-5,8), true),
    PLAINE_DES_SCARAFEUILLES("transport.type.zaap", "transport.zone.amakna", "transport.souszone.plaine_scarafeuilles", new Coordinate(-1,24), true),
    PORT_DE_MADRESTAM("transport.type.zaap", "transport.zone.amakna", "transport.souszone.port_madrestam", new Coordinate(7,-4), true),
    VILLAGE_D_AMAKNA("transport.type.zaap", "transport.zone.amakna", "transport.souszone.village_amakna", new Coordinate(-2,0), true),
    CITE_D_ASTRUB("transport.type.zaap", "transport.zone.astrub", "transport.souszone.cite_astrub", new Coordinate(4,-19), true),
    SUFOKIA("transport.type.zaap", "transport.zone.baie_sufokia", "transport.souszone.sufokia", new Coordinate(13, 26), true),
    TEMPLE_DES_ALLIANCES("transport.type.zaap", "transport.zone.baie_sufokia", "transport.souszone.temple_alliances", new Coordinate(13, 35), true),
    RIVAGE_SUFOKIEN("transport.type.zaap", "transport.zone.baie_sufokia", "transport.souszone.rivage_sufokien", new Coordinate(10,22), true),
    BONTA("transport.type.zaap", "transport.zone.bonta", "transport.souszone.centre_ville", new Coordinate(-32,-56), true),
    VILLAGE_COTIER("transport.type.zaap", "transport.zone.ile_otomai", "transport.souszone.village_cotier", new Coordinate(-46,18), true),
    VILLAGE_DE_LA_CANOPEE("transport.type.zaap", "transport.zone.ile_otomai", "transport.souszone.village_canopee", new Coordinate(-54,16), true),
    ENTREE_DU_CHATEAU_DE_HAREBOURG("transport.type.zaap", "transport.zone.ile_frigost", "transport.souszone.entree_chateau_harebourg", new Coordinate(-67,-75), true),
    LA_BOURGADE("transport.type.zaap", "transport.zone.ile_frigost", "transport.souszone.bourgade", new Coordinate(-78,-41), true),
    VILLAGE_ENSEVELI("transport.type.zaap", "transport.zone.ile_frigost", "transport.souszone.village_enseveli", new Coordinate(-77,-73), true),
    PLAGE_DE_LA_TORTUE("transport.type.zaap", "transport.zone.ile_moon", "transport.souszone.plage_tortue", new Coordinate(35,12), true),
    ILE_DE_LA_CAWOTTE("transport.type.zaap", "transport.zone.ile_wabbit", "transport.souszone.ile_cawotte", new Coordinate(25,-4), true),
    LABORATOIRES_ABANDONNES("transport.type.zaap", "transport.zone.ile_wabbit", "transport.souszone.laboratoires_abandonnes", new Coordinate(27,-14), false),
    ROUTE_DES_ROULOTTES("transport.type.zaap", "transport.zone.landes_sidimote", "transport.souszone.route_roulottes", new Coordinate(-25,12), true),
    TERRES_DESACREES("transport.type.zaap", "transport.zone.landes_sidimote", "transport.souszone.terres_desacrees", new Coordinate(-15,25), true),
    VILLAGE_DES_ELEVEURS("transport.type.zaap", "transport.zone.montagne_koalaks", "transport.souszone.village_eleveurs", new Coordinate(-16,1), true),
    VILLAGE_D_AERDALA("transport.type.zaap", "transport.zone.pandala_air", "transport.souszone.village_aerdala", new Coordinate(17,-31), false),
    VILLAGE_D_AKWADALA("transport.type.zaap", "transport.zone.pandala_eau", "transport.souszone.village_akwadala", new Coordinate(23,-22), false),
    VILLAGE_DE_FEUDALA("transport.type.zaap", "transport.zone.pandala_feu", "transport.souszone.village_feudala", new Coordinate(29,-49), false),
    FAUBOURGS_DE_PANDALA("transport.type.zaap", "transport.zone.pandala_neutre", "transport.souszone.faubourgs_pandala", new Coordinate(26,-37), true),
    VILLAGE_DE_TERRDALA("transport.type.zaap", "transport.zone.pandala_terre", "transport.souszone.village_terrdala", new Coordinate(30,-38), false),
    CHAMPS_DE_CANIA("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.champs_cania", new Coordinate(-27,-36), true),
    LAC_DE_CANIA("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.lac_cania", new Coordinate(-3,-42), true),
    MASSIF_DE_CANIA("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.massif_cania", new Coordinate(-13,-28), true),
    PLAINE_DES_PORKASS("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.plaine_porkass", new Coordinate(-5,-23), true),
    PLAINES_ROCHEUSES("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.plaines_rocheuses", new Coordinate(-17,-47), true),
    ROUTES_ROCAILLEUSES("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.routes_rocailleuses", new Coordinate(-20,-20), true),
    VILLAGE_DES_KANIGS("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.village_kanigs", new Coordinate(0,-56), false),
    DUNE_DES_OSSEMENTS("transport.type.zaap", "transport.zone.saharach", "transport.souszone.dune_ossements", new Coordinate(15,-58), true),
    BERCEAU("transport.type.zaap", "transport.zone.tainela", "transport.souszone.berceau", new Coordinate(1,-32), true),
    VILLAGE_DES_DOPEULS("transport.type.zaap", "transport.zone.territoire_dopeuls", "transport.souszone.village_dopeuls", new Coordinate(-34,-8), false),
    VILLAGE_DES_BRIGANDINS("transport.type.zaap", "transport.zone.village_brigandins", "transport.souszone.village_brigandins", new Coordinate(-16,-24), false),
    VILLAGE_DES_ZOTHS("transport.type.zaap", "transport.zone.village_zoths", "transport.souszone.village_zoths", new Coordinate(-53,18), false),
    BRAKMAR("transport.type.zaap", "transport.zone.brakmar", "transport.souszone.centre_ville", new Coordinate(-26,35), true);

    private String type;
    private String zone;
    private String sousZone;
    private Coordinate coordinate;
    private boolean isFreeAccess;

    Transport(String type, String zone, String sousZone, Coordinate coordinate, boolean isFreeAccess) {
        this.type = type;
        this.zone = zone;
        this.sousZone = sousZone;
        this.coordinate = coordinate;
        this.isFreeAccess = isFreeAccess;
    }

    public String getType(Language lg) {
        return Translator.getLabel(lg, type);
    }

    public String getZone(Language lg) {
        return Translator.getLabel(lg, zone);
    }

    public String getSousZone(Language lg) {
        return Translator.getLabel(lg, sousZone);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean isFreeAccess() {
        return isFreeAccess;
    }

    public String toDiscordString(Language lg){
        return getZone(lg) + " (" + getSousZone(lg) + ") **" + getCoordinate() + "**";
    }
}

package com.github.kaysoro.kaellybot.portal.model.constants;

import com.github.kaysoro.kaellybot.portal.model.entity.Position;

public enum Transport {

    // Zaaps
    BORD_DE_LA_FORET_MALEFIQUE("transport.type.zaap", "transport.area.amakna", "transport.souszone.bord_foret_malefique", Position.of(-1, 13), true),
    CHATEAU_D_AMAKNA("transport.type.zaap", "transport.area.amakna", "transport.souszone.chateau_amakna", Position.of(3,-5), true),
    COIN_DES_BOUFTOUS("transport.type.zaap", "transport.area.amakna", "transport.souszone.coin_bouftous", Position.of(5,7), true),
    MONTAGNE_DES_CRAQUELEURS("transport.type.zaap", "transport.area.amakna", "transport.souszone.montagne_craqueleurs", Position.of(-5, -8), true),
    PLAINE_DES_SCARAFEUILLES("transport.type.zaap", "transport.area.amakna", "transport.souszone.plaine_scarafeuilles", Position.of(-1,24), true),
    PORT_DE_MADRESTAM("transport.type.zaap", "transport.area.amakna", "transport.souszone.port_madrestam", Position.of(7,-4), true),
    VILLAGE_D_AMAKNA("transport.type.zaap", "transport.area.amakna", "transport.souszone.village_amakna", Position.of(-2,0), true),
    CITE_D_ASTRUB("transport.type.zaap", "transport.area.astrub", "transport.souszone.cite_astrub", Position.of(5,-18), true),
    SUFOKIA("transport.type.zaap", "transport.area.baie_sufokia", "transport.souszone.sufokia", Position.of(13, 26), true),
    TEMPLE_DES_ALLIANCES("transport.type.zaap", "transport.area.baie_sufokia", "transport.souszone.temple_alliances", Position.of(13, 35), true),
    RIVAGE_SUFOKIEN("transport.type.zaap", "transport.area.baie_sufokia", "transport.souszone.rivage_sufokien", Position.of(10,22), true),
    BONTA("transport.type.zaap", "transport.area.bonta", "transport.souszone.centre_ville", Position.of(-32,-56), true),
    VILLAGE_COTIER("transport.type.zaap", "transport.area.ile_otomai", "transport.souszone.village_cotier", Position.of(-46,18), true),
    VILLAGE_DE_LA_CANOPEE("transport.type.zaap", "transport.area.ile_otomai", "transport.souszone.village_canopee", Position.of(-54,16), true),
    LA_BOURGADE("transport.type.zaap", "transport.area.ile_frigost", "transport.souszone.bourgade", Position.of(-78,-41), true),
    VILLAGE_ENSEVELI("transport.type.zaap", "transport.area.ile_frigost", "transport.souszone.village_enseveli", Position.of(-77,-73), true),
    PLAGE_DE_LA_TORTUE("transport.type.zaap", "transport.area.ile_moon", "transport.souszone.plage_tortue", Position.of(35,12), true),
    ILE_DE_LA_CAWOTTE("transport.type.zaap", "transport.area.ile_wabbit", "transport.souszone.ile_cawotte", Position.of(25,-4), true),
    LABORATOIRES_ABANDONNES("transport.type.zaap", "transport.area.ile_wabbit", "transport.souszone.laboratoires_abandonnes", Position.of(27,-14), false),
    ROUTE_DES_ROULOTTES("transport.type.zaap", "transport.area.landes_sidimote", "transport.souszone.route_roulottes", Position.of(-25,12), true),
    TERRES_DESACREES("transport.type.zaap", "transport.area.landes_sidimote", "transport.souszone.terres_desacrees", Position.of(-15,25), true),
    VILLAGE_DES_ELEVEURS("transport.type.zaap", "transport.area.montagne_koalaks", "transport.souszone.village_eleveurs", Position.of(-16,1), true),
    VILLAGE_D_AERDALA("transport.type.zaap", "transport.area.pandala_air", "transport.souszone.village_aerdala", Position.of(17,-31), false),
    VILLAGE_D_AKWADALA("transport.type.zaap", "transport.area.pandala_eau", "transport.souszone.village_akwadala", Position.of(23,-22), false),
    VILLAGE_DE_FEUDALA("transport.type.zaap", "transport.area.pandala_feu", "transport.souszone.village_feudala", Position.of(29,-49), false),
    FAUBOURGS_DE_PANDALA("transport.type.zaap", "transport.area.pandala_neutre", "transport.souszone.faubourgs_pandala", Position.of(26,-37), true),
    VILLAGE_DE_TERRDALA("transport.type.zaap", "transport.area.pandala_terre", "transport.souszone.village_terrdala", Position.of(30,-38), false),
    CHAMPS_DE_CANIA("transport.type.zaap", "transport.area.plaines_cania", "transport.souszone.champs_cania", Position.of(-27,-36), true),
    LAC_DE_CANIA("transport.type.zaap", "transport.area.plaines_cania", "transport.souszone.lac_cania", Position.of(-3,-42), true),
    MASSIF_DE_CANIA("transport.type.zaap", "transport.area.plaines_cania", "transport.souszone.massif_cania", Position.of(-13,-28), true),
    PLAINE_DES_PORKASS("transport.type.zaap", "transport.area.plaines_cania", "transport.souszone.plaine_porkass", Position.of(-5,-23), true),
    PLAINES_ROCHEUSES("transport.type.zaap", "transport.area.plaines_cania", "transport.souszone.plaines_rocheuses", Position.of(-17,-47), true),
    ROUTES_ROCAILLEUSES("transport.type.zaap", "transport.area.plaines_cania", "transport.souszone.routes_rocailleuses", Position.of(-20,-20), true),
    VILLAGE_DES_KANIGS("transport.type.zaap", "transport.area.plaines_cania", "transport.souszone.village_kanigs", Position.of(0,-56), false),
    DUNE_DES_OSSEMENTS("transport.type.zaap", "transport.area.saharach", "transport.souszone.dune_ossements", Position.of(15,-58), true),
    BERCEAU("transport.type.zaap", "transport.area.tainela", "transport.souszone.berceau", Position.of(1,-32), true),
    VILLAGE_DES_DOPEULS("transport.type.zaap", "transport.area.territoire_dopeuls", "transport.souszone.village_dopeuls", Position.of(-34,-8), false),
    VILLAGE_DES_BRIGANDINS("transport.type.zaap", "transport.area.village_brigandins", "transport.souszone.village_brigandins", Position.of(-16,-24), false),
    VILLAGE_DES_ZOTHS("transport.type.zaap", "transport.area.village_zoths", "transport.souszone.village_zoths", Position.of(-53,18), false),
    BRAKMAR("transport.type.zaap", "transport.area.brakmar", "transport.souszone.centre_ville", Position.of(-26,35), true),

    //Foreuses
    FOREUSE_LABORATOIRE_ABANDONNE("transport.type.foreuse", "", "", Position.of(27,-14), true),
    FOREUSE_PANDALA_NEUTRE("transport.type.foreuse", "", "", Position.of(28,-30), true),
    FOREUSE_PLAGE_MOON("transport.type.foreuse", "", "", Position.of(36,4), true),
    FOREUSE_FEUILLAGE_ARBRE_HAKAM("transport.type.foreuse", "", "", Position.of(-53,17), true),
    FOREUSE_MINE_MAKSAGE("transport.type.foreuse", "Frigost", "La Forêt des Pins Perdus", Position.of(-55,-64), true),
    FOREUSE_MILIFUTAIE("transport.type.foreuse", "Continent d'Amakna", "", Position.of(2,4), true),
    FOREUSE_KARTONPATH("transport.type.foreuse", "Continent d'Amakna", "", Position.of(18, 11), true),
    FOREUSE_TERRITOIRE_PORCOS("transport.type.foreuse", "Continent d'Amakna", "", Position.of(0,33), true),
    FOREUSE_BAIE_CANIA("transport.type.foreuse", "Continent d'Amakna", "", Position.of(-29,-12), true),
    FOREUSE_GROTTE_HURIE("transport.type.foreuse", "Continent d'Amakna", "Vallée Morh kitu", Position.of(-18,14), true),
    FOREUSE_MINE_HERALE("transport.type.foreuse", "Continent d'Amakna", "Forêt amakna", Position.of(5,19), true),
    FOREUSE_MINE_ISTAIRAMEUR("transport.type.foreuse", "Continent d'Amakna", "Montagne Basse des Craqueleurs", Position.of(-3,9), true),
    FOREUSE_MINE_HIPOUCE("transport.type.foreuse", "Continent d'Amakna", "lande sidimote", Position.of(-23,25), true),
    FOREUSE_DENTS_PIERRE("transport.type.foreuse", "Continent d'Amakna", "", Position.of(-4,-53), true),

    // Diligence d'atrub
    DILIGENCE_ASTRUB("transport.type.diligence", "", "", Position.of(0, -18), false),
    DILIGENCE_BAIE_CANIA("transport.type.diligence", "", "", Position.of(-29,-12), false),
    DILIGENCE_DOMAINE_FUNGUS("transport.type.diligence", "", "", Position.of(-12,29), false),
    DILIGENCE_CIMETIERE_HEROS("transport.type.diligence", "", "", Position.of(-21,-58), false),

    // Transport Brigandin
    BRIGANDIN_RIVIERE_KAWAII("transport.type.brigandin","","",Position.of(6,-1), true),
    BRIGANDIN_PORTE_BONTA("transport.type.brigandin","","",Position.of(-31,-48), true),
    BRIGANDIN_PORTE_BRAKMAR("transport.type.brigandin","","",Position.of(-26,29), true),
    BRIGANDIN_VILLAGE_BWORKS("transport.type.brigandin","","",Position.of(-1,8), true),
    BRIGANDIN_PRESQU_ILE_DRAGOEUFS("transport.type.brigandin","","",Position.of(-3,27), true),
    BRIGANDIN_VILLAGE_BRIGANDINS("transport.type.brigandin","","",Position.of(-17,-26), true),
    BRIGANDIN_PENINSULE_GELEES("transport.type.brigandin","","",Position.of(11,30), true),
    BRIGANDIN_PANDALA("transport.type.brigandin","","",Position.of(18,-39), true),
    BRIGANDIN_ILE_MINOTOROR("transport.type.brigandin","","",Position.of(-41,-18), true),
    BRIGANDIN_ILE_MOON("transport.type.brigandin","","",Position.of(35,4), true),
    BRIGANDIN_ILE_WABBITS("transport.type.brigandin","","",Position.of(23,-3), true),
    BRIGANDIN_VILLAGE_ZOTHS("transport.type.brigandin","","",Position.of(-53,15), true),

    // Transport Frigostien
    TRANSPORTEUR_FRIGOSTIEN_CHAMPS_GLACE("transport.type.transporteur_frigostien", "", "", Position.of(-68,-34), false),
    TRANSPORTEUR_FRIGOSTIEN_BERCEAU_ALMA("transport.type.transporteur_frigostien", "", "", Position.of(-56,-74), false),
    TRANSPORTEUR_FRIGOSTIEN_LARMES_OURONIGRIDE("transport.type.transporteur_frigostien", "", "", Position.of(-69,-87), false),
    TRANSPORTEUR_FRIGOSTIEN_CREVASSE_PERGE("transport.type.transporteur_frigostien", "", "", Position.of(-78,-82), false),
    TRANSPORTEUR_FRIGOSTIEN_FORET_PETRIFIEE("transport.type.transporteur_frigostien", "", "", Position.of(-76,-66), false),
    TRANSPORTEUR_FRIGOSTIEN_CROCS_VERRE("transport.type.transporteur_frigostien", "", "", Position.of(-66,-65), false),
    TRANSPORTEUR_FRIGOSTIEN_MONT_TORRIDEAU("transport.type.transporteur_frigostien", "", "", Position.of(-63,-72), false),
    TRANSPORTEUR_FRIGOSTIEN_PLAINE_SAKAI("transport.type.transporteur_frigostien", "", "", Position.of(-55,-41), false),

    // Ski Frigost
    SKI_BAMBOU("transport.type.ski", "Frigost" , "Entrée de la Bourgade", Position.of(-76, -46), false),
    SKI_TREMBLE("transport.type.ski", "Frigost" , "Serre du Royalmouth", Position.of(-84, -49), false),
    SKI_ORME("transport.type.ski", "Frigost" , "Excavation du Mansot Royal", Position.of(-64, -55), false),
    SKI_CHENE("transport.type.ski", "Frigost" , "Au transporteur frigostien", Position.of(-68,-34), false),

    // Scaéroplane
    SCAEROPLANE_PLAINES_HERBEUSES("transport.type.scaeroplane","","",Position.of(-56,22), false),
    SCAEROPLANE_VILLAGE_ELEVEURS("transport.type.scaeroplane","","",Position.of(-57,4), false),
    SCAEROPLANE_VILLAGE_COTIER("transport.type.scaeroplane","","",Position.of(-49,14), false),
    SCAEROPLANE_ARBRE_HAKAM("transport.type.scaeroplane","","",Position.of(-54,19), false);

    //Zaapis (Bonta, Brâkmar, Sufokia, Marches magmatiques, Saharach)
    // TODO

    private String type;
    private String area;
    private String subArea;
    private Position position;
    private boolean isAvailableUnderConditions;

    Transport(String type, String area, String subArea, Position position, boolean isAvailableUnderConditions) {
        this.type = type;
        this.area = area;
        this.subArea = subArea;
        this.position = position;
        this.isAvailableUnderConditions = isAvailableUnderConditions;
    }

    public String getType() {
        return type;
    }

    public String getArea() {
        return area;
    }

    public String getSubArea() {
        return subArea;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isAvailableUnderConditions() {
        return isAvailableUnderConditions;
    }
}
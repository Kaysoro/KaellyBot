package com.github.kaysoro.kaellybot.portal.model.entity;

public enum Transport {

        // Zaaps
        BORD_DE_LA_FORET_MALEFIQUE("transport.type.zaap", "transport.zone.amakna", "transport.souszone.bord_foret_malefique", Position.of(-1, 13), true),
        CHATEAU_D_AMAKNA("transport.type.zaap", "transport.zone.amakna", "transport.souszone.chateau_amakna", Position.of(3,-5), true),
        COIN_DES_BOUFTOUS("transport.type.zaap", "transport.zone.amakna", "transport.souszone.coin_bouftous", Position.of(5,7), true),
        MONTAGNE_DES_CRAQUELEURS("transport.type.zaap", "transport.zone.amakna", "transport.souszone.montagne_craqueleurs", Position.of(-5,8), true),
        PLAINE_DES_SCARAFEUILLES("transport.type.zaap", "transport.zone.amakna", "transport.souszone.plaine_scarafeuilles", Position.of(-1,24), true),
        PORT_DE_MADRESTAM("transport.type.zaap", "transport.zone.amakna", "transport.souszone.port_madrestam", Position.of(7,-4), true),
        VILLAGE_D_AMAKNA("transport.type.zaap", "transport.zone.amakna", "transport.souszone.village_amakna", Position.of(-2,0), true),
        CITE_D_ASTRUB("transport.type.zaap", "transport.zone.astrub", "transport.souszone.cite_astrub", Position.of(4,-19), true),
        SUFOKIA("transport.type.zaap", "transport.zone.baie_sufokia", "transport.souszone.sufokia", Position.of(13, 26), true),
        TEMPLE_DES_ALLIANCES("transport.type.zaap", "transport.zone.baie_sufokia", "transport.souszone.temple_alliances", Position.of(13, 35), true),
        RIVAGE_SUFOKIEN("transport.type.zaap", "transport.zone.baie_sufokia", "transport.souszone.rivage_sufokien", Position.of(10,22), true),
        BONTA("transport.type.zaap", "transport.zone.bonta", "transport.souszone.centre_ville", Position.of(-32,-56), true),
        VILLAGE_COTIER("transport.type.zaap", "transport.zone.ile_otomai", "transport.souszone.village_cotier", Position.of(-46,18), true),
        VILLAGE_DE_LA_CANOPEE("transport.type.zaap", "transport.zone.ile_otomai", "transport.souszone.village_canopee", Position.of(-54,16), true),
        LA_BOURGADE("transport.type.zaap", "transport.zone.ile_frigost", "transport.souszone.bourgade", Position.of(-78,-41), true),
        VILLAGE_ENSEVELI("transport.type.zaap", "transport.zone.ile_frigost", "transport.souszone.village_enseveli", Position.of(-77,-73), true),
        PLAGE_DE_LA_TORTUE("transport.type.zaap", "transport.zone.ile_moon", "transport.souszone.plage_tortue", Position.of(35,12), true),
        ILE_DE_LA_CAWOTTE("transport.type.zaap", "transport.zone.ile_wabbit", "transport.souszone.ile_cawotte", Position.of(25,-4), true),
        LABORATOIRES_ABANDONNES("transport.type.zaap", "transport.zone.ile_wabbit", "transport.souszone.laboratoires_abandonnes", Position.of(27,-14), false),
        ROUTE_DES_ROULOTTES("transport.type.zaap", "transport.zone.landes_sidimote", "transport.souszone.route_roulottes", Position.of(-25,12), true),
        TERRES_DESACREES("transport.type.zaap", "transport.zone.landes_sidimote", "transport.souszone.terres_desacrees", Position.of(-15,25), true),
        VILLAGE_DES_ELEVEURS("transport.type.zaap", "transport.zone.montagne_koalaks", "transport.souszone.village_eleveurs", Position.of(-16,1), true),
        VILLAGE_D_AERDALA("transport.type.zaap", "transport.zone.pandala_air", "transport.souszone.village_aerdala", Position.of(17,-31), false),
        VILLAGE_D_AKWADALA("transport.type.zaap", "transport.zone.pandala_eau", "transport.souszone.village_akwadala", Position.of(23,-22), false),
        VILLAGE_DE_FEUDALA("transport.type.zaap", "transport.zone.pandala_feu", "transport.souszone.village_feudala", Position.of(29,-49), false),
        FAUBOURGS_DE_PANDALA("transport.type.zaap", "transport.zone.pandala_neutre", "transport.souszone.faubourgs_pandala", Position.of(26,-37), true),
        VILLAGE_DE_TERRDALA("transport.type.zaap", "transport.zone.pandala_terre", "transport.souszone.village_terrdala", Position.of(30,-38), false),
        CHAMPS_DE_CANIA("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.champs_cania", Position.of(-27,-36), true),
        LAC_DE_CANIA("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.lac_cania", Position.of(-3,-42), true),
        MASSIF_DE_CANIA("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.massif_cania", Position.of(-13,-28), true),
        PLAINE_DES_PORKASS("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.plaine_porkass", Position.of(-5,-23), true),
        PLAINES_ROCHEUSES("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.plaines_rocheuses", Position.of(-17,-47), true),
        ROUTES_ROCAILLEUSES("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.routes_rocailleuses", Position.of(-20,-20), true),
        VILLAGE_DES_KANIGS("transport.type.zaap", "transport.zone.plaines_cania", "transport.souszone.village_kanigs", Position.of(0,-56), false),
        DUNE_DES_OSSEMENTS("transport.type.zaap", "transport.zone.saharach", "transport.souszone.dune_ossements", Position.of(15,-58), true),
        BERCEAU("transport.type.zaap", "transport.zone.tainela", "transport.souszone.berceau", Position.of(1,-32), true),
        VILLAGE_DES_DOPEULS("transport.type.zaap", "transport.zone.territoire_dopeuls", "transport.souszone.village_dopeuls", Position.of(-34,-8), false),
        VILLAGE_DES_BRIGANDINS("transport.type.zaap", "transport.zone.village_brigandins", "transport.souszone.village_brigandins", Position.of(-16,-24), false),
        VILLAGE_DES_ZOTHS("transport.type.zaap", "transport.zone.village_zoths", "transport.souszone.village_zoths", Position.of(-53,18), false),
        BRAKMAR("transport.type.zaap", "transport.zone.brakmar", "transport.souszone.centre_ville", Position.of(-26,35), true);

        //Foreuses
        // TODO

        //Zaapis
        // TODO

        // Truc de la Cité d'atrub
        //TODO

        // Transport Brigandin
        // TODO

        // Transport Frigostien
        // TODO

        //

        private String type;
        private String zone;
        private String sousZone;
        private Position position;
        private boolean isFreeAccess;

        Transport(String type, String zone, String sousZone, Position position, boolean isFreeAccess) {
            this.type = type;
            this.zone = zone;
            this.sousZone = sousZone;
            this.position = position;
            this.isFreeAccess = isFreeAccess;
        }

        public Position getPosition() {
            return position;
        }

        public boolean isFreeAccess() {
            return isFreeAccess;
        }
}

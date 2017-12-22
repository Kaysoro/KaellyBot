package enums;

import util.Translator;

import static enums.SuperTypeResource.*;

/**
 * Created by steve on 07/06/2017.
 */
public enum TypeResource implements Type {

    //HAVRE_SAC("", "resource.havre_sac", HAVEN_BAG),

    HARNACHEMENT("", "resource.harnachement", HARNESS),

    IDOLE("", "resource.idole", IDOL),

    AILE("&type_id[]=104", "resource.aile", RESOURCE),
    ALLIAGE("&type_id[]=40", "resource.alliage", RESOURCE),
    BOIS("&type_id[]=38", "resource.bois", RESOURCE),
    BOURGEON("&type_id[]=108", "resource.bourgeon", RESOURCE),
    CARAPACE("&type_id[]=107", "resource.carapace", RESOURCE),
    CARTE("&type_id[]=174", "resource.carte", RESOURCE),
    CHAMPIGNON("&type_id[]=119", "resource.champignon", RESOURCE),
    CLEF("&type_id[]=84", "resource.clef", RESOURCE),
    COQUILLE("&type_id[]=111", "resource.coquille", RESOURCE),
    CUIR("&type_id[]=56", "resource.cuir", RESOURCE),
    CEREALE("&type_id[]=34", "resource.cereale", RESOURCE),
    EMBALLAGE("&type_id[]=154", "resource.emballage", RESOURCE),
    ESSENCE_GARDIEN_DONJON("&type_id[]=167", "resource.essence_gardien_donjon", RESOURCE),
    ETOFFE("&type_id[]=55", "resource.etoffe", RESOURCE),
    FANTOME_FAMILIER("&type_id[]=90", "resource.fantome_familier", RESOURCE),
    FANTOME_MONTILIER("&type_id[]=124", "resource.fantome_montilier", RESOURCE),
    FARINE("&type_id[]=52", "resource.farine", RESOURCE),
    FLEUR("&type_id[]=35", "resource.fleur", RESOURCE),
    FRAGMENT_CARTE("&type_id[]=175", "resource.fragment_carte", RESOURCE),
    FRUIT("&type_id[]=46", "resource.fruit", RESOURCE),
    GALET("&type_id[]=152", "resource.galet", RESOURCE),
    GELEE("&type_id[]=110", "resource.gelee", RESOURCE),
    GRAINE("&type_id[]=58", "resource.graine", RESOURCE),
    HUILE("&type_id[]=60", "resource.huile", RESOURCE),
    LAINE("&type_id[]=57", "resource.laine", RESOURCE),
    LEGUME("&type_id[]=68", "resource.legume", RESOURCE),
    MATERIEL_ALCHIMIE("&type_id[]=71", "resource.materiel_alchimie", RESOURCE),
    METARIA("&type_id[]=66", "resource.metaria", RESOURCE),
    MINERAI("&type_id[]=39", "resource.minerai", RESOURCE),
    NOWEL("&type_id[]=153", "resource.nowel", RESOURCE),
    OEIL("&type_id[]=109", "resource.oeil", RESOURCE),
    OEUF("&type_id[]=105", "resource.oeuf", RESOURCE),
    ORBE_FORGEMAGIE("&type_id[]=189", "resource.orbe_forgemagie", RESOURCE),
    OREILLE("&type_id[]=106", "resource.oreille", RESOURCE),
    OS("&type_id[]=47", "resource.os", RESOURCE),
    PATTE("&type_id[]=103", "resource.patte", RESOURCE),
    PEAU("&type_id[]=59", "resource.peau", RESOURCE),
    PELUCHE("&type_id[]=61", "resource.peluche", RESOURCE),
    PIERRE_BRUTE("&type_id[]=51", "resource.pierre_brute", RESOURCE),
    PIERRE_PRECIEUSE("&type_id[]=50", "resource.pierre_precieuse", RESOURCE),
    PLANCHE("&type_id[]=95", "resource.planche", RESOURCE),
    PLANTE("&type_id[]=36", "resource.plante", RESOURCE),
    PLUME("&type_id[]=53", "resource.plume", RESOURCE),
    POIL("&type_id[]=54", "resource.poil", RESOURCE),
    POISSON("&type_id[]=41", "resource.poisson", RESOURCE),
    POTION_FORGEMAGIE("&type_id[]=26", "resource.potion_forgemagie", RESOURCE),
    POUDRE("&type_id[]=48", "resource.poudre", RESOURCE),
    PREPARATION("&type_id[]=179", "resource.preparation", RESOURCE),
    QUEUE("&type_id[]=65", "resource.queue", RESOURCE),
    RACINE("&type_id[]=98", "resource.racine", RESOURCE),
    RESSOURCES_DIVERSES("&type_id[]=15", "resource.ressources_diverses", RESOURCE),
    RUNE_FORGEMAGIE("&type_id[]=78", "resource.rune_forgemagie", RESOURCE),
    SOUVENIR("&type_id[]=125", "resource.souvenir", RESOURCE),
    SUBSTRAT("&type_id[]=183", "resource.substrat", RESOURCE),
    SEVE("&type_id[]=185", "resource.seve", RESOURCE),
    TEINTURE("&type_id[]=70", "resource.teinture", RESOURCE),
    VIANDE("&type_id[]=63", "resource.viande", RESOURCE),
    VETEMENT("&type_id[]=164", "resource.vetement", RESOURCE),
    ECORCE("&type_id[]=96", "resource.ecorce", RESOURCE),

    BIERE("&type_id[]=37", "resource.biere", CONSUMABLE),
    BOISSON("&type_id[]=79", "resource.boisson", CONSUMABLE),
    BOITE_FRAGMENTS("&type_id[]=176", "resource.boite_fragments", CONSUMABLE),
    COFFRE("&type_id[]=172", "resource.coffre", CONSUMABLE),
    CONTENEUR("&type_id[]=184", "resource.conteneur", CONSUMABLE),
    FRIANDISE("&type_id[]=42", "resource.friandise", CONSUMABLE),
    FEE_ARTIFICE ("&type_id[]=74", "resource.fee_artifice", CONSUMABLE),
    MIMIBIOTE ("&type_id[]=166", "resource.mimibiote", CONSUMABLE),
    OBJET_ELEVAGE ("&type_id[]=93", "resource.objet_elevage", CONSUMABLE),
    OBJET_UTILISABLE ("&type_id[]=94", "resource.objet_utilisable", CONSUMABLE),
    OEUF_FAMILIER ("&type_id[]=72", "resource.oeuf_familier", CONSUMABLE),
    PAIN ("&type_id[]=33", "resource.pain", CONSUMABLE),
    PARCHEMIN_ATTITUDE ("&type_id[]=173", "resource.parchemin_attitude", CONSUMABLE),
    PARCHEMIN_XP ("&type_id[]=13", "resource.parchemin_xp", CONSUMABLE),
    PARCHEMIN_EMOTE ("&type_id[]=188", "resource.parchemin_emote", CONSUMABLE),
    PARCHEMIN_CARACTERISTIQUE ("&type_id[]=76", "resource.parchemin_caracteristique", CONSUMABLE),
    PARCHEMIN_RECHERCHE("&type_id[]=87", "resource.parchemin_recherche", CONSUMABLE),
    PARCHEMIN_SORTILEGE ("&type_id[]=75", "resource.parchemin_sortilege", CONSUMABLE),
    PARCHEMIN_TITRE ("&type_id[]=200", "resource.parchemin_titre", CONSUMABLE),
    PIERRE_MAGIQUE("&type_id[]=88", "resource.pierre_magique", CONSUMABLE),
    POISSON_CONSOMMABLE("&type_id[]=49", "resource.poisson_consommable", CONSUMABLE),
    POTION("&type_id[]=12", "resource.potion", CONSUMABLE),
    POTION_OUBLI_PERCEPTEUR ("&type_id[]=86", "resource.potion_oubli_percepteur", CONSUMABLE),
    POTION_CONQUETE ("&type_id[]=165", "resource.potion_conquete", CONSUMABLE),
    POTION_FAMILIER ("&type_id[]=116", "resource.potion_familier", CONSUMABLE),
    POTION_MONTILIER ("&type_id[]=122", "resource.potion_montilier", CONSUMABLE),
    POTION_TELEPORTATION("&type_id[]=43", "resource.potion_teleportation", CONSUMABLE),
    VIANDE_CONSOMMABLE ("&type_id[]=69", "resource.viande_consommable", CONSUMABLE);

    private String typeID;
    private String names;
    private SuperType superType;

    TypeResource(String typeID, String names, SuperTypeResource superType){
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

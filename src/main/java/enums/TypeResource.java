package enums;

import static enums.SuperTypeResource.*;

/**
 * Created by steve on 07/06/2017.
 */
public enum TypeResource {

    HAVRE_SAC("", new String[]{"Havre-sac"}, HAVEN_BAG),

    HARNACHEMENT("", new String[]{"Harnachement"}, HARNESS),

    IDOLE("", new String[]{"Idole", "Aroumb", "Bihilète", "Binar", "Boble", "Butor", "Cafra", "Corrode", "Critus",
            "Dagob", "Djim", "Domo", "Dynamo", "Horize", "Hoskar", "Hulhu", "Korria", "Kyoub", "Leukide",
            "Muta", "Nahuatl", "Nékinéko", "Nyan", "Nyoro", "Ougah", "Pého", "Pénitent", "Pétunia", "Pikmi",
            "Proxima", "Sak", "Teleb", "Ultram", "Vaude", "Yoche", "Zaihn"}, IDOL),

    AILE("&type_id[]=104", new String[]{"Aile"}, RESOURCE),
    ALLIAGE("&type_id[]=40", new String[]{"Alliage"}, RESOURCE),
    BOIS("&type_id[]=38", new String[]{"Bois", "Sourcil"}, RESOURCE),
    BOURGEON("&type_id[]=108", new String[]{"Bourgeon"}, RESOURCE),
    CARAPACE("&type_id[]=107", new String[]{"Carapace"}, RESOURCE),
    CARTE("&type_id[]=174", new String[]{"Carte"}, RESOURCE),
    CHAMPIGNON("&type_id[]=119", new String[]{"Champignon", "Mycose", "Lamelle", "Pédoncule", "Volve"}, RESOURCE),
    CLEF("&type_id[]=84", new String[]{"Clef"}, RESOURCE),
    COQUILLE("&type_id[]=111", new String[]{"Coquille"}, RESOURCE),
    CUIR("&type_id[]=56", new String[]{"Cuir"}, RESOURCE),
    CEREALE("&type_id[]=34", new String[]{"Céréale"}, RESOURCE),
    EMBALLAGE("&type_id[]=154", new String[]{"Emballage"}, RESOURCE),
    ESSENCE_GARDIEN_DONJON("&type_id[]=167", new String[]{"Essence"}, RESOURCE),
    ETOFFE("&type_id[]=55", new String[]{"Étoffe", "Fibre", "Tissu"}, RESOURCE),
    FANTOME_FAMILIER("&type_id[]=90", new String[]{"Fantôme"}, RESOURCE),
    FANTOME_MONTILIER("&type_id[]=124", new String[]{"Fantôme"}, RESOURCE),
    FARINE("&type_id[]=52", new String[]{"Farine"}, RESOURCE),
    FLEUR("&type_id[]=35", new String[]{"Fleur", "Pétale"}, RESOURCE),
    FRAGMENT_CARTE("&type_id[]=175", new String[]{"Fragment de carte", "Fragment"}, RESOURCE),
    FRUIT("&type_id[]=46", new String[]{"Fruit", "Noix"}, RESOURCE),
    GALET("&type_id[]=152", new String[]{"Galet"}, RESOURCE),
    GELEE("&type_id[]=110", new String[]{"Gelée"}, RESOURCE),
    GRAINE("&type_id[]=58", new String[]{"Graine", "Noix"}, RESOURCE),
    HUILE("&type_id[]=60", new String[]{"Huile"}, RESOURCE),
    LAINE("&type_id[]=57", new String[]{"Laine"}, RESOURCE),
    LEGUME("&type_id[]=68", new String[]{"Légume"}, RESOURCE),
    MATERIEL_ALCHIMIE("&type_id[]=71", new String[]{"Fiole", "Tube", "Bocal", "Breuvage"}, RESOURCE),
    METARIA("&type_id[]=66", new String[]{"Metaria"}, RESOURCE),
    MINERAI("&type_id[]=39", new String[]{"Minerai"}, RESOURCE),
    NOWEL("&type_id[]=153", new String[]{"Paquet", "Patron", "Moule"}, RESOURCE),
    OEIL("&type_id[]=109", new String[]{"Œil", "Oeil", "Iris", "Cornée"}, RESOURCE),
    OEUF("&type_id[]=105", new String[]{"Œuf", "Oeuf"}, RESOURCE),
    ORBE_FORGEMAGIE("&type_id[]=189", new String[]{"Orbe"}, RESOURCE),
    OREILLE("&type_id[]=106", new String[]{"Oreille"}, RESOURCE),
    OS("&type_id[]=47", new String[]{"Pince", "Canine", "Griffe", "Bec", "Molaire", "Dent", "Épine", "Incisive", "Crâne",
    "Dentier", "Pic", "Corne", "Ongle", "Pointe", "Os", "Dard", "Scapula", "Sabot", "Fémur", "Péroné", "Radius", "Défense",
    "Tibia", "Rotule", "Cubitus", "Fragment", "Crocs"}, RESOURCE),
    PATTE("&type_id[]=103", new String[]{"Patte", "Serre"}, RESOURCE),
    PEAU("&type_id[]=59", new String[]{"Peau", "Scalp", "Écaille", "Fragment"}, RESOURCE),
    PELUCHE("&type_id[]=61", new String[]{"Peluche"}, RESOURCE),
    PIERRE_BRUTE("&type_id[]=51", new String[]{"Pierre", "Fragment", "Résidu", "Coeur", "Corail"}, RESOURCE),
    PIERRE_PRECIEUSE("&type_id[]=50", new String[]{"Corne", "Joyau", "Boule", "Pierre", "Ambre", "Eclat", "Fragment",
    "Rune"}, RESOURCE),
    PLANCHE("&type_id[]=95", new String[]{"Planche"}, RESOURCE),
    PLANTE("&type_id[]=36", new String[]{"Feuille", "Sépale", "Bulbe"}, RESOURCE),
    PLUME("&type_id[]=53", new String[]{"Plume", "Duvet"}, RESOURCE),
    POIL("&type_id[]=54", new String[]{"Barbe", "Sourcil", "Crinière", "Poil", "Cheveux", "Moustache", "Touffe", "Tresse",
    "Perruque", "Duvet"}, RESOURCE),
    POISSON("&type_id[]=41", new String[]{"Greuvette", "Sardine", "Poisson", "Goujon", "Crabe", "Truite", "Morue", "Carpe", "Kralamoure",
            "Anguille", "Dorade", "Perche", "Raie", "Lotte", "Requin", "Bar", "Tanche", "Espadon", "Poisskaille"}, RESOURCE),
    POISSON_VIDE("&type_id[]=62", new String[]{"Greuvette", "Sardine", "Poisson", "Goujon", "Crabe", "Truite", "Morue", "Carpe", "Kralamoure",
            "Anguille", "Dorade", "Perche", "Raie", "Lotte", "Requin", "Bar", "Tanche", "Espadon", "Poisskaille"}, RESOURCE),
    POTION_FORGEMAGIE("&type_id[]=26", new String[]{"Potion"}, RESOURCE),
    POUDRE("&type_id[]=48", new String[]{"Poudre", "Hormone", "Sel", "Cendre", "Sable"}, RESOURCE),
    PREPARATION("&type_id[]=179", new String[]{"Potion", "Liquide", "Poison"}, RESOURCE),
    QUEUE("&type_id[]=65", new String[]{"Queue"}, RESOURCE),
    RACINE("&type_id[]=98", new String[]{"Racine"}, RESOURCE),
    RESSOURCES_DIVERSES("&type_id[]=15", new String[]{"Cervelle", "Scalp", "Coeur", "Cœur", "Sang", "Antenne", "Arakne",
    "Ballon", "Bandelette", "Bâton", "Bave", "Bon ", "Boomerang", "Boue", "Boule", "Boulon", "Bout", "Bouteille", "Carte",
    "Cendre", "Chaîne", "Coffre", "Collier", "Corde", "Croupion", "Eau", "Éclat" ,"Encre", "Estomac", "Faux", "Fil",
    "Flaque", "Fragment", "Flèche", "Fumée", "Glande", "Griffe", "Groin", "Insigne", "Jeton", "Lait", "Langue", "Pic",
    "Pixel", "Poche", "Poupée", "Relique", "Sac", "Sceau", "Sève", "Symbole", "Tentacule", "Testicule", "Ticket", "Tranche",
    "Ver", "Viscère"}, RESOURCE),
    RUNE_FORGEMAGIE("&type_id[]=78", new String[]{"Rune", "Pa ", "Ra "}, RESOURCE),
    SOUVENIR("&type_id[]=125", new String[]{"Souvenir"}, RESOURCE),
    SUBSTRAT("&type_id[]=183", new String[]{"Substrat"}, RESOURCE),
    SEVE("&type_id[]=185", new String[]{"Sève"}, RESOURCE),
    TEINTURE("&type_id[]=70", new String[]{"Teinture"}, RESOURCE),
    VIANDE("&type_id[]=63", new String[]{"Viande", "Bidoche"}, RESOURCE),
    VETEMENT("&type_id[]=164", new String[]{"Braguette", "Fermeture", "Épaulette", "Gilet", "Ceinture", "Culotte", "Broderie",
    "Culotte", "Chaussette", "Masque", "Caleçon", "Bandeau", "Boucle", "Casque", "Pagne", "String", "Foulard", "Slip", "Collier",
    "Botte", "Brassard", "Maillot", "Cagoule"}, RESOURCE),
    ECORCE("&type_id[]=96", new String[]{"Écorce"}, RESOURCE),

    BIERE("&type_id[]=37", new String[]{"Bière"}, CONSUMABLE),
    BOISSON("&type_id[]=79", new String[]{"Bouteille", "Limonade", "Coquetel"}, CONSUMABLE),
    BOITE_FRAGMENTS("&type_id[]=176", new String[]{"Boîte de fragments"}, CONSUMABLE),
    COFFRE("&type_id[]=172", new String[]{"Coffre"}, CONSUMABLE),
    CONTENEUR("&type_id[]=184", new String[]{"Tonneau", "Sachet"}, CONSUMABLE),
    FRIANDISE("&type_id[]=42", new String[]{"Bûche", "Bonbon", "Sukette", "Sucette", "Shigekax", "Barre Rabmarac", "Bouf'gomme"}, CONSUMABLE),
    FEE_ARTIFICE ("&type_id[]=74", new String[]{"Fée d'Artifice", "Grande Fée d'Artifice", "Petite Fée d'Artifice"}, CONSUMABLE),
    MIMIBIOTE ("&type_id[]=166", new String[]{"Mimibiote"}, CONSUMABLE),
    OBJET_ELEVAGE ("&type_id[]=93", new String[]{"Abreuvoir", "Dragofesse", "Foudroyeur", "Mangeoire", "Caresseur", "Baffeur"}, CONSUMABLE),
    OBJET_UTILISATBLE ("&type_id[]=94", new String[]{"Cawotte", "Voile Eidolonique", "Ballon", "Paire de skis", "Parchemin"}, CONSUMABLE),
    OEUF_FAMILIER ("&type_id[]=72", new String[]{"Oeuf"}, CONSUMABLE),
    PAIN ("&type_id[]=33", new String[]{"Pain", "Brioche"}, CONSUMABLE),
    PARCHEMIN_ATTITUDE ("&type_id[]=173", new String[]{"Coup fatal"}, CONSUMABLE),
    PARCHEMIN_XP ("&type_id[]=13", new String[]{"Parchemin"}, CONSUMABLE),
    PARCHEMIN_EMOTE ("&type_id[]=188", new String[]{"Émoticônes"}, CONSUMABLE),
    PARCHEMIN_CARACTERISTIQUE ("&type_id[]=76", new String[]{"Parchemin"}, CONSUMABLE),
    PARCHEMIN_RECHERCHE("&type_id[]=87", new String[]{"Parchemin"}, CONSUMABLE),
    PARCHEMIN_SORTILEGE ("&type_id[]=75", new String[]{"Parchemin de sort"}, CONSUMABLE),
    PARCHEMIN_TITRE ("&type_id[]=200", new String[]{"Diplôme"}, CONSUMABLE),
    PIERRE_MAGIQUE("&type_id[]=88", new String[]{"Fragment"}, CONSUMABLE),
    POISSON_CONSOMMABLE("&type_id[]=49", new String[]{"Sardine", "Poisson", "Goujon", "Beignet", "Truite", "Bâton", "Carpe", "Kralamoure",
            "Anguille", "Dorade", "Perche", "Raie", "Salace", "Aileron", "Bar", "Tanche", "Espadon", "Poisskaille"},
            CONSUMABLE),
    POTION("&type_id[]=12", new String[]{"Potion", "Fiole", "Oeuf"}, CONSUMABLE),
    POTION_OUBLI_PERCEPTEUR ("&type_id[]=86", new String[]{"Potion d'oubli Percepteur"}, CONSUMABLE),
    POTION_CONQUETE ("&type_id[]=165", new String[]{"Potion"}, CONSUMABLE),
    POTION_FAMILIER ("&type_id[]=116", new String[]{"Potion d'Amélioration", "Eupéoh"}, CONSUMABLE),
    POTION_MONTILIER ("&type_id[]=122", new String[]{"Potion d'Amélioration"}, CONSUMABLE),
    POTION_TELEPORTATION("&type_id[]=43", new String[]{"Potion", "Elixir"}, CONSUMABLE),
    VIANDE_CONSOMMABLE ("&type_id[]=69", new String[]{"Saucisse", "Andouillette", "Boudin", "Salade", "Quenelle", "Filet", "Mijoté",
            "Daube", "Marinade", "Grillade", "Pemmican", "Poêlée", "Pot-au-feu", "Terrine", "Parmentier", "Friture",
            "Papillotte", "Roulade", "Beignet", "Oeuf"}, CONSUMABLE);


    private String typeID;
    private String[] names;
    private SuperTypeResource superType;

    TypeResource(String typeID, String[] names, SuperTypeResource superType){
        this.typeID = typeID;
        this.names = names;
        this.superType = superType;
    }

    public String[] getNames(){ return names;}

    public SuperTypeResource getType(){
        return superType;
    }

    public String getTypeID(){
        return typeID;
    }
}

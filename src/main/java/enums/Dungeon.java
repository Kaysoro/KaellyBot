package enums;

import util.Translator;

public enum Dungeon {

    GLOURSELESTE("dungeon.glourseleste", 170, false),
    BLOP_MULTICOLORE_ROYAL("dungeon.blop_multicolore_royal", 120, false),
    DRAGON_COCHON("dungeon.dragon_cochon", 100, false),
    KORRIANDRE("dungeon.korriandre", 170, false),
    KRALAMOURE_GEANT("dungeon.kralamoure_geant", 180, true),
    MAITRE_CORBAC("dungeon.maitre_corbac", 100, false),
    ARCHE_OTOMAI("dungeon.arche_otomai", 90, false),
    KIMBO("dungeon.kimbo", 160, false),
    BULBES("dungeon.bulbes", 40, false),
    KOULOSSE("dungeon.koulosse", 100, false),
    KOLOSSO("dungeon.kolosso", 170, false),
    GIVREFOUX("dungeon.givrefoux", 170, false),
    MINOTOROR("dungeon.minotoror", 120, false),
    KARDORIM("dungeon.kardorim", 10, false),
    LARVES("dungeon.larves", 50, false),
    ANCESTRAL("dungeon.ancestral", 90, false),
    BLOPS("dungeon.blops", 60, false),
    BOUFTOUS("dungeon.bouftous", 30, false),
    BWORKS("dungeon.bworks", 50, false),
    CANIDES("dungeon.canides", 100, false),
    CHAMPS("dungeon.champs", 20, false),
    CRAQUELEURS("dungeon.craqueleurs", 70, false),
    FIREFOUX("dungeon.firefoux", 180, false),
    FORGERONS("dungeon.forgerons", 40, false),
    KITSOUNES("dungeon.kitsounes", 130, false),
    RATS_BONTA("dungeon.rats_bonta", 110, false),
    RATS_BRAKMAR("dungeon.rats_brakmar", 110, false),
    RATS_CHATEAU_AMAKNA("dungeon.rats_chateau_amakna", 150, false),
    SCARAFEUILLES("dungeon.scarafeuilles", 40, false),
    SQUELETTES("dungeon.squelettes", 40, false),
    TOFUS("dungeon.tofus", 40, false),
    DRAGOEUFS("dungeon.dragoeufs", 120, false),
    BWORKER("dungeon.bworker", 180, false),
    CHENE_MOU("dungeon.chene_mou", 160, false),
    ENSABLE("dungeon.ensable", 20, false),
    FUNGUS("dungeon.fungus", 190, false),
    KANNIBOUL("dungeon.kanniboul", 60, false),
    CHATEAU_WA_WABBIT("dungeon.chateau_wa_wabbit", 60, false),
    EPAVE_GROLANDAIS_VIOLENT("dungeon.epave_grolandais_violent", 160, false),
    MANSOT_ROYAL("dungeon.mansot_royal", 150, false),
    GELAXIEME_DIMENSION("dungeon.gelaxieme_dimension", 60, false),
    GOULET_RASBOUL("dungeon.goulet_rasboul", 110, false),
    GROTTE_HESQUE("dungeon.grotte_hesque", 50, false),
    OBSIDIANTRE("dungeon.obsidiantre", 160, false),
    BRUMEN_TINCTORIAS("dungeon.brumen_tinctorias", 70, false),
    TYNRIL("dungeon.tynril", 140, false),
    MAISON_FANTOME("dungeon.maison_fantome", 40, false),
    KWAKWA("dungeon.kwakwa", 50, false),
    DAIGORO("dungeon.daigoro", 80, false),
    SKEUNK("dungeon.skeunk", 120, false),
    PANDIKAZES("dungeon.pandikazes", 120, false),
    MINOTOT("dungeon.minotot", 160, false),
    ROYALMOUTH("dungeon.royalmouth", 120, false),
    TOFULAILLER_ROYAL("dungeon.tofulailler_royal", 120, false),
    CAVERNES_NOURRICIERES("dungeon.cavernes_nourricieres", 190, false),
    MOON("dungeon.moon", 100, false),
    SAKAI("dungeon.sakai", 190, false),
    MISSIZ_FRIZZ("dungeon.missiz_frizz", 190, false),
    SYLARGH("dungeon.sylargh", 190, false),
    KLIME("dungeon.klime", 190, false),
    NILEZA("dungeon.nileza", 190, false),
    COMTE("dungeon.comte", 190, false),
    HAREBOURG_NILEZA("dungeon.harebourg_nileza", 190, false),
    HAREBOURG_FRIZZ("dungeon.harebourg_frizz", 190, false),
    HAREBOURG_SYLARGH("dungeon.harebourg_sylargh", 190, false),
    HAREBOURG_KLIME("dungeon.harebourg_klime", 190, false),
    TERRIER_WA_WABBIT("dungeon.terrier_wa_wabbit", 80, false),
    THEATRE_DRAMAK("dungeon.theatre_dramak", 100, false),
    AQUADOME_MERKATOR("dungeon.aquadome_merkator", 190, false),
    OMBRE("dungeon.ombre", 190, false),
    KANIGROULA("dungeon.kanigroula", 170, false),
    HAUTE_TRUCHE("dungeon.haute_truche", 130, false),
    ROI_NIDAS("dungeon.roi_nidas", 190, false),
    PHOSSILE("dungeon.phossile", 150, false),
    MALLEFISK("dungeon.mallefisk", 100, false),
    TRONE_COUR_SOMBRE("dungeon.trone_cour_sombre", 190, false),
    TOXOLIATH("dungeon.toxoliath", 190, false),
    CAPITAINE_EKARLATTE("dungeon.capitaine_ekarlatte", 130, false),
    VENTRE_BALEINE("dungeon.ventre_baleine", 190, false),
    VORTEX("dungeon.vortex", 190, false),
    XLII("dungeon.xlii", 170, false),
    FRAKTALE("dungeon.fraktale", 120, false),
    KANKREBLATH("dungeon.kankreblath", 40, false),
    REINE_NYEE("dungeon.reine_nyee", 90, false),
    CHOUQUE("dungeon.chouque", 90, false),
    POUNICHEUR("dungeon.pounicheur", 110, false),
    USH("dungeon.ush", 160, false),
    HALOUINE("dungeon.halouine", 100, true),
    NOWEL("dungeon.nowel", 60, true),
    CAVERNE_NOWEL("dungeon.caverne_nowel", 120, true),
    PAPA_NOWEL("dungeon.papa_nowel", 180, true),
    GROZILLA_GRASMERA_SOMNAMBULES("dungeon.grozilla_grasmera_somnambules", 40, true),
    GROZILLA_GRASMERA_EPUISES("dungeon.grozilla_grasmera_epuises", 90, true),
    GROZILLA_GRASMERA_FATIGUES("dungeon.grozilla_grasmera_fatigues", 140, true),
    GROZILLA_GRASMERA("dungeon.grozilla_grasmera", 190, true),
    CHALOEIL("dungeon.chaloeil", 190, false),
    CAPITAINE_MENO("dungeon.capitaine_meno", 190, false),
    KOUTOULOU("dungeon.koutoulou", 190, false),
    DANTINEA("dungeon.dantinea", 190, false),
    MAGIK_RIKTUS("dungeon.magik_riktus", 90, false),
    KATREPAT("dungeon.katrepat", 190, false),
    ILYZAELLE("dungeon.ilyzaelle", 190, false),
    COMTE_RAZOF("dungeon.comte_razof", 190, false),
    MASTODONTES("dungeon.mastodontes", 80, false),
    EL_PIKO("dungeon.el_piko", 130, false),
    PERE_VER("dungeon.pere_ver", 170, false),
    TAL_KASHA("dungeon.tal_kasha", 190, false),
    SOLAR("dungeon.solar", 190, false),
    BETHEL("dungeon.bethel", 190, false),
    ROI_DAZAK("dungeon.roi_dazak", 190, false);

    private String name;
    private int level;
    private boolean isEvent;

    Dungeon(String name, int level, boolean isEvent) {
        this.name = name;
        this.level = level;
        this.isEvent = isEvent;
    }

    public String getName(){
        return name;
    }

    public int getLevel() {
        return level;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public String getLabel(Language lg){
        return Translator.getLabel(lg, getName());
    }

    public boolean filterByLevel(int level, int tolerance){
        return Math.abs(getLevel() - level) <= tolerance;
    }
}

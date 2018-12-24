package enums;

import util.Translator;

public enum Dungeon {

    CHAMPS("dungeon.champs", 20, false),
    ENSABLE("dungeon.ensable", 10, false),
    BOUFTOUS("dungeon.bouftous", 30, false),
    INCARNAM("dungeon.incarnam", 40, false),
    SCARAFEUILLES("dungeon.scarafeuilles", 40, false),
    TOFUS("dungeon.tofus", 40, false),
    MAISON_FANTOME("dungeon.maison_fantome", 40, false),
    SQUELETTES("dungeon.squelettes", 40, false),
    BWORKS("dungeon.bworks", 50, false),
    FORGERONS("dungeon.forgerons", 50, false),
    LARVES("dungeon.larves", 50, false),
    GROTTE_HESQUE("dungeon.grotte_hesque", 50, false),
    KWAKWA("dungeon.kwakwa", 50, false),
    BULBES("dungeon.bulbes", 50, false),
    CHATEAU_WA_WABBIT("dungeon.chateau_wa_wabbit", 60, false),
    KANNIBOUL("dungeon.kanniboul", 60, false),
    BLOPS("dungeon.blops", 60, false),
    GELAXIEME_DIMENSION("dungeon.gelaxieme_dimension", 60, false),
    BRUMEN_TINCTORIAS("dungeon.brumen_tinctorias", 70, false),
    ARCHE_OTOMAI("dungeon.arche_otomai", 70, false),
    CRAQUELEURS("dungeon.craqueleurs", 70, false),
    DAIGORO("dungeon.daigoro", 80, false),
    TERRIER_WA_WABBIT("dungeon.terrier_wa_wabbit", 80, false),
    ANCESTRAL("dungeon.ancestral", 90, false),
    COLONIMB("dungeon.colonimb", 90, false),
    DRAGON_COCHON("dungeon.dragon_cochon", 100, false),
    KOULOSSE("dungeon.koulosse", 100, false),
    MOON("dungeon.moon", 100, false),
    CANIDES("dungeon.canides", 100, false),
    GOULET_RASBOUL("dungeon.goulet_rasboul", 110, false),
    MAITRE_CORBAC("dungeon.maitre_corbac", 110, false),
    RATS_BONTA("dungeon.rats_bonta", 110, false),
    RATS_BRAKMAR("dungeon.rats_brakmar", 110, false),
    BLOP_MULTICOLORE_ROYAL("dungeon.blop_multicolore_royal", 120, false),
    MINOTOROR("dungeon.minotoror", 120, false),
    ROYALMOUTH("dungeon.royalmouth", 120, false),
    PANDIKAZES("dungeon.pandikazes", 120, false),
    TOFULAILLER_ROYAL("dungeon.tofulailler_royal", 120, false),
    DRAGOEUFS("dungeon.dragoeufs", 120, false),
    SKEUNK("dungeon.skeunk", 120, false),
    KITSOUNES("dungeon.kitsounes", 130, false),
    FIREFOUX("dungeon.firefoux", 140, false),
    CHENE_MOU("dungeon.chene_mou", 140, false),
    TYNRIL("dungeon.tynril", 140, false),
    MANSOT_ROYAL("dungeon.mansot_royal", 140, false),
    EPAVE_GROLANDAIS_VIOLENT("dungeon.epave_grolandais_violent", 150, false),
    RATS_CHATEAU_AMAKNA("dungeon.rats_chateau_amakna", 150, false),
    KIMBO("dungeon.kimbo", 160, false),
    MINOTOT("dungeon.minotot", 160, false),
    OBSIDIANTRE("dungeon.obsidiantre", 160, false),
    GIVREFOUX("dungeon.givrefoux", 170, false),
    KORRIANDRE("dungeon.korriandre", 180, false),
    KRALAMOURE_GEANT("dungeon.kralamoure_geant", 180, true),
    BWORKER("dungeon.bworker", 180, false),
    FUNGUS("dungeon.fungus", 180, false),
    KOLOSSO("dungeon.kolosso", 190, false),
    CAVERNES_NOURRICIERES("dungeon.cavernes_nourricieres", 190, false),
    SAKAI("dungeon.sakai", 190, false),
    GLOURSELESTE("dungeon.glourseleste", 190, false),
    MISSIZ_FRIZZ("dungeon.missiz_frizz", 190, false),
    SYLARGH("dungeon.sylargh", 190, false),
    KLIME("dungeon.klime", 190, false),
    NILEZA("dungeon.nileza", 190, false),
    COMTE("dungeon.comte", 190, false),
    HAREBOURG_FRIZZ("dungeon.harebourg_frizz", 190, false),
    HAREBOURG_SYLARGH("dungeon.harebourg_sylargh", 190, false),
    HAREBOURG_KLIME("dungeon.harebourg_klime", 190, false),
    HAREBOURG_NILEZA("dungeon.harebourg_nileza", 190, false),
    NOWEL("dungeon.nowel", 50, true),
    CAVERNE_NOWEL("dungeon.caverne_nowel", 110, true),
    PAPA_NOWEL("dungeon.papa_nowel", 180, true),
    GROZILLA_GRASMERA_SOMNAMBULES("dungeon.grozilla_grasmera_somnambules", 40, true),
    GROZILLA_GRASMERA_EPUISES("dungeon.grozilla_grasmera_epuises", 90, true),
    GROZILLA_GRASMERA_FATIGUES("dungeon.grozilla_grasmera_fatigues", 140, true),
    GROZILLA_GRASMERA("dungeon.grozilla_grasmera", 190, true),
    HALOUINE("dungeon.halouine", 90, true),
    ROC_OSTIQUE("dungeon.roc_ostique", 60, true),
    GOULDEN_PALACE("dungeon.goulden_palace", 200, true);

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

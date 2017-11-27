package enums;

import util.Translator;

public enum Statistique {
    DEGAT_NEUTRE("stat.degat_neutre", "<:neutre:368655765864710144>"),
    DEGAT_TERRE("stat.degat_terre", "<:force:368655415376084993>"),
    DEGAT_FEU("stat.degat_feu", "<:intelligence:368655513920995338>"),
    DEGAT_EAU("stat.degat_eau", "<:chance:368655576051482632>"),
    DEGAT_AIR("stat.degat_air", "<:agilite:368655691914674176>"),
    VDV_NEUTRE("stat.vdv_neutre", "<:neutre:368655765864710144>"),
    VDV_TERRE("stat.vdv_terre", "<:force:368655415376084993>"),
    VDV_FEU("stat.vdv_feu", "<:intelligence:368655513920995338>"),
    VDV_EAU("stat.vdv_eau", "<:chance:368655576051482632>"),
    VDV_AIR("stat.vdv_air", "<:agilite:368655691914674176>"),
    SOIN_RENDU("stat.soin_rendu", "<:soin:368657664613416960>"),
    VITALITE("stat.vitalite", "<:vitalite:368655306420518914>"),
    SAGESSE("stat.sagesse", "<:sagesse:368655367900758017>"),
    FORCE("stat.force", "<:force:368655415376084993>"),
    INTELLIGENCE("stat.intelligence", "<:intelligence:368655513920995338>"),
    CHANCE("stat.chance", "<:chance:368655576051482632>"),
    AGILITE("stat.agilite", "<:agilite:368655691914674176>"),
    INITIATIVE("stat.initiative", "<:initiative:368655803277639682>"),
    PROSPECTION("stat.prospection", "<:prospection:368658734785757195>"),
    POD("stat.pod", "<:pods:369041617505484812>"),
    PORTEE("stat.portee", "<:po:368656759684071425>"),
    PA("stat.pa", "<:pa:368656549998231553>"),
    PM ("stat.pm", "<:pm:368656594927484928>"),
    INVOCATION("stat.invocation", "<:invocation:368655852346802176>"),
    TACLE("stat.tacle", "<:tacle:368656968799354900>"),
    FUITE("stat.fuite", "<:fuite:368657018795327488>"),
    PUISSANCE("stat.puissance", "<:puissance:368657279949733888>"),
    PUISSANCE_PIEGE("stat.puissance_piege", "<:puipi:368657984265519105>"),
    DOMMAGE("stat.dommage", "<:do:368657984164986880>"),
    DOMMAGE_NEUTRE("stat.dommage_neutre", "<:neutre:368655765864710144>"),
    DOMMAGE_TERRE("stat.dommage_terre", "<:force:368655415376084993>"),
    DOMMAGE_FEU("stat.dommage_feu", "<:intelligence:368655513920995338>"),
    DOMMAGE_EAU("stat.dommage_eau", "<:chance:368655576051482632>"),
    DOMMAGE_AIR("stat.dommage_air", "<:agilite:368655691914674176>"),
    DOMMAGE_PIEGE("stat.dommage_piege", "<:dopi:368657984181501963>"),
    DOMMAGE_POUSSEE("stat.dommage_poussee", "<:dopou:368657983690768385>"),
    DOMMAGE_CRITIQUE("stat.dommage_critique", "<:docrit:368657983732711428>"),
    ESQUIVE_PA("stat.esquive_pa", "<:esquivepa:368656921848446976>"),
    ESQUIVE_PM("stat.esquive_pm", "<:esquivepm:368656934053871616>"),
    RETRAIT_PA("stat.retrait_pa", "<:retraitpa:368657522346950658>"),
    RETRAIT_PM("stat.retrait_pm", "<:retraitpm:368657522401214466>"),
    COUP_CRITIQUE("stat.coup_critique", "<:cc:368657664273547266>"),
    SOIN("stat.soin", "<:soin:368657664613416960>"),
    RESISTANCE_FIXE_NEUTRE("stat.resistance_fixe_neutre", "<:neutre:368655765864710144>"),
    RESISTANCE_FIXE_TERRE("stat.resistance_fixe_terre", "<:force:368655415376084993>"),
    RESISTANCE_FIXE_FEU("stat.resistance_fixe_feu", "<:intelligence:368655513920995338>"),
    RESISTANCE_FIXE_EAU("stat.resistance_fixe_eau", "<:chance:368655576051482632>"),
    RESISTANCE_FIXE_AIR("stat.resistance_fixe_air", "<:agilite:368655691914674176>"),
    RESISTANCE_FIXE_POUSSEE("stat.resistance_fixe_poussee", "<:repou:368658734706196491>"),
    RESISTANCE_FIXE_CC("stat.resistance_fixe_cc", "<:recc:368657664789708800>"),
    RESISTANCE_PERCENT_NEUTRE("stat.resistance_percent_neutre", "<:neutre:368655765864710144>"),
    RESISTANCE_PERCENT_TERRE("stat.resistance_percent_terre", "<:force:368655415376084993>"),
    RESISTANCE_PERCENT_FEU("stat.resistance_percent_feu", "<:intelligence:368655513920995338>"),
    RESISTANCE_PERCENT_EAU("stat.resistance_percent_eau", "<:chance:368655576051482632>"),
    RESISTANCE_PERCENT_AIR("stat.resistance_percent_air", "<:agilite:368655691914674176>"),
    RENVOI("stat.renvoi", "<:renvoi:368657983468470274>");

    private String names;
    private String emoji;

    Statistique(String names, String emoji){
        this.names = names;
        this.emoji = emoji;
    }

    public String[] getNames(Language lg){ return Translator.getLabel(lg, names).split(";");}

    public String getEmoji(){ return emoji;}
}

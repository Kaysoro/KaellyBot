package enums;

import util.Translator;

public enum Statistique {
    DEGAT_NEUTRE(Translator.getStatProposalFor("stat.degat_neutre"), "<:neutre:368655765864710144>"),
    DEGAT_TERRE(Translator.getStatProposalFor("stat.degat_terre"), "<:force:368655415376084993>"),
    DEGAT_FEU(Translator.getStatProposalFor("stat.degat_feu"), "<:intelligence:368655513920995338>"),
    DEGAT_EAU(Translator.getStatProposalFor("stat.degat_eau"), "<:chance:368655576051482632>"),
    DEGAT_AIR(Translator.getStatProposalFor("stat.degat_air"), "<:agilite:368655691914674176>"),
    VDV_NEUTRE(Translator.getStatProposalFor("stat.vdv_neutre"), "<:neutre:368655765864710144>"),
    VDV_TERRE(Translator.getStatProposalFor("stat.vdv_terre"), "<:force:368655415376084993>"),
    VDV_FEU(Translator.getStatProposalFor("stat.vdv_feu"), "<:intelligence:368655513920995338>"),
    VDV_EAU(Translator.getStatProposalFor("stat.vdv_eau"), "<:chance:368655576051482632>"),
    VDV_AIR(Translator.getStatProposalFor("stat.vdv_air"), "<:agilite:368655691914674176>"),
    SOIN_RENDU(Translator.getStatProposalFor("stat.soin_rendu"), "<:soin:368657664613416960>"),
    VITALITE(Translator.getStatProposalFor("stat.vitalite"), "<:vitalite:368655306420518914>"),
    SAGESSE(Translator.getStatProposalFor("stat.sagesse"), "<:sagesse:368655367900758017>"),
    FORCE(Translator.getStatProposalFor("stat.force"), "<:force:368655415376084993>"),
    INTELLIGENCE(Translator.getStatProposalFor("stat.intelligence"), "<:intelligence:368655513920995338>"),
    CHANCE(Translator.getStatProposalFor("stat.chance"), "<:chance:368655576051482632>"),
    AGILITE(Translator.getStatProposalFor("stat.agilite"), "<:agilite:368655691914674176>"),
    INITIATIVE(Translator.getStatProposalFor("stat.initiative"), "<:initiative:368655803277639682>"),
    PROSPECTION(Translator.getStatProposalFor("stat.prospection"), "<:prospection:368658734785757195>"),
    POD(Translator.getStatProposalFor("stat.pod"), "<:pods:369041617505484812>"),
    PORTEE(Translator.getStatProposalFor("stat.portee"), "<:po:368656759684071425>"),
    PA(Translator.getStatProposalFor("stat.pa"), "<:pa:368656549998231553>"),
    PM (Translator.getStatProposalFor("stat.pm"), "<:pm:368656594927484928>"),
    INVOCATION(Translator.getStatProposalFor("stat.invocation"), "<:invocation:368655852346802176>"),
    TACLE(Translator.getStatProposalFor("stat.tacle"), "<:tacle:368656968799354900>"),
    FUITE(Translator.getStatProposalFor("stat.fuite"), "<:fuite:368657018795327488>"),
    PUISSANCE(Translator.getStatProposalFor("stat.puissance"), "<:puissance:368657279949733888>"),
    PUISSANCE_PIEGE(Translator.getStatProposalFor("stat.puissance_piege"), "<:puipi:368657984265519105>"),
    DOMMAGE(Translator.getStatProposalFor("stat.dommage"), "<:do:368657984164986880>"),
    DOMMAGE_NEUTRE(Translator.getStatProposalFor("stat.dommage_neutre"), "<:neutre:368655765864710144>"),
    DOMMAGE_TERRE(Translator.getStatProposalFor("stat.dommage_terre"), "<:force:368655415376084993>"),
    DOMMAGE_FEU(Translator.getStatProposalFor("stat.dommage_feu"), "<:intelligence:368655513920995338>"),
    DOMMAGE_EAU(Translator.getStatProposalFor("stat.dommage_eau"), "<:chance:368655576051482632>"),
    DOMMAGE_AIR(Translator.getStatProposalFor("stat.dommage_air"), "<:agilite:368655691914674176>"),
    DOMMAGE_PIEGE(Translator.getStatProposalFor("stat.dommage_piege"), "<:dopi:368657984181501963>"),
    DOMMAGE_POUSSEE(Translator.getStatProposalFor("stat.dommage_poussee"), "<:dopou:368657983690768385>"),
    DOMMAGE_CRITIQUE(Translator.getStatProposalFor("stat.dommage_critique"), "<:docrit:368657983732711428>"),
    ESQUIVE_PA(Translator.getStatProposalFor("stat.esquive_pa"), "<:esquivepa:368656921848446976>"),
    ESQUIVE_PM(Translator.getStatProposalFor("stat.esquive_pm"), "<:esquivepm:368656934053871616>"),
    RETRAIT_PA(Translator.getStatProposalFor("stat.retrait_pa"), "<:retraitpa:368657522346950658>"),
    RETRAIT_PM(Translator.getStatProposalFor("stat.retrait_pm"), "<:retraitpm:368657522401214466>"),
    COUP_CRITIQUE(Translator.getStatProposalFor("stat.coup_critique"), "<:cc:368657664273547266>"),
    SOIN(Translator.getStatProposalFor("stat.soin"), "<:soin:368657664613416960>"),
    RESISTANCE_FIXE_NEUTRE(Translator.getStatProposalFor("stat.resistance_fixe_neutre"), "<:neutre:368655765864710144>"),
    RESISTANCE_FIXE_TERRE(Translator.getStatProposalFor("stat.resistance_fixe_terre"), "<:force:368655415376084993>"),
    RESISTANCE_FIXE_FEU(Translator.getStatProposalFor("stat.resistance_fixe_feu"), "<:intelligence:368655513920995338>"),
    RESISTANCE_FIXE_EAU(Translator.getStatProposalFor("stat.resistance_fixe_eau"), "<:chance:368655576051482632>"),
    RESISTANCE_FIXE_AIR(Translator.getStatProposalFor("stat.resistance_fixe_air"), "<:agilite:368655691914674176>"),
    RESISTANCE_FIXE_POUSSEE(Translator.getStatProposalFor("stat.resistance_fixe_poussee"), "<:repou:368658734706196491>"),
    RESISTANCE_FIXE_CC(Translator.getStatProposalFor("stat.resistance_fixe_cc"), "<:recc:368657664789708800>"),
    RESISTANCE_PERCENT_NEUTRE(Translator.getStatProposalFor("stat.resistance_percent_neutre"), "<:neutre:368655765864710144>"),
    RESISTANCE_PERCENT_TERRE(Translator.getStatProposalFor("stat.resistance_percent_terre"), "<:force:368655415376084993>"),
    RESISTANCE_PERCENT_FEU(Translator.getStatProposalFor("stat.resistance_percent_feu"), "<:intelligence:368655513920995338>"),
    RESISTANCE_PERCENT_EAU(Translator.getStatProposalFor("stat.resistance_percent_eau"), "<:chance:368655576051482632>"),
    RESISTANCE_PERCENT_AIR(Translator.getStatProposalFor("stat.resistance_percent_air"), "<:agilite:368655691914674176>"),
    RENVOI(Translator.getStatProposalFor("stat.renvoi"), "<:renvoi:368657983468470274>");

    private String[] names;
    private String emoji;

    Statistique(String[] names, String emoji){
        this.names = names;
        this.emoji = emoji;
    }

    public String[] getNames(){ return names;}

    public String getEmoji(){ return emoji;}
}

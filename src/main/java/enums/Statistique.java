package enums;

public enum Statistique {
    DEGAT_NEUTRE(new String[]{"(dommages Neutre)"}, "<:neutre:368655765864710144>"),
    DEGAT_TERRE(new String[]{"(dommages Terre)"}, "<:force:368655415376084993>"),
    DEGAT_FEU(new String[]{"(dommages Feu)"}, "<:intelligence:368655513920995338>"),
    DEGAT_EAU(new String[]{"(dommages Eau)"}, "<:chance:368655576051482632>"),
    DEGAT_AIR(new String[]{"(dommages Air)"}, "<:agilite:368655691914674176>"),
    VDV_NEUTRE(new String[]{"(vol Neutre)"}, "<:neutre:368655765864710144>"),
    VDV_TERRE(new String[]{"(vol Terre)"}, "<:force:368655415376084993>"),
    VDV_FEU(new String[]{"(vol Feu)"}, "<:intelligence:368655513920995338>"),
    VDV_EAU(new String[]{"(vol Eau)"}, "<:chance:368655576051482632>"),
    VDV_AIR(new String[]{"(vol Air)"}, "<:agilite:368655691914674176>"),
    SOIN_RENDU(new String[]{"(PV rendus)"}, "<:soin:368657664613416960>"),
    VITALITE(new String[]{"PV", "Vitalité"}, "<:vitalite:368655306420518914>"),
    SAGESSE(new String[]{"Sagesse"}, "<:sagesse:368655367900758017>"),
    FORCE(new String[]{"Force"}, "<:force:368655415376084993>"),
    INTELLIGENCE(new String[]{"Intelligence"}, "<:intelligence:368655513920995338>"),
    CHANCE(new String[]{"Chance"}, "<:chance:368655576051482632>"),
    AGILITE(new String[]{"Agilité"}, "<:agilite:368655691914674176>"),
    INITIATIVE(new String[]{"Initiative"}, "<:initiative:368655803277639682>"),
    PROSPECTION(new String[]{"Prospection"}, "<:prospection:368658734785757195>"),
    POD(new String[]{"Pods"}, "<:pods:369041617505484812>"),
    PORTEE(new String[]{"Portée"}, "<:po:368656759684071425>"),
    PA(new String[]{"PA"}, "<:pa:368656549998231553>"),
    PM (new String[]{"PM"}, "<:pm:368656594927484928>"),
    INVOCATION(new String[]{"Invocations"}, "<:invocation:368655852346802176>"),
    TACLE(new String[]{"Tacle"}, "<:tacle:368656968799354900>"),
    FUITE(new String[]{"Fuite"}, "<:fuite:368657018795327488>"),
    PUISSANCE(new String[]{"Puissance"}, "<:puissance:368657279949733888>"),
    PUISSANCE_PIEGE(new String[]{"Puissance (pièges)"}, "<:puipi:368657984265519105>"),
    DOMMAGE(new String[]{"Dommages"}, "<:do:368657984164986880>"),
    DOMMAGE_NEUTRE(new String[]{"Dommages Neutre"}, "<:neutre:368655765864710144>"),
    DOMMAGE_TERRE(new String[]{"Dommages Terre"}, "<:force:368655415376084993>"),
    DOMMAGE_FEU(new String[]{"Dommages Feu"}, "<:intelligence:368655513920995338>"),
    DOMMAGE_EAU(new String[]{"Dommages Eau"}, "<:chance:368655576051482632>"),
    DOMMAGE_AIR(new String[]{"Dommages Air"}, "<:agilite:368655691914674176>"),
    DOMMAGE_PIEGE(new String[]{"Dommages Pièges"}, "<:dopi:368657984181501963>"),
    DOMMAGE_POUSSEE(new String[]{"Dommages Poussée"}, "<:dopou:368657983690768385>"),
    DOMMAGE_CRITIQUE(new String[]{"Dommages Critiques"}, "<:docrit:368657983732711428>"),
    ESQUIVE_PA(new String[]{"Esquive PA"}, "<:esquivepa:368656921848446976>"),
    ESQUIVE_PM(new String[]{"Esquive PM"}, "<:esquivepm:368656934053871616>"),
    RETRAIT_PA(new String[]{"Retrait PA"}, "<:retraitpa:368657522346950658>"),
    RETRAIT_PM(new String[]{"Retrait PM"}, "<:retraitpm:368657522401214466>"),
    COUP_CRITIQUE(new String[]{"% Critique"}, "<:cc:368657664273547266>"),
    SOIN(new String[]{"Soins"}, "<:soin:368657664613416960>"),
    RESISTANCE_FIXE_NEUTRE(new String[]{"Résistance Neutre"}, "<:neutre:368655765864710144>"),
    RESISTANCE_FIXE_TERRE(new String[]{"Résistance Terre"}, "<:force:368655415376084993>"),
    RESISTANCE_FIXE_FEU(new String[]{"Résistance Feu"}, "<:intelligence:368655513920995338>"),
    RESISTANCE_FIXE_EAU(new String[]{"Résistance Eau"}, "<:chance:368655576051482632>"),
    RESISTANCE_FIXE_AIR(new String[]{"Résistance Air"}, "<:agilite:368655691914674176>"),
    RESISTANCE_FIXE_POUSSEE(new String[]{"Résistance Poussée"}, "<:repou:368658734706196491>"),
    RESISTANCE_FIXE_CC(new String[]{"Résistance Critiques"}, "<:recc:368657664789708800>"),
    RESISTANCE_PERCENT_NEUTRE(new String[]{"Neutre %", "% Résistance Neutre"}, "<:neutre:368655765864710144>"),
    RESISTANCE_PERCENT_TERRE(new String[]{"Terre %", "% Résistance Terre"}, "<:force:368655415376084993>"),
    RESISTANCE_PERCENT_FEU(new String[]{"Feu %", "% Résistance Feu"}, "<:intelligence:368655513920995338>"),
    RESISTANCE_PERCENT_EAU(new String[]{"Eau %", "% Résistance Eau"}, "<:chance:368655576051482632>"),
    RESISTANCE_PERCENT_AIR(new String[]{"Air %", "% Résistance Air"}, "<:agilite:368655691914674176>"),
    RENVOI(new String[]{"Renvoie dommages"}, "<:renvoi:368657983468470274>");

    private String[] names;
    private String emoji;

    Statistique(String[] names, String emoji){
        this.names = names;
        this.emoji = emoji;
    }

    public String[] getNames(){ return names;}

    public String getEmoji(){ return emoji;}
}

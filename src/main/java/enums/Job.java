package enums;

import util.Translator;

public enum Job {

    // ToDo : Update this list with Dofus Touch Job
    ALCHIMISTE("job.alchimiste"),
    BIJOUTIER("job.bijoutier"),
    BOUCHER("job.boucher"),
    BOULANGER("job.boulanger"),
    BRICOLEUR("job.bricoleur"),
    BUCHERON("job.bucheron"),
    CHASSEUR("job.chasseur"),
    CORDOMAGE("job.cordomage"),
    CORDONNIER("job.cordonnier"),
    COSTUMAGE("job.costumage"),
    FORGEMAGEEPEES("job.forgemageepees"),
    FORGEMAGEDAGUES("job.forgemagedagues"),
    FORGEMAGEHACHES("job.forgemagehaches"),
    FORGEMAGEMARTEAUX("job.forgemagemarteaux"),
    FORGEMAGEPELLES("job.forgemagepelles"),
    FORGEUREPEES("job.forgeurepees"),
    FORGEURBOUCLIERS("job.forgeurboucliers"),
    FORGEURDAGUES("job.forgeurdagues"),
    FORGEURHACHES("job.forgeurhaches"),
    FORGEURMARTEAUX("job.forgeurmarteaux"),
    FORGEURPELLES("job.forgeurpelles"),
    JOAILLOMAGE("job.joaillomage"),
    MINEUR("job.mineur"),
    PAYSAN("job.paysan"),
    PECHEUR("job.pecheur"),
    POISSONNIER("job.poissonnier"),
    SCULPTEMAGEARCS("job.sculptemagearcs"),
    SCULPTEMAGEBAGUETTES("job.sculptemagebaguettes"),
    SCULPTEMAGEBATONS("job.sculptemagebatons"),
    SCULPTEURARCS("job.sculpteurarcs"),
    SCULPTEURBAGUETTES("job.sculpteurbaguettes"),
    SCULPTEURBATONS("job.sculpteurbatons"),
    TAILLEUR("job.tailleur");

    private String name;

    Job(String name){this.name = name;}

    public String getName(){
        return name;
    }

    public String getLabel(Language lg){
        return Translator.getLabel(lg, getName());
    }
}

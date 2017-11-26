package enums;

import util.Translator;

public enum Job {

    ALCHIMISTE("job.alchimiste"),
    BIJOUTIER("job.bijoutier"),
    BRICOLEUR("job.bricoleur"),
    BUCHERON("job.bucheron"),
    CHASSEUR("job.chasseur"),
    CORDOMAGE("job.cordomage"),
    CORDONNIER("job.cordonnier"),
    COSTUMAGE("job.costumage"),
    FACONNEUR("job.faconneur"),
    FORGEMAGE("job.forgemage"),
    FORGERON("job.forgeron"),
    JOAILLOMAGE("job.joaillomage"),
    MINEUR("job.mineur"),
    PAYSAN("job.paysan"),
    PECHEUR("job.pecheur"),
    SCULPTEMAGE("job.sculptemage"),
    SCULPTEUR("job.sculpteur"),
    TAILLEUR("job.tailleur"),
    FACOMAGE("job.facomage");

    private String name;

    Job(String name){this.name = name;}

    public String getName(){
        return name;
    }

    public String getLabel(Language lg){
        return Translator.getLabel(lg, getName());
    }
}

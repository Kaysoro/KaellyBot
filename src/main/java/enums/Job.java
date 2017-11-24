package enums;

import util.Translator;

public enum Job {

    ALCHIMISTE("alchimiste"),
    BIJOUTIER("bijoutier"),
    BRICOLEUR("bricoleur"),
    BUCHERON("bucheron"),
    CHASSEUR("chasseur"),
    CORDOMAGE("cordomage"),
    CORDONNIER("cordonnier"),
    COSTUMAGE("costumage"),
    FACONNEUR("faconneur"),
    FORGEMAGE("forgemage"),
    FORGERON("forgeron"),
    JOAILLOMAGE("joaillomage"),
    MINEUR("mineur"),
    PAYSAN("paysan"),
    PECHEUR("pecheur"),
    SCULPTEMAGE("sculptemage"),
    SCULPTEUR("sculpteur"),
    TAILLEUR("tailleur"),
    FACOMAGE("facomage");

    private String name;

    Job(String name){this.name = name;}

    public String getName(){
        return name;
    }

    public String getLabel(Language lg){
        return Translator.getLabel(lg, getName());
    }
}

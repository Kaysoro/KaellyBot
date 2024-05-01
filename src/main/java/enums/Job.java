package enums;

import util.Translator;

import java.util.HashMap;
import java.util.Map;

public enum Job {

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
    FACONNEUR("job.faconneur"),
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

    private static Map<String, Job> jobs;
    private String name;

    Job(String name){this.name = name;}

    public String getName(){
        return name;
    }

    public String getLabel(Language lg){
        return Translator.getLabel(lg, getName());
    }

    /**
     *
     * @param name Nom du métier
     * @return L'énumération Job correspondant à name
     * @throws IllegalArgumentException si aucun Job n'est trouvé pour le nom proposé.
     */
    public static synchronized Job getJob(String name){
        if (jobs == null){
            jobs = new HashMap<>();
            for(Job job : Job.values())
                jobs.put(job.getName(), job);
        }

        if (jobs.containsKey(name))
            return jobs.get(name);
        throw new IllegalArgumentException("No job found for \"" + name + "\".");
    }
}

package enums;

import util.Translator;

import java.util.HashMap;
import java.util.Map;

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

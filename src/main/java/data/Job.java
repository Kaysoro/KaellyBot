package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve on 12/11/2016.
 */
public class Job {

    private final static Logger LOG = LoggerFactory.getLogger(Job.class);
    private static List<Job> jobs;
    private String name;

    private Job(String name){
        this.name = name;
    }

    public Map<User, Integer> getJobbers(){
        Map<User, Integer> jobbers = new HashMap<User, Integer>();

        //TODO SQL Part

        return jobbers;
    }

    public static List<Job> getJobs(){
        if (jobs == null){
            jobs = new ArrayList<Job>();
            jobs.add(new Job("Forgeron"));
            jobs.add(new Job("Tailleur"));
            jobs.add(new Job("Sculpteur"));
            //TODO gather data from databsse
        }
        return jobs;
    }
}

package triggers;

import triggers.model.Trigger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TriggerManager {

    private static TriggerManager instance;

    private List<Trigger> triggers;

    private TriggerManager(){
        super();
        triggers = new CopyOnWriteArrayList<>();
        //triggers.add(new DofusRoomTrigger());
    }

    public static TriggerManager getInstance(){
        if (instance == null)
            instance = new TriggerManager();
        return instance;
    }

    public static List<Trigger> getTriggers(){
        return getInstance().triggers;
    }
}

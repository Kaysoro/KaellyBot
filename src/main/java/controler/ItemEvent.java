package controler;

import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by steve on 14/07/2016.
 */
public class ItemEvent extends Event {
    private final IMessage message;

    public ItemEvent(IMessage message){
        super();
        this.message = message;
    }

    public IMessage getMessage(){
        return message;
    }
}

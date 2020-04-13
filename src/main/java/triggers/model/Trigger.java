package triggers.model;

import discord4j.core.object.entity.Message;
import enums.Language;

public interface Trigger {

    boolean isTriggered(Message message, Language lg);

    void execute(Message message, Language lg);
}

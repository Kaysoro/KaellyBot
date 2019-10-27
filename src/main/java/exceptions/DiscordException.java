package exceptions;

import commands.model.Command;
import discord4j.core.object.entity.Message;
import enums.Language;

/**
 * Created by steve on 14/11/2016.
 */
public interface DiscordException {

    void throwException(Message message, Command command, Language lg, Object... arguments);
}

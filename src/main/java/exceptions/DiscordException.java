package exceptions;

import commands.model.LegacyCommand;
import discord4j.core.object.entity.Message;
import enums.Language;

/**
 * Created by steve on 14/11/2016.
 */
public interface DiscordException {

    void throwException(Message message, LegacyCommand command, Language lg, Object... arguments);
}

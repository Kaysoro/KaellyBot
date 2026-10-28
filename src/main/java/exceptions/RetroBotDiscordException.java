package exceptions;

import commands.model.Command;
import data.Constants;
import discord4j.core.object.entity.Message;
import enums.Language;
import util.Translator;

/**
 * Sent instead of handling a DOFUS Retro server: KaellyRetro took them over, with their data already migrated.
 */
public class RetroBotDiscordException implements DiscordException {

    @Override
    public void throwException(Message message, Command command, Language lg, Object... arguments) {
        message.getChannel().flatMap(channel ->
                channel.createEmbed(spec ->
                        spec.setTitle(Translator.getLabel(lg, "exception.retro_bot"))
                                .setDescription(Translator.getLabel(lg, "exception.retro_bot.description")
                                        .replace("{url}", Constants.retroInvite))
                                .setThumbnail(Constants.retroAvatar)))
                .subscribe();
    }
}

package exceptions;

import commands.model.Command;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;
import util.Translator;

/**
 * Created by steve on 14/11/2016.
 */
public class BasicDiscordException implements DiscordException {

    private final static Logger LOG = LoggerFactory.getLogger(BasicDiscordException.class);
    public final static BasicDiscordException ALMANAX = new BasicDiscordException("exception.basic.almanax");
    public final static BasicDiscordException ALLIANCEPAGE_INACCESSIBLE = new BasicDiscordException("exception.basic.alliancepage_inaccessible");
    public final static BasicDiscordException GUILDPAGE_INACCESSIBLE = new BasicDiscordException("exception.basic.guildpage_inaccessible");
    public final static BasicDiscordException CHARACTERPAGE_INACCESSIBLE = new BasicDiscordException("exception.basic.characterpage_inaccessible");
    public final static BasicDiscordException CHARACTER_TOO_OLD = new BasicDiscordException("exception.basic.character_too_old");
    public final static BasicDiscordException COMMAND_FORBIDDEN = new BasicDiscordException("exception.basic.command_forbidden");
    public final static BasicDiscordException FORBIDDEN_COMMAND_FOUND = new BasicDiscordException("exception.basic.forbidden_command_found");
    public final static BasicDiscordException FORBIDDEN_COMMAND_NOTFOUND = new BasicDiscordException("exception.basic.forbidden_command_notfound");
    public final static BasicDiscordException INCORRECT_DATE_FORMAT = new BasicDiscordException("exception.basic.incorrect_date_format");
    public final static BasicDiscordException IN_DEVELOPPMENT = new BasicDiscordException("exception.basic.in_developpment");
    public final static BasicDiscordException NO_EXTERNAL_EMOJI_PERMISSION = new BasicDiscordException("exception.basic.no_external_emoji_permission");
    public final static BasicDiscordException NO_SEND_TEXT_PERMISSION = new BasicDiscordException("exception.basic.no_send_text_permission");
    public final static BasicDiscordException NO_NSFW_CHANNEL = new BasicDiscordException("exception.basic.no_nsfw_channel");
    public final static BasicDiscordException NO_ENOUGH_RIGHTS = new BasicDiscordException("exception.basic.no_enough_rights");
    public final static BasicDiscordException NO_VOICE_PERMISSION = new BasicDiscordException("exception.basic.no_voice_permission");
    public final static BasicDiscordException NOT_IN_VOCAL_CHANNEL = new BasicDiscordException("exception.basic.not_in_vocal_channel");
    public final static BasicDiscordException NOT_USABLE_IN_MP = new BasicDiscordException("exception.basic.not_usable_in_mp");
    public final static BasicDiscordException UNKNOWN_ERROR = new BasicDiscordException("exception.basic.unknown_error");
    public final static BasicDiscordException USER_NEEDED = new BasicDiscordException("exception.basic.user_needed");
    public final static BasicDiscordException VOICE_CHANNEL_LIMIT = new BasicDiscordException("exception.basic.voice_channel_limit");
    private String messageKey;

    private BasicDiscordException(String message){
        this.messageKey = message;
    }
    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        Message.sendText(message.getChannel(), Translator.getLabel(lg, messageKey));
    }
}

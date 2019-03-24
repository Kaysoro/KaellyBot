package exceptions;

import commands.classic.AllianceCommand;
import commands.classic.GuildCommand;
import commands.classic.ItemCommand;
import commands.classic.WhoisCommand;
import commands.model.Command;
import enums.AnkamaBug;
import enums.Language;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;
import util.Translator;

/**
 * Created by steve on 14/11/2016.
 */
public class NotFoundDiscordException implements DiscordException {

    private String objectKey;

    public NotFoundDiscordException(String objectKey){
        this.objectKey = objectKey;
    }

    @Override
    public void throwException(IMessage message, Command command, Language lg, Object... arguments) {
        AnkamaBug bug = null;
        if (command instanceof WhoisCommand)
            bug = AnkamaBug.CHARACTER_NOT_FOUND;
        else if (command instanceof GuildCommand)
            bug = AnkamaBug.GUILD_NOT_FOUND;
        else if (command instanceof AllianceCommand)
            bug = AnkamaBug.ALLY_NOT_FOUND;
        else if (command instanceof ItemCommand)
            if (message.getContent().contains("'"))
                bug = AnkamaBug.ITEM_NOT_FOUND_APOSTROPHE;
            else if (message.getContent().toLowerCase().contains(Translator.getLabel(lg, "equip.muldo").toLowerCase())
            || message.getContent().toLowerCase().contains(Translator.getLabel(lg, "equip.volkorne").toLowerCase()))
                bug = AnkamaBug.ITEM_PAGE_MULDO_VOLKORNE_NOT_FOUND;

        String gender = Translator.getLabel(lg, "exception.object." + objectKey + ".gender");

        String text = Translator.getLabel(lg, "exception.notfound.not." + gender)
                + " " + Translator.getLabel(lg, "exception.object." + objectKey + ".singular")
                + " " + Translator.getLabel(lg, "exception.notfound.found." + gender) + ".";

        if (bug != null)
            Message.sendEmbed(message.getChannel(), bug.getEmbed(text, lg));
        else
            Message.sendText(message.getChannel(), text);
    }
}

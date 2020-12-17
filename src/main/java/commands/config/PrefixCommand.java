package commands.config;

import commands.model.AbstractCommand;
import data.Constants;
import data.Guild;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import enums.Language;
import exceptions.AdvancedDiscordException;
import exceptions.BasicDiscordException;
import util.Translator;

import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class PrefixCommand extends AbstractCommand {

    private exceptions.DiscordException prefixeOutOfBounds;

    public PrefixCommand(){
        super("prefix","\\s+(.+)");
        setUsableInMP(false);
        prefixeOutOfBounds = new AdvancedDiscordException("exception.advanced.prefix_out_of_bound",
                new String[]{String.valueOf(Constants.prefixeLimit)}, new Boolean[]{false});
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        if (isUserHasEnoughRights(message)) {
            String newPrefix = m.group(1).trim();
            Optional<discord4j.core.object.entity.Guild> guildOptional = message.getGuild().blockOptional();
            if (newPrefix.length() >= 1 && newPrefix.length() <= Constants.prefixeLimit && guildOptional.isPresent()) {
                Guild.getGuild(guildOptional.get()).setPrefix(newPrefix);
                message.getChannel().flatMap(chan -> chan
                        .createMessage(Translator.getLabel(lg, "prefix.request.1")
                                .replace("{prefix}", getPrefixMdEscaped(message))))
                        .subscribe();

                guildOptional.get().getOwner().flatMap(User::getPrivateChannel)
                        .flatMap(chan -> chan.createMessage(Translator.getLabel(lg, "prefix.request.2")
                                .replace("{prefix}", getPrefixMdEscaped(message))
                                .replace("{guild.name}", guildOptional.get().getName())))
                        .subscribe();
            }
            else
                prefixeOutOfBounds.throwException(message, this, lg);
        }
        else
            BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "prefix.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`prefix`* : " + Translator.getLabel(lg, "prefix.help.detailed")
                .replace("{prefixeLimit}", String.valueOf(Constants.prefixeLimit)) + "\n";
    }
}

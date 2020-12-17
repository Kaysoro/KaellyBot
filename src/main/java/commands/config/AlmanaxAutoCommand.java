package commands.config;

import commands.model.AbstractCommand;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import enums.Language;
import exceptions.BasicDiscordException;
import finders.AlmanaxCalendar;
import util.Translator;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class AlmanaxAutoCommand extends AbstractCommand {

    public AlmanaxAutoCommand(){
        super("almanax-auto", "(\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)");
        setUsableInMP(false);
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        if (isUserHasEnoughRights(message)) {
            String guildId = message.getGuild().blockOptional()
                    .map(Guild::getId).map(Snowflake::asString).orElse("");
            String channelId = message.getChannel().blockOptional()
                    .map(MessageChannel::getId).map(Snowflake::asString).orElse("");

            if (m.group(1).matches("\\s+true") || m.group(1).matches("\\s+0") || m.group(1).matches("\\s+on"))
                if (!AlmanaxCalendar.getAlmanaxCalendars().containsKey(channelId)) {
                    new AlmanaxCalendar(guildId, channelId).addToDatabase();
                    message.getChannel().flatMap(chan -> chan
                            .createMessage(Translator.getLabel(lg, "almanax-auto.request.1")))
                            .subscribe();
                } else
                    message.getChannel().flatMap(chan -> chan
                            .createMessage(Translator.getLabel(lg, "almanax-auto.request.2")))
                            .subscribe();
            else if (m.group(1).matches("\\s+false") || m.group(1).matches("\\s+1") || m.group(1).matches("\\s+off"))
                if (AlmanaxCalendar.getAlmanaxCalendars().containsKey(channelId)) {
                    AlmanaxCalendar.getAlmanaxCalendars().get(channelId).removeToDatabase();
                    message.getChannel().flatMap(chan -> chan
                            .createMessage(Translator.getLabel(lg, "almanax-auto.request.3")))
                            .subscribe();
                } else
                    message.getChannel().flatMap(chan -> chan
                            .createMessage(Translator.getLabel(lg, "almanax-auto.request.4")))
                            .subscribe();
        } else
            BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "almanax-auto.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " true` : " + Translator.getLabel(lg, "almanax-auto.help.detailed.1")
                + "\n`" + prefixe + name + " false` : " + Translator.getLabel(lg, "almanax-auto.help.detailed.2") + "\n";
    }
}

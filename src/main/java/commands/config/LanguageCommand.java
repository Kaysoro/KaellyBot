package commands.config;

import commands.model.AbstractCommand;
import data.ChannelLanguage;
import data.Guild;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import enums.Language;
import exceptions.BasicDiscordException;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import exceptions.TooMuchDiscordException;
import util.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class LanguageCommand extends AbstractCommand {

    private DiscordException tooMuchLangs;
    private DiscordException notFoundLang;

    public LanguageCommand(){
        super("lang", "(\\s+-channel)?(\\s+[A-Za-z]+)?");
        setUsableInMP(false);
        tooMuchLangs = new TooMuchDiscordException("lang");
        notFoundLang = new NotFoundDiscordException("lang");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        Optional<discord4j.core.object.entity.Guild> guild = message.getGuild().blockOptional();
        Optional<MessageChannel> channel = message.getChannel().blockOptional();

        if (guild.isPresent() && channel.isPresent())
            if (m.group(2) != null) { // Ajouts
                String channelName = ((GuildMessageChannel) channel.get()).getName();
                if (isUserHasEnoughRights(message)) {
                    List<Language> langs = new ArrayList<>();
                    for(Language lang : Language.values())
                        if (m.group(2).trim().toUpperCase().equals(lang.getAbrev()))
                            langs.add(lang);

                    if (langs.size() == 1) {
                        if (m.group(1) == null) {
                            Guild.getGuild(guild.get()).setLanguage(langs.get(0));
                            final Language LG = langs.get(0);
                            message.getChannel().flatMap(chan -> chan
                                    .createMessage(guild.get().getName() + " " + Translator
                                            .getLabel(LG, "lang.request.1") + " " + langs.get(0)))
                                    .subscribe();
                        } else {
                            ChannelLanguage chan = ChannelLanguage.getChannelLanguages().get(channel.get().getId().asLong());
                            if (chan != null){
                                if (chan.getLang().equals(langs.get(0))){
                                    chan.removeToDatabase();
                                    final Language LG = Translator.getLanguageFrom(channel.get());
                                    message.getChannel().flatMap(salon -> salon
                                            .createMessage(channelName
                                                    + " " + Translator.getLabel(LG, "lang.request.2") + " "
                                                    + Guild.getGuild(guild.get()).getLanguage()))
                                            .subscribe();
                                }
                                else {
                                    chan.setLanguage(langs.get(0));
                                    final Language LG = langs.get(0);
                                    message.getChannel().flatMap(salon -> salon
                                            .createMessage(channelName + " " + Translator
                                                    .getLabel(LG, "lang.request.1") + " " + chan.getLang()))
                                            .subscribe();
                                }
                            }
                            else {
                                final Language LG = langs.get(0);
                                final ChannelLanguage CHAN = new ChannelLanguage(langs.get(0), channel.get().getId().asLong());
                                CHAN.addToDatabase();
                                message.getChannel().flatMap(salon -> salon
                                        .createMessage(channelName + " " + Translator
                                                .getLabel(LG, "lang.request.1") + " " + CHAN.getLang()))
                                        .subscribe();
                            }
                        }
                    }
                    else if (langs.isEmpty())
                        notFoundLang.throwException(message, this, lg);
                    else
                        tooMuchLangs.throwException(message, this, lg);

                } else
                    BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
            }
            else { // Consultation
                String text = "**" + guild.get().getName() + "** " + Translator.getLabel(lg, "lang.request.3")
                        + " " + Guild.getGuild(guild.get()).getLanguage() + ".";

                ChannelLanguage chanLang = ChannelLanguage.getChannelLanguages().get(channel.get().getId().asLong());
                if (chanLang != null)
                    text += "\nLe salon *" + ((GuildMessageChannel) channel.get()).getName() + "* "
                    + Translator.getLabel(lg, "lang.request.3")
                    + " " + chanLang.getLang() + ".";
                final String TEXT = text;
                message.getChannel().flatMap(salon -> salon.createMessage(TEXT)).subscribe();
            }
    }

    @Override
    public String help(Language lg, String prefixe) {
        StringBuilder st = new StringBuilder(" (");
        for(Language lang : Language.values())
            st.append(lang.getAbrev()).append(", ");
        st.setLength(st.length() - 2);
        st.append(").");

        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "lang.help") + st.toString();
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`language`* : " + Translator.getLabel(lg, "lang.help.detailed.1")
                + "\n`" + prefixe + name + " -channel `*`language`* : " + Translator.getLabel(lg, "lang.help.detailed.2") + "\n";
    }
}

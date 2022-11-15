package commands.classic;

import commands.model.AbstractLegacyCommand;
import data.Constants;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.rest.util.Image;
import enums.Donator;
import enums.Language;
import util.Translator;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static data.Constants.authorAvatar;
import static data.Constants.authorName;

/**
 * Created by Kaysoro on 20/05/2019.
 */
public class DonateCommand extends AbstractLegacyCommand {

    public DonateCommand() {
        super("donate", "");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        Optional<ApplicationInfo> appInfo = message.getClient().getApplicationInfo().blockOptional();

        if (appInfo.isPresent()) {
            Optional<User> author = appInfo.get().getOwner().blockOptional();

            message.getChannel().flatMap(chan -> chan
                    .createEmbed(spec -> {
                        spec.setTitle(Translator.getLabel(lg, "about.title")
                                .replace("{name}", Constants.name)
                                .replace("{version}", Constants.version))
                                .setDescription(Translator.getLabel(lg, "about.free.desc")
                                        .replace("{paypal}", Constants.paypal))
                                .setThumbnail(appInfo.get().getIconUrl(Image.Format.PNG).orElse(null))
                                .setAuthor(author.map(User::getUsername).orElse(authorName), null,
                                        author.map(User::getAvatarUrl).orElse(authorAvatar));
                        spec.addField(Translator.getLabel(lg, "donate.thanks"),
                                Stream.of(Donator.values())
                                        .map(Donator::getName)
                                        .collect(Collectors.joining("**\n- **", "- **", "**")),
                                false);
                    })).subscribe();
        }
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "donate.help")
                .replace("{name}", Constants.name);
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe);
    }
}

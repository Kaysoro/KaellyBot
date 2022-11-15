package commands.classic;

import commands.model.AbstractLegacyCommand;
import commands.model.SlashCommand;
import data.Constants;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Image;
import enums.Donator;
import enums.Graphist;
import enums.Language;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Mono;
import util.Translator;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Songfu on 29/05/2017.
 */
public class AboutCommand extends AbstractLegacyCommand implements SlashCommand {

    public AboutCommand() {
        super("about", "");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
            message.getChannel()
                    .flatMap(chan -> chan.createMessage(aboutEmbed(message.getClient(), lg)))
                    .subscribe();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.reply()
                .withEphemeral(true)
                .withEmbeds(aboutEmbed(event.getClient(),
                        Translator.mapLocale(event.getInteraction().getUserLocale())));
    }

    @Override
    public ApplicationCommandRequest getCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description(help(Constants.defaultLanguage, ""))
                .descriptionLocalizationsOrNull(Stream.of(Language.values())
                        .flatMap(lg -> lg.getLocales().stream()
                                .map(locale -> Pair.of(locale, help(lg, ""))))
                        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)))
                .build();
    }

    private EmbedCreateSpec aboutEmbed(GatewayDiscordClient client, Language lg){
        Optional<ApplicationInfo> appInfo = client.getApplicationInfo().blockOptional();
        StringBuilder donors = new StringBuilder();
        for (Donator donator : Donator.values())
            donors.append(donator.getName()).append(", ");
        donors.setLength(donors.length() - 2);

        return EmbedCreateSpec.builder()
                .title(Translator.getLabel(lg, "about.title")
                        .replace("{name}", Constants.name)
                        .replace("{version}", Constants.version))
                .image(Constants.changelog)
                .thumbnail(appInfo.flatMap(app -> app.getIconUrl(Image.Format.PNG)).orElse(""))
                .addField(Translator.getLabel(lg, "about.invite.title"),
                        Translator.getLabel(lg, "about.invite.desc")
                                .replace("{name}", Constants.name)
                                .replace("{invite}", Constants.invite), false)
                .addField(Translator.getLabel(lg, "about.support.title"),
                        Translator.getLabel(lg, "about.support.desc")
                                .replace("{name}", Constants.name)
                                .replace("{discordInvite}", Constants.discordInvite), false)
                .addField(Translator.getLabel(lg, "about.twitter.title"),
                        Translator.getLabel(lg, "about.twitter.desc")
                                .replace("{name}", Constants.name)
                                .replace("{twitter}", Constants.twitterAccount), false)
                .addField(Translator.getLabel(lg, "about.opensource.title"),
                        Translator.getLabel(lg, "about.opensource.desc")
                                .replace("{git}", Constants.git), false)
                .addField(Translator.getLabel(lg, "about.free.title"),
                        Translator.getLabel(lg, "about.free.desc")
                                .replace("{paypal}", Constants.paypal), false)
                .addField(Translator.getLabel(lg, "about.privacy.title"),
                        Translator.getLabel(lg, "about.privacy.desc")
                                .replace("{paypal}", Constants.paypal), false)
                .addField(Translator.getLabel(lg, "about.graphist.title"),
                        Translator.getLabel(lg, "about.graphist.desc")
                                .replace("{graphist}", Graphist.ELYCANN.toMarkdown()), false)
                .addField(Translator.getLabel(lg, "about.donators.title"), donors + ".", false)
                .build();
    }

    @Override
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "about.help")
                .replace("{name}", Constants.name);
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return help(lg, prefix);
    }
}

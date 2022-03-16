package commands.classic;

import commands.model.AbstractCommand;
import data.Constants;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.rest.util.Image;
import enums.Donator;
import enums.Graphist;
import enums.Language;
import util.Translator;

import java.util.Optional;
import java.util.regex.Matcher;

import static data.Constants.authorAvatar;
import static data.Constants.authorName;

/**
 * Created by Songfu on 29/05/2017.
 */
public class AboutCommand extends AbstractCommand {

    public AboutCommand() {
        super("about", "");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        Optional<ApplicationInfo> appInfo = message.getClient().getApplicationInfo().blockOptional();

        if (appInfo.isPresent()) {
            Optional<User> author = appInfo.get().getOwner().blockOptional();

            message.getChannel().flatMap(chan -> chan.createEmbed(spec -> {

                spec.setTitle(Translator.getLabel(lg, "about.title")
                                .replace("{name}", Constants.name)
                                .replace("{version}", Constants.version))
                        .setImage(Constants.changelog)
                        .setThumbnail(appInfo.get().getIconUrl(Image.Format.PNG).orElse(null))
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
                                        .replace("{graphist}", Graphist.ELYCANN.toMarkdown()), false);
                StringBuilder st = new StringBuilder();
                for(Donator donator : Donator.values())
                    st.append(donator.getName()).append(", ");
                st.setLength(st.length() - 2);
                spec.addField(Translator.getLabel(lg, "about.donators.title"),
                        st.toString() + ".", false);
            })).subscribe();
        }
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "about.help")
                .replace("{name}", Constants.name);
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe);
    }
}

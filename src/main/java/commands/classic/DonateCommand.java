package commands.classic;

import commands.model.AbstractCommand;
import data.Constants;
import enums.Donator;
import enums.Language;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import util.ClientConfig;
import util.Message;
import util.Translator;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.stream.Stream;

/**
 * Created by Kaysoro on 20/05/2019.
 */
public class DonateCommand extends AbstractCommand {

    public DonateCommand() {
        super("donate", "");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        IUser author = ClientConfig.DISCORD().getApplicationOwner();
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(Translator.getLabel(lg, "about.title")
                    .replace("{name}", Constants.name)
                    .replace("{version}", Constants.version))
                .withDesc(Translator.getLabel(lg, "about.free.desc")
                        .replace("{paypal}", Constants.paypal))
                .withColor(new Random().nextInt(16777216))
                .withThumbnail(ClientConfig.DISCORD().getApplicationIconURL())
                .withAuthorName(author.getName())
                .withAuthorIcon(author.getAvatarURL());

        Stream.of(Donator.values())
                .forEach(donator -> builder.appendField(donator.getName(),
                       Translator.getLabel(lg, "donator." + donator.name().toLowerCase() + ".desc"), false));
        Message.sendEmbed(message.getChannel(), builder.build());
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

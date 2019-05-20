package commands.classic;

import commands.model.AbstractCommand;
import data.Constants;
import enums.Language;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import util.ClientConfig;
import util.Message;
import util.Translator;

import java.util.Random;
import java.util.regex.Matcher;

/**
 * Created by Kaysoro on 20/05/2019.
 */
public class InviteCommand extends AbstractCommand {

    public InviteCommand() {
        super("invite", "");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        EmbedBuilder builder = new EmbedBuilder();
        IUser author = ClientConfig.DISCORD().getApplicationOwner();
        builder.withTitle(Translator.getLabel(lg, "about.title")
                    .replace("{name}", Constants.name)
                    .replace("{version}", Constants.version))
                .withColor(new Random().nextInt(16777216))
                .withThumbnail(ClientConfig.DISCORD().getApplicationIconURL())
                .withAuthorName(author.getName())
                .withAuthorIcon(author.getAvatarURL());

        builder.appendField(Translator.getLabel(lg, "about.invite.title"),
                Translator.getLabel(lg, "about.invite.desc")
                    .replace("{name}", Constants.name)
                    .replace("{invite}", Constants.invite), true)
        .appendField(Translator.getLabel(lg, "about.support.title"),
                Translator.getLabel(lg, "about.support.desc")
                    .replace("{name}", Constants.name)
                    .replace("{discordInvite}", Constants.discordInvite), true);

        Message.sendEmbed(message.getChannel(), builder.build());
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "invite.help")
                .replace("{name}", Constants.name);
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe);
    }
}

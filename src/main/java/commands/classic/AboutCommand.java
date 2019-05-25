package commands.classic;

import commands.model.AbstractCommand;
import data.Constants;
import enums.Donator;
import enums.Graphist;
import enums.Language;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import util.ClientConfig;
import util.Message;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.util.Random;
import java.util.regex.Matcher;

/**
 * Created by Songfu on 29/05/2017.
 */
public class AboutCommand extends AbstractCommand {

    public AboutCommand() {
        super("about", "");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        IUser author = ClientConfig.DISCORD().getApplicationOwner();
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(Translator.getLabel(lg, "about.title")
                    .replace("{name}", Constants.name)
                    .replace("{version}", Constants.version))
                .withDesc(Translator.getLabel(lg, "about.desc")
                    .replace("{game}", Constants.game.getName()))
                .withColor(new Random().nextInt(16777216))
                .withThumbnail(ClientConfig.DISCORD().getApplicationIconURL())
                .withAuthorName(author.getName())
                .withAuthorIcon(author.getAvatarURL())
                .withImage(Constants.changelog);

        builder.appendField(Translator.getLabel(lg, "about.invite.title"),
                Translator.getLabel(lg, "about.invite.desc")
                    .replace("{name}", Constants.name)
                    .replace("{invite}", Constants.invite), true)
        .appendField(Translator.getLabel(lg, "about.support.title"),
                Translator.getLabel(lg, "about.support.desc")
                    .replace("{name}", Constants.name)
                    .replace("{discordInvite}", Constants.discordInvite), true)
        .appendField(Translator.getLabel(lg, "about.twitter.title"),
                Translator.getLabel(lg, "about.twitter.desc")
                    .replace("{name}", Constants.name)
                    .replace("{twitter}", Constants.twitterAccount), true)
        .appendField(Translator.getLabel(lg, "about.opensource.title"),
                Translator.getLabel(lg, "about.opensource.desc")
                    .replace("{git}", Constants.git), true)
        .appendField(Translator.getLabel(lg, "about.free.title"),
                Translator.getLabel(lg, "about.free.desc")
                    .replace("{paypal}", Constants.paypal), true)
        .appendField(Translator.getLabel(lg, "about.graphist.title"),
                Translator.getLabel(lg, "about.graphist.desc")
                        .replace("{graphist}", Graphist.ELYCANN.toMarkdown()), true);

        StringBuilder st = new StringBuilder();
        for(Donator donator : Donator.values())
            st.append(donator.getName()).append(", ");
        st.setLength(st.length() - 2);
        builder.appendField(Translator.getLabel(lg, "about.donators.title"), st.toString() + ".", true);

        Message.sendEmbed(message.getChannel(), builder.build());
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

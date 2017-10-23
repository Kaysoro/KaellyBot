package commands;

import data.Constants;
import enums.Donator;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import util.ClientConfig;
import util.Message;
import sx.blah.discord.handle.obj.IMessage;
import java.util.Random;

/**
 * Created by Songfu on 29/05/2017.
 */
public class AboutCommand extends AbstractCommand{

    public AboutCommand() {
        super("about", "");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){
            IUser author = ClientConfig.DISCORD().getApplicationOwner();
            EmbedBuilder builder = new EmbedBuilder();

            builder.withTitle(Constants.name + " version " + Constants.version)
            .withDesc("Bot Discord dédié au jeu " + Constants.game + " !")
                    .withColor(new Random().nextInt(16777216))
                    .withThumbnail(ClientConfig.DISCORD().getApplicationIconURL())
                    .withAuthorName(author.getName())
                    .withAuthorIcon(author.getAvatarURL());

            builder.appendField(":electric_plug: Lien d'invitation :",
                    Constants.name +" peut être invité sur vos serveurs grâce à ce [lien]("
                            + Constants.invite + "). Vous devrez avoir les droits suffisants pour effectuer l'action !", true)
            .appendField(":bulb: Serveur support :",
                    "Vous rencontrez un problème quant à son utilisation ? Une suggestion ? "
                            + "Une amélioration à proposer ? N'hésitez pas à rejoindre le [Discord de "
                            + Constants.name +"](" + Constants.discordInvite + ") !", true)
            .appendField("<:github:372093338628784148> Open source :",
                    "L'intégralité du code source est sous licence GPL-3.0 et accessible sur [Github]("
                            + Constants.git + ").", true)
            .appendField(":money_with_wings: Gratuit :", "L'ensemble des fonctionnalités sont gratuites. "
                    + "Il vous est possible de participer financièrement mais n'allez pas vous mettre dans le rouge ! "
                    + " [Paypal](" + Constants.paypal + ")", true);

            try {
                IChannel news = ClientConfig.DISCORD().getChannelByID(Constants.newsChan);
                builder.appendField(":bookmark_tabs: Changelog :", news.getFullMessageHistory().getLatestMessage().getContent(), true);
            } catch(Exception e) {e.printStackTrace();}

            StringBuilder st = new StringBuilder();
            for(Donator donator : Donator.values())
                st.append(donator.getName()).append(", ");
            st.setLength(st.length() - 2);
            builder.appendField(":ok_woman: Donateurs", st.toString() + ".", true);

            Message.sendEmbed(message.getChannel(), builder.build());
            return true;
        }

        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** donne des informations sur " + Constants.name + " et un moyen d'obtenir de l'aide.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe);
    }
}

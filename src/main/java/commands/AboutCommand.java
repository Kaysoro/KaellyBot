package commands;

import data.Constants;
import discord.Message;
import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Pattern;

/**
 * Created by Songfu on 29/05/2017.
 */
public class AboutCommand extends AbstractCommand{

    public AboutCommand() {
        super(Pattern.compile("about"),
        Pattern.compile("^(" + Constants.prefixCommand + "about)$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){

            Message.sendText(message.getChannel(), Constants.name + " est destinée à fournir des commandes utiles à la communauté de dofus !\nL'intégralité de son code est libre d'accès et est disponible ici : " + Constants.git + "\nSi vous avez des questions, des suggestions ou que vous souhaitez juste passer un coucou, rejoignez le discord de " + Constants.name + " : https://discord.gg/VsrbrYC Promis, on ne mord pas ! :yum:");
            return true;
        }

        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "about** donne des informations sur " + Constants.name + "et un moyen d'obtenir de l'aide.";
    }

    @Override
    public String helpDetailed() {
        return help();
    }
}

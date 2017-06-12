package commands;

import data.Constants;
import discord.Message;
import sx.blah.discord.handle.obj.IMessage;

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

            Message.sendText(message.getChannel(), Constants.about);
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

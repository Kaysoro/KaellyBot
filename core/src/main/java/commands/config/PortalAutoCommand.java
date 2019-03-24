package commands.config;

import commands.model.AbstractCommand;
import data.Guild;
import enums.Language;
import exceptions.BasicDiscordException;
import finders.PortalTracker;
import sx.blah.discord.handle.obj.IMessage;
import util.Message;
import util.Translator;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class PortalAutoCommand extends AbstractCommand {

    public PortalAutoCommand(){
        super("pos-auto","(\\s+true|\\s+false|\\s+0|\\s+1|\\s+on|\\s+off)");
        setUsableInMP(false);
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        //On check si la personne a bien les droits pour exécuter cette commande
        if (isUserHasEnoughRights(message)) {
            String value = m.group(1);

            if (value.matches("\\s+true") || value.matches("\\s+0") || value.matches("\\s+on")){

                if (!Guild.getGuild(message.getGuild()).getPortalTrackers().containsKey(message.getChannel().getStringID())) {
                    new PortalTracker(message.getGuild().getStringID(), message.getChannel().getStringID()).addToDatabase();
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "portal-auto.request.1"));
                }
                else
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "portal-auto.request.2"));
            }
            else if (value.matches("\\s+false") || value.matches("\\s+1") || value.matches("\\s+off"))
                if (Guild.getGuild(message.getGuild()).getPortalTrackers().containsKey(message.getChannel().getStringID())){
                    Guild.getGuild(message.getGuild()).getPortalTrackers().get(message.getChannel().getStringID()).removeToDatabase();
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "portal-auto.request.3"));
                }
                else
                    Message.sendText(message.getChannel(), Translator.getLabel(lg, "portal-auto.request.4"));
            else
                badUse.throwException(message, this, lg);
        } else
            BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "portal-auto.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " true` : " + Translator.getLabel(lg, "portal-auto.help.detailed.1")
                + "\n`" + prefixe + name + " false` : " + Translator.getLabel(lg, "portal-auto.help.detailed.2") + "\n";
    }
}

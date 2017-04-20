package commands;

import data.Constants;
import data.ItemDofus;
import exceptions.InDeveloppmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class ItemCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(ItemCommand.class);

    public ItemCommand(){
        super(Pattern.compile("item"),
        Pattern.compile("^(" + Constants.prefixCommand + "item)\\s+(.*)$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){

            //TODO

            new InDeveloppmentException().throwException(message, this);
            return true;
        }

        return false;
    }

    @Override
    public boolean isUsableInMP() {
        return true;
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "item** renvoit les statistiques d'un item du jeu Dofus.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "item `*`item`* : renvoit les statistiques de l'item spécifié :"
                + " son nom doit être exact.\n";
    }
}

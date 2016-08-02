package commands;

import data.ClientConfig;
import data.Constants;
import data.ItemDofus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class ItemCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(ItemCommand.class);
    private List<ItemDofus> items;

    public ItemCommand(){
        super(Pattern.compile("item"),
        Pattern.compile("^(!item)\\W+(.*)$"));
        items = ItemDofus.getItems();
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)){
            //TODO
            RequestBuffer.request(() -> {
                try {
                    new MessageBuilder(ClientConfig.CLIENT())
                            .withChannel(message.getChannel())
                            .withContent("Cette commande est en cours de développement "
                                    + message.getAuthor()
                                    + ", tu pourrais attendre un peu quand même !")
                            .build();
                } catch (DiscordException e){
                    LOG.error(e.getErrorMessage());
                } catch(MissingPermissionsException e){
                    LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                }
                return null;
            });
            return true;
        }

        return false;
    }

    @Override
    public String help() {
        //TODO
        return null;
    }

    @Override
    public String helpDetailed() {
        //TODO
        return null;
    }
}

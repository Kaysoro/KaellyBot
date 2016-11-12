package commands;

import data.ClientConfig;
import data.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class MapCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(MapCommand.class);

    public MapCommand(){
        super(Pattern.compile("map"),
              Pattern.compile("^(" + Constants.prefixCommand + "map)([\\W+\\w+]*)$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            String[] maps;
            try {
                if (m.group(2).replaceAll("^\\W+", "").length() > 0)
                    maps = m.group(2).replaceAll("^\\W+", "").split("\\W+");
                else
                    maps = new String[]{"I", "II", "III", "IV", "V", "VI",
                            "VII", "VIII", "IX", "X", "XI", "XII"};

                RequestBuffer.request(() -> {
                    try {
                        new MessageBuilder(ClientConfig.CLIENT())
                                .withChannel(message.getChannel())
                                .withContent(message.getAuthor() + ", le combat aura lieu sur la carte "
                                + maps[new Random().nextInt(maps.length)] + " !")
                                .build();
                    } catch (DiscordException e){
                        LOG.error(e.getErrorMessage());
                    } catch(MissingPermissionsException e){
                        LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                    }
                    return null;
                });

            } catch(Exception e ){
                e.printStackTrace();
                LOG.warn("Malformation des arguments de la requête");
                RequestBuffer.request(() -> {
                    try {
                        new MessageBuilder(ClientConfig.CLIENT()).withChannel(message.getChannel())
                                .withContent(message.getAuthor() + ", !map ne s'utilise pas comme ça !"
                                        + "\nVoici un exemple : `!map 1 2 3`").build();

                    } catch (DiscordException e2){
                        LOG.error(e2.getErrorMessage());
                    } catch(MissingPermissionsException e2){
                        LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                    }
                    return null;
                });
            }
            return true;
        }
        return false;
    }

    @Override
    public String help() {
        return "**" + Constants.prefixCommand + "map** sélectionne au hasard une carte du Goultarminator ou bien parmi celles"
                + " spécifiées en paramètre.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`" + Constants.prefixCommand + "map` : sélectionne une carte du Goultarminator entre I et XII compris."
                + "\n`" + Constants.prefixCommand + "map `*`map1, map2, ...`* : sélectionne une carte parmi celles spécifiées en paramètre.\n";
    }
}

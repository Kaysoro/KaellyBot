package commands;

import data.Constants;
import discord.Message;
import exceptions.BadUseCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
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

                Message.send(message.getChannel(), message.getAuthor() + ", le combat aura lieu sur la carte "
                        + maps[new Random().nextInt(maps.length)] + " !");

            } catch(Exception e ){
                LOG.warn("Malformation des arguments de la requête : " + e.getMessage());
                new BadUseCommandException().throwException(message, this);
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

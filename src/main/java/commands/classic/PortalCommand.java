package commands.classic;

import commands.model.AbstractLegacyCommand;
import data.*;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import enums.Dimension;
import enums.Language;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import exceptions.TooMuchDiscordException;
import external.DofusPortalsAPI;
import mapper.PortalMapper;
import payloads.PortalDto;
import util.ServerUtils;
import util.Translator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class PortalCommand extends AbstractLegacyCommand {

    private DofusPortalsAPI dofusPortalsAPI;
    private DiscordException tooMuchPortals;
    private DiscordException notFoundPortal;
    private DiscordException notFoundServer;

    public PortalCommand(){
        super("pos", "(\\s+\\p{L}+)?");
        setUsableInMP(false);
        dofusPortalsAPI = new DofusPortalsAPI();
        tooMuchPortals = new TooMuchDiscordException("portal");
        notFoundPortal = new NotFoundDiscordException("portal");
        notFoundServer = new NotFoundDiscordException("server");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        ServerDofus server = ServerUtils.getDofusServerFrom(Guild.getGuild(message.getGuild().block()), message.getChannel().block());

        if (server != null){
            if (m.group(1) == null) { // No dimension precised
                List<PortalDto> portals = Optional.ofNullable(dofusPortalsAPI.getPositions(server)).orElse(Collections.emptyList());
                for(PortalDto pos : portals)
                    message.getChannel().flatMap(chan -> chan
                            .createEmbed(PortalMapper.decorateSpec(pos, lg)))
                            .subscribe();
            }
            else {
                final List<Dimension> DIMENSIONS = getDimensions(m.group(1), lg);
                if (DIMENSIONS.size() == 1) {
                    Optional.ofNullable(dofusPortalsAPI.getPosition(server, DIMENSIONS.get(0)))
                            .ifPresent(pos -> message.getChannel().flatMap(chan -> chan
                                    .createEmbed(PortalMapper.decorateSpec(pos, lg)))
                                    .subscribe());
                }
                else if (DIMENSIONS.isEmpty())
                    notFoundPortal.throwException(message, this, lg);
                else
                    tooMuchPortals.throwException(message, this, lg);
            }
        }
        else
            notFoundServer.throwException(message, this, lg);
    }

    private List<Dimension> getDimensions(String nameProposed, Language lg){
        nameProposed = Normalizer.normalize(nameProposed, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        nameProposed = nameProposed.replaceAll("\\W+", "");
        List<Dimension> dimensions = new ArrayList<>();

        for(Dimension dimension : Dimension.values())
            if (Normalizer.normalize(dimension.getLabel(lg), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase().startsWith(nameProposed))
                dimensions.add(dimension);
        return dimensions;
    }

    @Override
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "portal.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return help(lg, prefix)
                + "\n`" + prefix + name + "` : " + Translator.getLabel(lg, "portal.help.detailed.1")
                + "\n`" + prefix + name + " `*`dimension`* : " + Translator.getLabel(lg, "portal.help.detailed.2")
                + "\n";
    }
}
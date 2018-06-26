package commands.config;

import commands.model.AbstractCommand;
import data.Constants;
import data.Guild;
import data.ServerDofus;
import enums.Language;
import exceptions.BasicDiscordException;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import exceptions.TooMuchDiscordException;
import util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class ServerCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(ServerCommand.class);
    private DiscordException tooMuchServers;
    private DiscordException notFoundServer;
    private DiscordException noEnoughRights;

    public ServerCommand(){
        super("server","(\\s+.+)?");
        setUsableInMP(false);
        tooMuchServers = new TooMuchDiscordException("exception.toomuch.servers", "exception.toomuch.servers_found");
        notFoundServer = new NotFoundDiscordException("exception.notfound.server", "exception.notfound.server_found");
        noEnoughRights = new BasicDiscordException("exception.basic.no_enough_rights");
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Guild guild = Guild.getGuild(message.getGuild());
            Language lg = Translator.getLanguageFrom(message.getChannel());
            Matcher m = getMatcher(message);
            m.find();
            if (m.group(1) != null)
                if (isUserHasEnoughRights(message)) {
                    String serverName = m.group(1).toLowerCase().trim();

                    if (! serverName.equals("-reset")) {
                        serverName = Normalizer.normalize(serverName, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll("\\W+", "").trim();
                        List<ServerDofus> result = new ArrayList<>();
                        for (ServerDofus server : ServerDofus.getServersDofus())
                            if (Normalizer.normalize(server.getName(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll("\\W+", "").toLowerCase().trim().startsWith(serverName))
                                result.add(server);

                        if (result.size() == 1) {
                            guild.setServer(result.get(0));
                            Message.sendText(message.getChannel(), Translator.getLabel(lg, "server.request.1")
                                    .replace("{game}", Constants.game)
                                    + " " + guild.getName() + " " + Translator.getLabel(lg, "server.request.2")
                                    + " " + result.get(0).getName() + ".");
                        } else if (result.isEmpty())
                            notFoundServer.throwException(message, this, lg);
                        else
                            tooMuchServers.throwException(message, this, lg, result);
                    }
                    else {
                        guild.setServer(null);
                        Message.sendText(message.getChannel(), guild.getName()
                                + " " + Translator.getLabel(lg, "server.request.3")
                                .replace("{game}", Constants.game));
                    }
                }
                else
                    noEnoughRights.throwException(message, this, lg);
            else {
                if (guild.getServerDofus() != null)
                    Message.sendText(message.getChannel(), guild.getName() + " "
                            + Translator.getLabel(lg, "server.request.4") + " "
                            + guild.getServerDofus().getName() + ".");
                else
                    Message.sendText(message.getChannel(), guild.getName()
                            + " " + Translator.getLabel(lg, "server.request.5")
                            .replace("{game}", Constants.game));
            }
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "server.help")
                .replace("{game}", Constants.game);
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + "` : " + Translator.getLabel(lg, "server.help.detailed.1")
                .replace("{game}", Constants.game)
                + "\n" + prefixe + "`"  + name + " `*`server`* : " + Translator.getLabel(lg, "server.help.detailed.2")
                .replace("{game}", Constants.game)
                + "\n" + prefixe + "`"  + name + " `*`-reset`* : " + Translator.getLabel(lg, "server.help.detailed.3")
                .replace("{game}", Constants.game) + "\n";
    }
}
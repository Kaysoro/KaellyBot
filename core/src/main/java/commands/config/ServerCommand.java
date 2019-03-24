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

    private DiscordException tooMuchServers;
    private DiscordException notFoundServer;

    public ServerCommand(){
        super("server","(\\s+.+)?");
        setUsableInMP(false);
        tooMuchServers = new TooMuchDiscordException("server");
        notFoundServer = new NotFoundDiscordException("server");
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        Guild guild = Guild.getGuild(message.getGuild());

        if (m.group(1) != null)
            if (isUserHasEnoughRights(message)) {
                String serverName = m.group(1).toLowerCase().trim();

                if (! serverName.equals("-reset")) {
                    serverName = Normalizer.normalize(serverName, Normalizer.Form.NFD)
                            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                            .replaceAll("\\W+", "").trim();
                    List<ServerDofus> result = new ArrayList<>();
                    for (ServerDofus server : ServerDofus.getServersDofus())
                        if (Normalizer.normalize(server.getName(), Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                                .replaceAll("\\W+", "").toLowerCase().trim()
                                .startsWith(serverName))
                            result.add(server);

                    if (result.size() == 1) {
                        if (guild.getServerDofus() != null && guild.getServerDofus() != result.get(0))
                            guild.resetPortals();
                        guild.setServer(result.get(0));
                        guild.mergePortals(result.get(0).getSweetPortals());
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
                BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
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

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "server.help")
                .replace("{game}", Constants.game);
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + "` : " + Translator.getLabel(lg, "server.help.detailed.1")
                    .replace("{game}", Constants.game)
                + "\n`" + prefixe + name + " `*`server`* : " + Translator.getLabel(lg, "server.help.detailed.2")
                    .replace("{game}", Constants.game)
                + "\n`" + prefixe + name + " `*`-reset`* : " + Translator.getLabel(lg, "server.help.detailed.3")
                    .replace("{game}", Constants.game) + "\n";
    }
}
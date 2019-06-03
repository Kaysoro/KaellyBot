package commands.config;

import commands.model.AbstractCommand;
import data.Constants;
import data.Guild;
import data.ServerDofus;
import enums.Game;
import enums.Language;
import exceptions.BasicDiscordException;
import util.Message;
import sx.blah.discord.handle.obj.IMessage;
import util.ServerUtils;
import util.Translator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by steve on 14/07/2016.
 */
public class ServerCommand extends AbstractCommand {

    public ServerCommand(){
        super("server","(\\s+.+)?");
        setUsableInMP(false);
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        Guild guild = Guild.getGuild(message.getGuild());

        if (m.group(1) != null){
            String serverName = m.group(1).toLowerCase().trim();

            if (serverName.equals("-list")) {
                String sb = Translator.getLabel(lg, "server.list") + "\n" + getServersList();
                Message.sendText(message.getChannel(), sb);
                return;
            }

            if (isUserHasEnoughRights(message)) {
                if (!serverName.equals("-reset")) {
                    ServerUtils.ServerQuery query = ServerUtils.getServerDofusFromName(serverName);

                    if (query.hasSucceed()) {
                        guild.setServer(query.getServer());
                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "server.request.1")
                                .replace("{game}", Constants.game.getName())
                                + " " + guild.getName() + " " + Translator.getLabel(lg, "server.request.2")
                                + " " + query.getServer().getName() + ".");
                    } else
                        query.getExceptions()
                                .forEach(exception -> exception.throwException(message, this, lg, query.getServersFound()));
                } else {
                    guild.setServer(null);
                    Message.sendText(message.getChannel(), guild.getName()
                            + " " + Translator.getLabel(lg, "server.request.3")
                            .replace("{game}", Constants.game.getName()));
                }
            } else
                BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
        }
        else {
            if (guild.getServerDofus() != null)
                Message.sendText(message.getChannel(), guild.getName() + " "
                        + Translator.getLabel(lg, "server.request.4") + " "
                        + guild.getServerDofus().getName() + ".");
            else
                Message.sendText(message.getChannel(), guild.getName()
                        + " " + Translator.getLabel(lg, "server.request.5")
                        .replace("{game}", Constants.game.getName()));
        }
    }

    @Override
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "server.help")
                .replace("{game}", Constants.game.getName());
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return help(lg, prefix)
                + "\n`" + prefix + name + "` : " + Translator.getLabel(lg, "server.help.detailed.1")
                    .replace("{game}", Constants.game.getName())
                + "\n`" + prefix + name + " -list` : " + Translator.getLabel(lg, "server.help.detailed.2")
                .replace("{game}", Constants.game.getName())
                + "\n`" + prefix + name + " `*`server`* : " + Translator.getLabel(lg, "server.help.detailed.3")
                    .replace("{game}", Constants.game.getName())
                + "\n`" + prefix + name + " -channel `*`server`* : " + Translator.getLabel(lg, "server.help.detailed.4")
                    .replace("{game}", Constants.game.getName())
                + "\n`" + prefix + name + " -reset` : " + Translator.getLabel(lg, "server.help.detailed.5")
                .replace("{game}", Constants.game.getName())
                + "\n`" + prefix + name + " -channel -reset` : " + Translator.getLabel(lg, "server.help.detailed.6")
                .replace("{game}", Constants.game.getName())
                + "\n";
    }

    private String getServersList() {
        final List<String> SERVERS = ServerDofus.getServersDofus().stream()
                .filter(server -> server.getGame() == Game.DOFUS)
                .map(ServerDofus::getName)
                .sorted()
                .collect(Collectors.toList());
        final long SIZE_MAX = SERVERS.stream()
                .map(String::length)
                .max(Integer::compare)
                .orElse(20);

        StringBuilder sb = new StringBuilder("```");
        SERVERS.forEach(serverName -> sb.append(String.format("%-" + SIZE_MAX + "s", serverName)).append("\t"));
        sb.append("```");
        return sb.toString();
    }
}
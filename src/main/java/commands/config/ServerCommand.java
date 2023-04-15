package commands.config;

import commands.model.AbstractCommand;
import data.*;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import enums.Game;
import enums.Language;
import exceptions.BasicDiscordException;
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
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        Guild guild = Guild.getGuild(message.getGuild().block());
        MessageChannel channel = message.getChannel().block();

        if (m.group(1) != null){
            String serverName = m.group(1).toLowerCase().trim();

            if (serverName.equals("-list")) {
                String sb = Translator.getLabel(lg, "server.list") + "\n" + getServersList(lg);
                message.getChannel().flatMap(chan -> chan.createMessage(sb)).subscribe();
                return;
            }

            if (isUserHasEnoughRights(message)) {
                if (!serverName.startsWith("-channel")) {
                    ServerUtils.ServerQuery query = ServerUtils.getServerDofusFromName(serverName, lg);

                    if (query.hasSucceed()) {
                        guild.setServer(query.getServer());
                        message.getChannel().flatMap(chan -> chan
                                .createMessage(Translator.getLabel(lg, "server.request.1")
                                        .replace("{game}", Constants.game.getName())
                                        + " " + guild.getName() + " " + Translator.getLabel(lg, "server.request.2")
                                        + " " + query.getServer().getName() + "."))
                                .subscribe();
                    } else
                        query.getExceptions()
                                .forEach(exception -> exception.throwException(message, this, lg, query.getServersFound()));
                } else {
                    ServerUtils.ServerQuery query = ServerUtils.getServerDofusFromName(serverName.replace("-channel", "").trim(), lg);
                    ChannelServer channelServer = ChannelServer.getChannelServers().get(channel.getId().asLong());

                    if (query.hasSucceed()){
                        if (channelServer != null){
                            if (channelServer.getServer().getName().equals(query.getServer().getName())){
                                channelServer.removeToDatabase();
                                channel.createMessage(((GuildMessageChannel) channel).getName()
                                        + " " + Translator.getLabel(lg, "server.request.4")
                                        .replace("{server}", guild.getServerDofus().toString()))
                                        .subscribe();
                            }
                            else {
                                channelServer.setServer(query.getServer());
                                message.getChannel().flatMap(salon -> salon
                                        .createMessage(((GuildMessageChannel) channel).getName() + " " + Translator
                                                .getLabel(lg, "server.request.4").replace("{server}", query.getServer().toString())))
                                        .subscribe();
                            }
                        }
                        else {
                            final ChannelServer CHAN = new ChannelServer(query.getServer(), channel.getId().asLong());
                            CHAN.addToDatabase();
                            message.getChannel().flatMap(salon -> salon
                                    .createMessage(((GuildMessageChannel) channel).getName() + " " + Translator
                                            .getLabel(lg, "server.request.4").replace("{server}", query.getServer().toString())))
                                    .subscribe();
                        }
                    } else
                        query.getExceptions()
                                .forEach(exception -> exception.throwException(message, this, lg, query.getServersFound()));
                }
            } else
                BasicDiscordException.NO_ENOUGH_RIGHTS.throwException(message, this, lg);
        }
        else {
            String text = "**" + guild.getName() + "** "
                    + (guild.getServerDofus() != null ?
                        Translator.getLabel(lg, "server.request.4").replace("{server}", guild.getServerDofus().getName()) :
                        Translator.getLabel(lg, "server.request.5"))
                            .replace("{game}", Constants.game.getName());

            ChannelServer channelServer = ChannelServer.getChannelServers().get(channel.getId().asLong());
            if (channelServer != null)
                text += "\n" + Translator.getLabel(lg, "server.request.6")
                        .replace("{channel}", ((GuildMessageChannel) channel).getName())
                        .replace("{server}", channelServer.getServer().getName());

            channel.createMessage(text).subscribe();
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
                + "\n";
    }

    private String getServersList(Language lg) {
        final List<String> SERVERS = ServerDofus.getServersDofus().stream()
                .filter(server -> server.getGame() == Game.DOFUS)
                .map(server -> server.getName())
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
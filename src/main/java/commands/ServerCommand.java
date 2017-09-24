package commands;

import data.Guild;
import data.ServerDofus;
import data.User;
import discord.Message;
import exceptions.NotEnoughRightsDiscordException;
import exceptions.ServerNotFoundDiscordException;
import exceptions.TooMuchServersDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public class ServerCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(ServerCommand.class);

    public ServerCommand(){
        super("server","(\\s+.+)?");
        setUsableInMP(false);
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            User author = User.getUsers().get(message.getGuild().getStringID()).get(message.getAuthor().getStringID());
            Guild guild = Guild.getGuilds().get(message.getGuild().getStringID());
            Matcher m = getMatcher(message);
            m.find();
            if (m.group(1) != null)
                if (author.getRights() >= User.RIGHT_MODERATOR) {
                    String serverName = m.group(1).replaceAll("^\\s+", "").toLowerCase();

                    if (! serverName.equals("-reset")) {
                        List<ServerDofus> result = new ArrayList<>();
                        for (ServerDofus server : ServerDofus.getServersDofus())
                            if (server.getName().toLowerCase().startsWith(serverName))
                                result.add(server);

                        if (result.size() == 1) {
                            if (guild.getServerDofus() != null && guild.getServerDofus() != result.get(0))
                                guild.resetPortals();
                            guild.setServer(result.get(0));
                            guild.mergePortals(result.get(0).getSweetPortals());
                            Message.sendText(message.getChannel(), "Le serveur dofus de " + guild.getName()
                                    + " est désormais " + result.get(0).getName() + ".");
                        } else if (result.isEmpty())
                            new ServerNotFoundDiscordException().throwException(message, this);
                        else
                            new TooMuchServersDiscordException().throwException(message, this, result);
                    }
                    else {
                        guild.setServer(null);
                        Message.sendText(message.getChannel(), guild.getName()
                                + " n'est désormais plus rattachée à un serveur Dofus.");
                    }
                }
                else
                    new NotEnoughRightsDiscordException().throwException(message, this);
            else {
                if (guild.getServerDofus() != null)
                    Message.sendText(message.getChannel(), guild.getName() + " correspond au serveur "
                        + guild.getServerDofus().getName() + ".");
                else
                    Message.sendText(message.getChannel(), guild.getName()
                            + " ne correspond à aucun serveur Dofus.");
            }
        }
        return false;
    }

    @Override
    public String help(String prefixe) {
        return "**" + prefixe + name + "** permet de déterminer à quel serveur Dofus correspond ce serveur Discord.";
    }

    @Override
    public String helpDetailed(String prefixe) {
        return help(prefixe)
                + "\n" + prefixe + "`"  + name + "` : affiche le serveur Dofus correspondant au serveur Discord."
                + "\n" + prefixe + "`"  + name + " `*`server`* : permet de déterminer à quel serveur Dofus correspond ce serveur Discord."
                + "\n" + prefixe + "`"  + name + " `*`-reset`* : permet de se détacher d'un quelconque serveur Dofus.\n";
    }
}
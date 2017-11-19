package commands;

import data.Guild;
import data.ServerDofus;
import data.User;
import enums.Language;
import util.Message;
import exceptions.NotEnoughRightsDiscordException;
import exceptions.ServerNotFoundDiscordException;
import exceptions.TooMuchServersDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

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
            User author = User.getUser(message.getGuild(), message.getAuthor());
            Guild guild = Guild.getGuild(message.getGuild());
            Language lg = Translator.getLanguageFrom(message.getChannel());
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
                            Message.sendText(message.getChannel(), Translator.getLabel(lg, "server.request.1")
                                    + " " + guild.getName() + " " + Translator.getLabel(lg, "server.request.2")
                                    + " " + result.get(0).getName() + ".");
                        } else if (result.isEmpty())
                            new ServerNotFoundDiscordException().throwException(message, this);
                        else
                            new TooMuchServersDiscordException().throwException(message, this, result);
                    }
                    else {
                        guild.setServer(null);
                        Message.sendText(message.getChannel(), guild.getName()
                                + " " + Translator.getLabel(lg, "server.request.3"));
                    }
                }
                else
                    new NotEnoughRightsDiscordException().throwException(message, this);
            else {
                if (guild.getServerDofus() != null)
                    Message.sendText(message.getChannel(), guild.getName() + " "
                            + Translator.getLabel(lg, "server.request.4") + " "
                            + guild.getServerDofus().getName() + ".");
                else
                    Message.sendText(message.getChannel(), guild.getName()
                            + " " + Translator.getLabel(lg, "server.request.5"));
            }
        }
        return false;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "server.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + "` : " + Translator.getLabel(lg, "server.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " `*`server`* : " + Translator.getLabel(lg, "server.help.detailed.2")
                + "\n" + prefixe + "`"  + name + " `*`-reset`* : " + Translator.getLabel(lg, "server.help.detailed.3") + "\n";
    }
}
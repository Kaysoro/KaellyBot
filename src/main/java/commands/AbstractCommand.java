package commands;

import data.ClientConfig;
import data.Constants;
import data.Guild;
import exceptions.BadUseCommandDiscordException;
import exceptions.CommandForbiddenDiscordException;
import exceptions.NotUsableInMPDiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public abstract class AbstractCommand implements Command {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);

    protected String name;
    protected String pattern;
    protected boolean isPublic;
    protected boolean isUsableInMP;
    protected boolean isAdmin;

    protected AbstractCommand(String name, String pattern){
        super();
        this.name = name;
        this.pattern = pattern;
        this.isPublic = true;
        this.isUsableInMP = true;
        this.isAdmin = false;
    }

    @Override
    public boolean request(IMessage message) {
        ClientConfig.setSentryContext(message.getGuild(), message.getAuthor(), message.getChannel(), message);
        Guild guild = Guild.getGuilds().get(message.getGuild().getStringID());
        Matcher m = getMatcher(message);
        boolean isFound = m.find();

        // Dans le cas où l'auteur est super-admin et que la commande est correcte, on accès à la demande.
        if (message.getAuthor().getStringID().equals(Constants.author) && isFound)
            return true;
        else if ((! isPublic() || isAdmin())) // Caché si la fonction est désactivée/réservée aux admin
            return false;

        if (isFound && message.getChannel().isPrivate() && !isUsableInMP()) // En MP et non utilisable en MP ?
            new NotUsableInMPDiscordException().throwException(message, this);
        else if(isFound && isForbidden(guild)) // Désactivé par la guilde ?
            new CommandForbiddenDiscordException().throwException(message, this);
        else if (!isFound && message.getContent().startsWith(getPrefix(message) + getName())) // Mauvaise utilisation ?
            new BadUseCommandDiscordException().throwException(message, this);

        return isFound && ! isForbidden(guild) && (isUsableInMP() || ! message.getChannel().isPrivate());
    }

    @Override
    public boolean isForbidden(Guild g){
        return g.getForbiddenCommands().containsKey(getName());
    }

    @Override
    public Matcher getMatcher(IMessage message){
        String prefixe = getPrefix(message);
        return Pattern.compile("^" + Pattern.quote(prefixe) + name + pattern + "$").matcher(message.getContent());
    }

    @Override
    public String getPrefix(IMessage message){
        String prefix = Constants.prefixCommand;
        if (message.getGuild() != null)
            prefix = Guild.getGuilds().get(message.getGuild().getStringID()).getPrefixe();
        return prefix;
    }

    protected String getPrefixMdEscaped(IMessage message){
        String prefix = Constants.prefixCommand;
        if (message.getGuild() != null)
            prefix = Guild.getGuilds().get(message.getGuild().getStringID()).getPrefixe();
        prefix = prefix.replaceAll("\\*", "\\\\*") // Italic & Bold
                .replaceAll("_", "\\_")          // Underline
                .replaceAll("~", "\\~")          //Strike
                .replaceAll("\\`", "\\\\`");         //Code
        return prefix;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public void setPublic(boolean isPublic){
        this.isPublic = isPublic;
    }

    @Override
    public boolean isPublic(){ return isPublic; }

    @Override
    public boolean isUsableInMP(){ return isUsableInMP; }

    @Override
    public void setUsableInMP(boolean isUsableInMP) {
        this.isUsableInMP = isUsableInMP;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}

package commands.model;

import data.Constants;
import data.Guild;
import enums.Language;
import exceptions.BadUseCommandDiscordException;
import exceptions.BasicDiscordException;
import exceptions.DiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stats.CommandStatistics;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import util.ClientConfig;
import util.Message;
import util.Reporter;
import util.Translator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public abstract class AbstractCommand implements Command {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);

    protected String name;
    protected String pattern;
    protected DiscordException badUse;
    private boolean isPublic;
    private boolean isUsableInMP;
    private boolean isAdmin;
    private boolean isHidden;


    protected AbstractCommand(String name, String pattern){
        super();
        this.name = name;
        this.pattern = pattern;
        this.isPublic = true;
        this.isUsableInMP = true;
        this.isAdmin = false;
        this.isHidden = false;
        badUse = new BadUseCommandDiscordException();
    }

    @Override
    public final void request(IMessage message) {
        try {
            Language lg = Translator.getLanguageFrom(message.getChannel());
            Matcher m = getMatcher(message);
            boolean isFound = m.find();

            // Caché si la fonction est désactivée/réservée aux admin et que l'auteur n'est pas super-admin
            if ((!isPublic() || isAdmin()) && message.getAuthor().getLongID() != Constants.authorId)
                return;

            // S'il s'agit d'une demande d'aide...
            if (message.getContent().matches(Pattern.quote(getPrefix(message)) + getName() + "\\s+help")){
                Message.sendText(message.getChannel(), helpDetailed(lg, getPrefix(message)));
                return;
            }

            // La commande est trouvée
            if (isFound) {
                // Mais n'est pas utilisable en MP
                if (!isUsableInMP() && message.getChannel().isPrivate()) {
                    BasicDiscordException.NOT_USABLE_IN_MP.throwException(message, this, lg);
                    return;
                }
                // Mais est désactivée par la guilde
                else if (!message.getChannel().isPrivate() && message.getAuthor().getLongID() != Constants.authorId
                        && isForbidden(Guild.getGuild(message.getGuild()))) {
                    BasicDiscordException.COMMAND_FORBIDDEN.throwException(message, this, lg);
                    return;
                }
            }
            // Mais est mal utilisée
            else if (message.getContent().startsWith(getPrefix(message) + getName())) {
                badUse.throwException(message, this, lg);
                return;
            }
            if (isFound) {
                if (! isAdmin())
                    CommandStatistics.addStatsToDatabase(this);
                request(message, m, lg);
            }
        } catch(Exception e){
            Reporter.report(e, message.getGuild(), message.getChannel(), message.getAuthor(), message);
            LOG.error("request", e);
        }
    }

    /**
     * @param message Message from the event
     * @param m Matcher that permit to fetch data
     * @param lg Language of the channel (FR, EN, ES..)
     */
    protected abstract void request(IMessage message, Matcher m, Language lg);

    @Override
    public boolean isForbidden(Guild g){
        return g.getForbiddenCommands().containsKey(getName());
    }

    @Override
    public Matcher getMatcher(IMessage message){
        String prefixe = getPrefix(message);
        return Pattern.compile("^" + Pattern.quote(prefixe) + name + pattern + "$").matcher(message.getContent());
    }

    public static String getPrefix(IMessage message){
        String prefix = "";
        if (! message.getChannel().isPrivate())
            prefix = Guild.getGuild(message.getGuild()).getPrefix();
        return prefix;
    }

    protected String getPrefixMdEscaped(IMessage message){
        if (! message.getChannel().isPrivate())
            return Guild.getGuild(message.getGuild()).getPrefix()
                .replaceAll("\\*", "\\\\*")      // Italic & Bold
                .replaceAll("_", "\\_")          // Underline
                .replaceAll("~", "\\~")          // Strike
                .replaceAll("\\`", "\\\\`");     // Code
        return "";
    }

    /**
     * @param message message d'origine
     * @return true si les permissions sont suffisantes, false le cas échéant
     */
    protected boolean isChannelHasExternalEmojisPermission(IMessage message){
        return message.getChannel().isPrivate() ||
                message.getChannel().getModifiedPermissions(ClientConfig.DISCORD().getOurUser())
                        .contains(Permissions.USE_EXTERNAL_EMOJIS)
                && ClientConfig.DISCORD().getOurUser().getPermissionsForGuild(message.getGuild())
                        .contains(Permissions.USE_EXTERNAL_EMOJIS);
    }

    /**
     * Retourne true si l'utilisateur a les droits nécessaires, false le cas échéant
     * @param message Message reçu
     * @return true si l'utilisateur a les droits nécessaires, false le cas échéant
     */
    protected boolean isUserHasEnoughRights(IMessage message){
        return ! message.getChannel().isPrivate() && (message.getAuthor().getLongID() == Constants.authorId
                || message.getAuthor().getPermissionsForGuild(message.getGuild()).contains(Permissions.MANAGE_SERVER)
                || message.getChannel().getModifiedPermissions(message.getAuthor()).contains(Permissions.MANAGE_SERVER));
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

    @Override
    public boolean isHidden() {
        return isHidden;
    }

    @Override
    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }
}

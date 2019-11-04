package commands.model;

import data.Constants;
import data.Guild;
import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.PermissionSet;
import discord4j.core.object.util.Snowflake;
import enums.Language;
import exceptions.BadUseCommandDiscordException;
import exceptions.BasicDiscordException;
import exceptions.DiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stats.CommandStatistics;
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
    public final void request(Message message) {
        Language lg = Translator.getLanguageFrom(message.getChannel().block());
        try {
            Matcher m = getMatcher(message);
            boolean isFound = m.find();

            // Caché si la fonction est désactivée/réservée aux admin et que l'auteur n'est pas super-admin
            if ((!isPublic() || isAdmin()) && message.getAuthor()
                    .map(user -> user.getId().asLong() != Constants.authorId).orElse(false))
                return;

            // S'il s'agit d'une demande d'aide...
            if (message.getContent().map(content -> content
                    .matches(Pattern.quote(getPrefix(message)) + getName() + "\\s+help")).orElse(false)){
                message.getChannel().flatMap(chan -> chan
                        .createMessage(helpDetailed(lg, getPrefix(message)))).subscribe();
                return;
            }

            // La commande est trouvée
            if (isFound) {
                // Mais n'est pas utilisable en MP
                MessageChannel channel = message.getChannel().block();
                if (!isUsableInMP() && channel instanceof PrivateChannel) {
                    BasicDiscordException.NOT_USABLE_IN_MP.throwException(message, this, lg);
                    return;
                }
                // Mais est désactivée par la guilde
                else if (!(channel instanceof PrivateChannel) && message.getAuthor()
                        .map(user -> user.getId().asLong() != Constants.authorId).orElse(false)
                        && isForbidden(Guild.getGuild(message.getGuild().block()))) {
                    BasicDiscordException.COMMAND_FORBIDDEN.throwException(message, this, lg);
                    return;
                }
            }
            // Mais est mal utilisée
            else if (message.getContent().map(content -> content
                    .startsWith(getPrefix(message) + getName())).orElse(false)) {
                badUse.throwException(message, this, lg);
                return;
            }
            if (isFound) {
                if (! isAdmin())
                    CommandStatistics.addStatsToDatabase(this);
                request(message, m, lg);
            }
        } catch(Exception e){
            BasicDiscordException.UNKNOWN_ERROR.throwException(message, this, lg);
            Reporter.report(e);
            LOG.error("request", e);
        }
    }

    /**
     * @param message Message from the event
     * @param m Matcher that permit to fetch data
     * @param lg Language of the channel (FR, EN, ES..)
     */
    protected abstract void request(Message message, Matcher m, Language lg);

    @Override
    public boolean isForbidden(Guild g){
        return g.getForbiddenCommands().containsKey(getName());
    }

    @Override
    public Matcher getMatcher(Message message){
        String prefixe = getPrefix(message);
        return Pattern.compile("^" + Pattern.quote(prefixe) + name + pattern + "$")
                .matcher(message.getContent().orElse(""));
    }

    public static String getPrefix(Message message){
        String prefix = "";
        if (message.getChannel().block() instanceof GuildMessageChannel)
            prefix = Guild.getGuild(message.getGuild().block()).getPrefix();
        return prefix;
    }

    protected String getPrefixMdEscaped(Message message){
        if (!(message.getChannel().block() instanceof PrivateChannel))
            return Guild.getGuild(message.getGuild().block()).getPrefix()
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
    protected boolean isChannelHasExternalEmojisPermission(Message message){
        return message.getChannel().blockOptional().filter(messageChannel -> messageChannel instanceof PrivateChannel ||
                ((TextChannel) messageChannel).getEffectivePermissions(message.getClient().getSelfId()
                        .orElse(Snowflake.of(0L))).blockOptional().orElse(PermissionSet.none())
                        .contains(Permission.USE_EXTERNAL_EMOJIS)).isPresent();
    }

    /**
     * Retourne true si l'utilisateur a les droits nécessaires, false le cas échéant
     * @param message Message reçu
     * @return true si l'utilisateur a les droits nécessaires, false le cas échéant
     */
    protected boolean isUserHasEnoughRights(Message message){
        return message.getChannel().blockOptional()
                .filter(messageChannel -> !(messageChannel instanceof PrivateChannel) && (message.getAuthor()
                        .map(user -> user.getId().asLong() == Constants.authorId).orElse(false)
                || ((TextChannel) messageChannel).getEffectivePermissions(message.getAuthor()
                        .map(User::getId).orElse(Snowflake.of(0L))).blockOptional().orElse(PermissionSet.none())
                .contains(Permission.MANAGE_GUILD))).isPresent();
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

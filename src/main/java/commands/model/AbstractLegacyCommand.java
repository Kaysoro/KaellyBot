package commands.model;

import data.Constants;
import data.Guild;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Translator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public abstract class AbstractLegacyCommand implements LegacyCommand {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractLegacyCommand.class);

    protected String name;
    protected String pattern;
    private boolean isPublic;
    private boolean isUsableInMP;
    private boolean isAdmin;
    private boolean isHidden;


    protected AbstractLegacyCommand(String name, String pattern){
        super();
        this.name = name;
        this.pattern = pattern;
        this.isPublic = true;
        this.isUsableInMP = true;
        this.isAdmin = false;
        this.isHidden = false;
    }

    @Override
    public final void request(MessageCreateEvent event, Message message) {
        Language lg = Translator.getLanguageFrom(message.getChannel().block());
        try {
            Matcher m = getMatcher(message);
            boolean isFound = m.find();

            // Caché si la fonction est désactivée/réservée aux admin et que l'auteur n'est pas super-admin
            if ((!isPublic() || isAdmin()) && message.getAuthor()
                    .map(user -> user.getId().asLong() != Constants.authorId).orElse(false))
                return;

            // S'il s'agit d'une demande d'aide...
            if (message.getContent().matches(Pattern.quote(getPrefix(message)) + getName() + "\\s+help")){
                message.getChannel().flatMap(chan -> chan
                        .createMessage(helpDetailed(lg, getPrefix(message)))).subscribe();
                return;
            }

            // La commande est trouvée
            if (isFound)
                request(event, message, m, lg);
        } catch(Exception e){
            LOG.error("request", e);
        }
    }

    /**
     * @param message Message from the event
     * @param m Matcher that permit to fetch data
     * @param lg Language of the channel (FR, EN, ES..)
     */
    protected abstract void request(MessageCreateEvent event, Message message, Matcher m, Language lg);

    @Override
    public boolean isForbidden(Guild g){
        return false;
    }

    @Override
    public Matcher getMatcher(Message message){
        String prefixe = getPrefix(message);
        return Pattern.compile("^" + Pattern.quote(prefixe) + name + pattern + "$")
                .matcher(message.getContent());
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
                ((TextChannel) messageChannel).getEffectivePermissions(message.getClient().getSelfId())
                        .blockOptional().orElse(PermissionSet.none())
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
                || ((GuildMessageChannel) messageChannel).getEffectivePermissions(message.getAuthor()
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

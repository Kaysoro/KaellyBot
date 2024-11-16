
package data;

import enums.Language;

/**
 * Created by steve on 28/07/2016.
 */
public class Constants {

    /**
     * Application name
     */
    public static final String name = "Kaelly";

    /**
     * Application version
     */
    public static final String version = "1.9.9";

    /**
     * Author id
     */
    public static final long authorId = 162842827183751169L;

    /**
     * Author name
     */
    public static final String authorName = "Kaysoro#8327";

    /**
     * Author avatar
     */
    public static final String authorAvatar = "https://avatars0.githubusercontent.com/u/5544670?s=460&v=4";

    /**
     * URL for Kaelly twitter account
     */
    public static final String twitterAccount = "https://twitter.com/KaellyBot";

    /**
     * URL for github KaellyBot repository
     */
    public static final String git = "https://github.com/Kaysoro/KaellyBot";

    /**
     * Official link invite
     */
    public static final String invite = "https://discordapp.com/oauth2/authorize?&client_id=202916641414184960&scope=bot";

    /**
     * Official paypal link
     */
    public static final String paypal = "https://paypal.me/kaysoro";

    /**
     * Database name
     */
    public static final String database = "bdd.sqlite";

    /**
     * Path to the database (can be left empty)
     */
    public static final String database_path = "";

    /**
     * prefix used for command call.
     * WARN : it is injected into regex expression.
     * If you use special characters as '$', don't forget to prefix it with '\\' like this : "\\$"
     */
    public static final String prefixCommand = "!";

    public static final Language defaultLanguage = Language.FR;

    /**
     * Discord invite link
     */
    public static final String discordInvite = "https://discord.gg/VsrbrYC";
}
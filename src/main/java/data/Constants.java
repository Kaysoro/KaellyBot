package data;

import enums.Language;

/**
 * Created by steve on 28/07/2016.
 */
public class Constants {

    /**
     * Application name
     */
    public final static String name = "Kaelly";

    /**
     * Application version
     */
    public final static String version = "1.5.1";

    /**
     * Changelog
     */
    public final static String changelog = "https://i.imgur.com/IcH7gqc.png"; //https://i.imgur.com/9RzHtLP.jpg

    /**
     * Author id
     */
    public final static long authorId = 162842827183751169L;

    /**
     * Author name
     */
    public final static String authorName = "Kaysoro#8327";

    /**
     * Author avatar
     */
    public final static String authorAvatar = "https://avatars0.githubusercontent.com/u/5544670?s=460&v=4";

    /**
     * URL for Kaelly twitter account
     */
    public final static String twitterAccount = "https://twitter.com/KaellyBot";

    /**
     * URL for github KaellyBot repository
     */
    public final static String git = "https://github.com/Kaysoro/KaellyBot";

    /**
     * Official link invite
     */
    public final static String invite = "https://discordapp.com/oauth2/authorize?&client_id=202916641414184960&scope=bot";

    /**
     * Official paypal link
     */
    public final static String paypal = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=89WTL4LXRZK98";

    /**
     * Database name
     */
    public final static String database = "bdd.sqlite";

    /**
     * prefix used for command call.
     * WARN : it is injected into regex expression.
     * If you use special characters as '$', don't forget to prefix it with '\\' like this : "\\$"
     */
    public final static String prefixCommand = "!";

    public final static Language defaultLanguage = Language.FR;
    /**
     * Game desserved
     */
    public final static String game = "Dofus";

    /**
     * Official Ankama Game Logo
     */
    public final static String officialLogo = "https://s.ankama.com/www/static.ankama.com/g/modules/masterpage/block/header/navbar/dofus/logo.png";

    /**
     * Tutorial URL
     */
    public final static String dofusPourLesNoobURL = "http://www.dofuspourlesnoobs.com";

    /**
     * Tutorial Search URL
     */
    public final static String dofusPourLesNoobSearch = "/apps/search";

    /**
     * Portal URL
     */
    public final static String sweetPortals = "http://www.sweet.ovh/portails/";

    public final static String turnamentMapImg = "https://dofus-tournaments.fr/_default/src/img/maps/A{number}.jpg";

    /**
     * Twitter Icon from Wikipedia
     */
    public final static String twitterIcon = "https://upload.wikimedia.org/wikipedia/fr/thumb/c/c8/Twitter_Bird.svg/langfr-20px-Twitter_Bird.svg.png";

    /**
     * RSS Icon from Wikipedia
     */
    public final static String rssIcon = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Feed-icon.svg/20px-Feed-icon.svg.png";

    /**
     * Character limit for nickname discord
     */
    public final static int nicknameLimit = 32;

    /**
     * Character limit for prefixe discord
     */
    public final static int prefixeLimit = 3;

    /**
     * User or channel dedicated to receive info logs.
     */
    public final static long chanReportID = 321197720629149698L;

    /**
     * User or channel dedicated to receive error logs.
     */
    public final static long chanErrorID = 358201712600678400L;

    /**
     * Official changelog
     */
    public final static long newsChan = 330475075381886976L;

    /**
     * Discord invite link
     */
    public final static String discordInvite = "https://discord.gg/VsrbrYC";
}
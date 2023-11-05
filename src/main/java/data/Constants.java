
package data;

import enums.Game;
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
    public static final String version = "1.9.1";

    /**
     * Changelog
     */
    public static final String changelog = "https://raw.githubusercontent.com/KaellyBot/Kaelly-dashboard/master/public/img/kaellyFull.png";

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
     * Path to the folder containing sounds (can be left empty)
     */
    public static final String sound_path = "";

    /**
     * prefix used for command call.
     * WARN : it is injected into regex expression.
     * If you use special characters as '$', don't forget to prefix it with '\\' like this : "\\$"
     */
    public static final String prefixCommand = "!";

    public static final Language defaultLanguage = Language.FR;

    /**
     * Game desserved
     */
    public static final Game game = Game.DOFUS;

    /**
     * Official Ankama Game Logo
     */
    public static final String officialLogo = "https://s.ankama.com/www/static.ankama.com/g/modules/masterpage/block/header/navbar/dofus/logo.png";

    /**
     * Tutorial URL
     */
    public static final String dofusPourLesNoobURL = "http://www.dofuspourlesnoobs.com";

    /**
     * Tutorial Search URL
     */
    public static final String dofusPourLesNoobSearch = "/apps/search";

    /**
     * DofusRoom build URL
     */
    public static final String dofusRoomBuildUrl = "https://www.dofusroom.com/buildroom/build/show/";

    public static final String turnamentMapImg = "https://dofus-tournaments.fr/_default/src/img/maps/A{number}.jpg";

    /**
     * Twitter Icon from Wikipedia
     */
    public static final String twitterIcon = "https://abs.twimg.com/favicons/twitter.2.ico";

    /**
     * RSS Icon from Wikipedia
     */
    public static final String rssIcon = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Feed-icon.svg/20px-Feed-icon.svg.png";

    /**
     * Character limit for nickname discord
     */
    public static final int nicknameLimit = 32;

    /**
     * Character limit for prefixe discord
     */
    public static final int prefixeLimit = 3;

    /**
     * User or channel dedicated to receive info logs.
     */
    public static final long chanReportID = 321197720629149698L;

    /**
     * User or channel dedicated to receive error logs.
     */
    public static final long chanErrorID = 358201712600678400L;

    /**
     * Official changelog
     */
    public static final long newsChan = 330475075381886976L;

    /**
     * Almanax API URL
     */
    public static final String almanaxURL = "https://api.dofusdu.de/{game}/{language}/almanax/{date}";

    /**
     * Almanax Redis cache time to live for each cached day
     */
    public static final int almanaxCacheHoursTTL = 3;

    /**
     * Discord invite link
     */
    public static final String discordInvite = "https://discord.gg/VsrbrYC";
}
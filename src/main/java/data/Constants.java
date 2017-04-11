package data;

/**
 * Created by steve on 28/07/2016.
 */
public class Constants {

    /**
     * Application name
     */
    public final static String name = "Kaelly";

    /**
     * Author
     */
    public final static String author = "162842827183751169";

    /**
     * URL for github KaellyBot repository
     */
    public final static String git = "https://github.com/Kaysoro/KaellyBot";

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

    /**
     * Official Dofus URL for almanax
     */
    public final static String almanaxURL = "http://www.krosmoz.com/fr/almanax/";

    /**
     * Official Dofus URL for news feed
     */
    public final static String dofusFeedURL = "http://www.dofus.com/fr/rss/news.xml";

    /**
     * Official Dofus account on Twitter
     */
    public final static String dofusTwitter = "@DOFUSfr";

    /**
     * Character limit for nickname discord
     */
    public final static int nicknameLimit = 32;

    /**
     * Youtube URL for music, without the code
     */
    public final static String youtubeURL = "https://www.youtube.com/watch?v=";

    /**
     * User or channel dedicated to receive error logs.
     */
    public final static String chanReportID = "162842827183751169";
}

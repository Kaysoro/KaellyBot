package data;

import commands.HelpCommand;

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
    public final static String version = "1.4.1";

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
     * Game desserved
     */
    public final static String game = "Dofus";

    /**
     * Official Ankama Game URL
     */
    public final static String officialURL = "https://www.dofus.com";

    /**
     * Official Dofus URL for almanax
     */
    public final static String almanaxURL = "http://www.krosmoz.com/fr/almanax/";

    /**
     * Official Dofus URL for news feed
     */
    public final static String feedURL = "/fr/rss/news.xml";

    /**
     * Official Dofus URL for character page
     */
    public final static String characterPageURL = "/fr/mmorpg/communaute/annuaires/pages-persos";

    /**
     * Official Dofus URL for Weapon page
     */
    public final static String weaponPageURL = "/fr/mmorpg/encyclopedie/armes";

    /**
     * Official Dofus URL for Equipement page
     */
    public final static String equipementPageURL = "/fr/mmorpg/encyclopedie/equipements";

    /**
     * Official Dofus URL for Pets page
     */
    public final static String petPageURL = "/fr/mmorpg/encyclopedie/familiers";

    /**
     * Official Dofus URL for Montures page
     */
    public final static String monturePageURL = "/fr/mmorpg/encyclopedie/montures";

    /**
     * Official Dofus URL for Monster page
     */

    public final static String monsterPageURL = "/fr/mmorpg/encyclopedie/monstres";

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

    /**
     * Twitter Icon from Wikipedia
     */
    public final static String twitterIcon = "https://upload.wikimedia.org/wikipedia/fr/thumb/c/c8/Twitter_Bird.svg/langfr-20px-Twitter_Bird.svg.png";
    /**
     * Official Dofus account on Twitter
     */
    public final static long dofusTwitter = 72272795L;

    /**
     * Character limit for nickname discord
     */
    public final static int nicknameLimit = 32;

    /**
     * Character limit for prefixe discord
     */
    public final static int prefixeLimit = 3;

    /**
     * Youtube URL for music, without the code
     */
    public final static String youtubeURL = "https://www.youtube.com/watch?v=";

    /**
     * User or channel dedicated to receive error logs.
     */
    public final static long chanReportID = 321197720629149698L;

    /**
     * Message sent when joining a new guild
     */
    public final static String msgJoinGuild = "Salut et merci pour l'invitation ! Moi c'est " + name
            + ", je suis un robot dédié au jeu " + game + "; j'ai quelques commandes dans ma manche qui "
            + "peuvent t'intéresser et qui me permettent d'afficher des items du jeu, des pages personnelles de joueurs, "
            + "gérer un annuaire de vos métiers ou encore des positions de portails !"
            + "\nTu veux en savoir plus ? Ecris `" + prefixCommand + new HelpCommand().getName()
            + "` pour avoir la liste complète ! :blush:"
            + "\n\n{0}, en tant qu'administrateur de {1}, tu possèdes un ensemble de droits plus poussés "
            + "et tu peux par exemple autoriser ou non dans un des salons la réception des tweets émis par le "
            + "compte officiel " + game + "."
            + "\n\nC'est pas génial tout ça ? J'espère que nous allons bien nous amuser tous ensemble ! :smile:";

    /**
     * Discord invite link
     */
    public final static String discordInvite = "https://discord.gg/VsrbrYC";

    /**
     * Message for about Kaelly command
     */
    public final static String about = name + " est destinée à fournir des commandes utiles à la "
            + "communauté de dofus !\nL'intégralité de son code est libre d'accès et est disponible ici : "
            + git + "\nSi vous avez des questions, des suggestions ou que vous souhaitez juste "
            + "passer un coucou, rejoignez le discord de " + name
            + " : " + discordInvite + "\nPromis, on ne mord pas ! :yum:";
}

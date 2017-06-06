package data;

import sx.blah.discord.handle.obj.Permissions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve on 06/06/2017.
 */
public class Translator {

    private static Map<Permissions, String> frenchPermissions;

    public static String getFrenchNameFor(Permissions p){
        if (frenchPermissions == null){
            frenchPermissions = new HashMap<>();
            frenchPermissions.put(Permissions.CREATE_INVITE, "Créer une invitation");
            frenchPermissions.put(Permissions.KICK, "Expulser les membres");
            frenchPermissions.put(Permissions.BAN, "Bannir les membres");
            frenchPermissions.put(Permissions.ADMINISTRATOR, "Administrateur");
            frenchPermissions.put(Permissions.MANAGE_CHANNELS, "Gérer les salons");
            frenchPermissions.put(Permissions.MANAGE_CHANNEL, "Gérer le salon");
            frenchPermissions.put(Permissions.MANAGE_SERVER, "Gérer le serveur");
            frenchPermissions.put(Permissions.ADD_REACTIONS, "Ajouter une réaction");
            frenchPermissions.put(Permissions.VIEW_AUDIT_LOG, "Voir le rapport d'audit");
            frenchPermissions.put(Permissions.READ_MESSAGES, "Lire les messages");
            frenchPermissions.put(Permissions.SEND_MESSAGES, "Envoyer des messages");
            frenchPermissions.put(Permissions.SEND_TTS_MESSAGES, "Envoyer des messages TTS");
            frenchPermissions.put(Permissions.MANAGE_MESSAGES, "Gérer les messages");
            frenchPermissions.put(Permissions.EMBED_LINKS, "Intégrer des liens");
            frenchPermissions.put(Permissions.ATTACH_FILES, "Attacher des fichiers");
            frenchPermissions.put(Permissions.READ_MESSAGE_HISTORY, "Voir les anciens messages");
            frenchPermissions.put(Permissions.MENTION_EVERYONE, "Mentionner @Everyone");
            frenchPermissions.put(Permissions.USE_EXTERNAL_EMOJIS, "Utiliser des émojis externes");
            frenchPermissions.put(Permissions.VOICE_CONNECT, "Se connecter à un canal vocal");
            frenchPermissions.put(Permissions.VOICE_SPEAK, "Parler dans un canal vocal");
            frenchPermissions.put(Permissions.VOICE_MUTE_MEMBERS, "Rendre muets");
            frenchPermissions.put(Permissions.VOICE_DEAFEN_MEMBERS, "Rendre sourds");
            frenchPermissions.put(Permissions.VOICE_MOVE_MEMBERS, "Déplacer les membres");
            frenchPermissions.put(Permissions.VOICE_USE_VAD, "Utiliser la détection de voix");
            frenchPermissions.put(Permissions.CHANGE_NICKNAME, "Changer de pseudo");
            frenchPermissions.put(Permissions.MANAGE_NICKNAMES, "Gérer les pseudos");
            frenchPermissions.put(Permissions.MANAGE_ROLES, "Gérer les rôles");
            frenchPermissions.put(Permissions.MANAGE_PERMISSIONS, "Gérer les permissions");
            frenchPermissions.put(Permissions.MANAGE_WEBHOOKS, "Gérer les webhooks");
            frenchPermissions.put(Permissions.MANAGE_EMOJIS, "Gérer les émojis");
        }
        return frenchPermissions.get(p);
    }
}

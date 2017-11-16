package util;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import data.Constants;
import enums.Language;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 06/06/2017.
 */
public class Translator {

    private static final int MAX_MESSAGES_READ = 100;
    private static final int MAX_CHARACTER_ACCEPTANCE = 20;
    private static Map<Permissions, String> frenchPermissions;
    private static Map<Language, Properties> labels;
    private static LanguageDetector languageDetector;

    @Deprecated
    public static String getFrenchNameFor(Permissions p){
        //TODO refactoring with labels files
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

    private static LanguageDetector getLanguageDetector(){
        if (languageDetector == null){
            try {
                List<String> languages = new ArrayList<String>();
                for(Language lg : Language.values())
                    languages.add(lg.getAbrev().toLowerCase());

                List<LanguageProfile> languageProfiles = new LanguageProfileReader().read(languages);
                languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                                .withProfiles(languageProfiles).build();
            }
            catch (IOException e) {
                LoggerFactory.getLogger(Translator.class).error("Translator.getLanguageDetector", e);
            }
        }
        return languageDetector;
    }

    private static List<String> getReformatedMessages(IChannel channel){
        List<String> result = new ArrayList<>();

        IMessage[] messages = channel.getMessageHistory(MAX_MESSAGES_READ).asArray();
        for(IMessage message : messages) {
            String content = message.getContent().replaceAll(":\\w+:", "").trim();
            if (content.length() > MAX_CHARACTER_ACCEPTANCE)
                result.add(content);
        }
        return result;
    }

    public static Language getLanguageFrom(String source){
        TextObject textObject = CommonTextObjectFactories.forDetectingOnLargeText().forText(source);
        Optional<LdLocale> lang = getLanguageDetector().detect(textObject);
        if (lang.isPresent())
            for(Language lg : Language.values())
                if(lang.get().getLanguage().equals(lg.getAbrev().toLowerCase()))
                    return lg;
        return null;
    }

    public static Language detectLanguage(IChannel channel){
        Language result = Constants.defaultLanguage;
        Map<Language, Integer> languages = new HashMap<>();
        for(Language lang : Language.values())
            languages.put(lang, 0);
        languages.put(null, 0);
        List<String> sources = getReformatedMessages(channel);
        for (String source : sources){
            Language lg = getLanguageFrom(source);
            languages.put(lg, languages.get(lg) + 1);
        }
        Map.Entry<Language, Integer> better = null;
        for (Map.Entry<Language, Integer> chosenLanguage : languages.entrySet()){
            if (better == null)
                better = chosenLanguage;
            if (better.getValue() < chosenLanguage.getValue())
                better = chosenLanguage;
        }

        if (better.getKey() != null)
            return better.getKey();
        return result;
    }

    public static String getLabel(Language lang, String property){
        if (labels == null){
            labels = new ConcurrentHashMap<>();

            for(Language language : Language.values())
                try {
                    InputStream file = Translator.class.getResourceAsStream("/label_" + language.getAbrev());
                    Properties prop = new Properties();
                    prop.load(new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8)));
                    labels.put(language, prop);
                    file.close();
                } catch (IOException e) {
                    LoggerFactory.getLogger(Translator.class).error("Translator.getLabel", e);
                }
        }

        String value = labels.get(lang).getProperty(property);
        if (value == null || value.trim().isEmpty()) return property;
        return value;
    }
}

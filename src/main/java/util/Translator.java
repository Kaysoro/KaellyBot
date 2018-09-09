package util;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import data.ChannelLanguage;
import data.Constants;
import data.Guild;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by steve on 06/06/2017.
 */
public class Translator {

    private final static Logger LOG = LoggerFactory.getLogger(Translator.class);
    private static final int MAX_MESSAGES_READ = 100;
    private static final int MAX_CHARACTER_ACCEPTANCE = 20;
    private static Map<Language, Properties> labels;
    private static LanguageDetector languageDetector;

    private static LanguageDetector getLanguageDetector(){
        if (languageDetector == null){
            try {
                List<String> languages = new ArrayList<>();
                for(Language lg : Language.values())
                    languages.add(lg.getAbrev().toLowerCase());

                List<LanguageProfile> languageProfiles = new LanguageProfileReader().read(languages);
                languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                                .withProfiles(languageProfiles).build();
            }
            catch (IOException e) {
                LOG.error("Translator.getLanguageDetector", e);
            }
        }
        return languageDetector;
    }

    /**
     * Fournit la langue utilisée dans un salon textuel
     * @param channel Salon textuel
     * @return Langue de la guilde ou du salon si précisé
     */
    public static Language getLanguageFrom(IChannel channel){
        Language result = Constants.defaultLanguage;
        if (! channel.isPrivate()) {
            Guild guild = Guild.getGuild(channel.getGuild());
            result = guild.getLanguage();
            ChannelLanguage channelLanguage = ChannelLanguage.getChannelLanguages().get(channel.getLongID());
            if (channelLanguage != null)
                result = channelLanguage.getLang();
        }
        return result;
    }

    /**
     * Génère une liste de source formatée à partir d'un salon textuel
     * @param channel Salon d'origine
     * @return Liste de message éligibles à une reconnaissance de langue
     */
    private static List<String> getReformatedMessages(IChannel channel){
        List<String> result = new ArrayList<>();

        if (channel != null) {
            try {
                IMessage[] messages = channel.getMessageHistory(MAX_MESSAGES_READ).asArray();
                for (IMessage message : messages) {
                    String content = message.getContent().replaceAll(":\\w+:", "").trim();
                    if (content.length() > MAX_CHARACTER_ACCEPTANCE)
                        result.add(content);
                }
            } catch (Exception e){
                LOG.warn("Impossible to gather data from " + channel.getStringID() + "/" + channel.getName());
            }
        }
        return result;
    }

    /**
     * Détermine une langue à partir d'une source textuelle
     * @param source Source textuelle
     * @return Langue majoritaire détectée au sein de la source
     */
    private static Language getLanguageFrom(String source){
        TextObject textObject = CommonTextObjectFactories.forDetectingOnLargeText().forText(source);
        LdLocale lang = getLanguageDetector().detect(textObject)
                .or(LdLocale.fromString(Constants.defaultLanguage.getAbrev().toLowerCase()));

        for(Language lg : Language.values())
            if(lang.getLanguage().equals(lg.getAbrev().toLowerCase()))
                return lg;
        return Constants.defaultLanguage;
    }

    /**
     * Détecte la langue majoritaire utilisée dans un salon textuel
     * @param channel Salon textuel à étudier
     * @return Langue majoritaire (ou Constants.defaultLanguage si non trouvée)
     */
    public static Language detectLanguage(IChannel channel){
        Language result = Constants.defaultLanguage;
        Map<Language, LanguageOccurrence> languages = new HashMap<>();
        for(Language lang : Language.values())
            languages.put(lang, LanguageOccurrence.of(lang));

        List<String> sources = getReformatedMessages(channel);
        for (String source : sources)
            languages.get(getLanguageFrom(source)).increment();

        int longest = languages.entrySet().stream()
                .map(Map.Entry::getValue)
                .mapToInt(LanguageOccurrence::getOccurrence)
                .max()
                .orElse(-1);

        if (longest > 0){
            List<Language> languagesSelected = languages.entrySet().stream()
                    .map(Map.Entry::getValue)
                    .filter(lo -> lo.getOccurrence() == longest)
                    .map(lo -> lo.getLanguage())
                    .collect(Collectors.toList());
            if (! languagesSelected.contains(result))
                return languagesSelected.get(0);
        }

        return result;
    }

    /**
     * Fournit un libellé dans la langue choisi, pour un code donné
     * @param lang Language du libellé
     * @param property code du libellé
     * @return Libellé correspondant au code, dans la langue choisie
     */
    public synchronized static String getLabel(Language lang, String property){
        if (labels == null){
            labels = new ConcurrentHashMap<>();

            for(Language language : Language.values())
                try(InputStream file = Translator.class.getResourceAsStream("/label_" + language.getAbrev())) {
                    Properties prop = new Properties();
                    prop.load(new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8)));
                    labels.put(language, prop);
                } catch (IOException e) {
                    LOG.error("Translator.getLabel", e);
                }
        }

        String value = labels.get(lang).getProperty(property);
        if (value == null || value.trim().isEmpty())
            if (Constants.defaultLanguage != lang) {
                LOG.warn("Missing label in " + lang.getAbrev() + " : " + property);
                return getLabel(Constants.defaultLanguage, property);
            }
            else
                return property;
        return value;
    }

    /**
     * Classe dédiée à la détection de la langue la plus utilisée au sein d'un salon textuel
     */
    private static class LanguageOccurrence {
        private final static LanguageOccurrence DEFAULT_LANGUAGE = of(Constants.defaultLanguage);
        private final static int DEFAULT_VALUE = 0;
        private Language language;
        private int occurrence;

        private LanguageOccurrence(Language language, int occurrence){
            this.language = language;
            this.occurrence = occurrence;
        }

        private static LanguageOccurrence of(Language language){
            return new LanguageOccurrence(language, DEFAULT_VALUE);
        }

        public Language getLanguage(){
            return language;
        }

        private int getOccurrence(){
            return occurrence;
        }

        private void increment(){
            occurrence++;
        }

        public boolean equals(LanguageOccurrence lgo){
            return language.equals(lgo.language);
        }
    }
}

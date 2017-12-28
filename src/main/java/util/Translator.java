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
import data.ChannelLanguage;
import data.Constants;
import data.Guild;
import enums.Language;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

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
                LoggerFactory.getLogger(Translator.class).error("Translator.getLanguageDetector", e);
            }
        }
        return languageDetector;
    }

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

    private static List<String> getReformatedMessages(IChannel channel){
        List<String> result = new ArrayList<>();

        if (channel != null) {
            IMessage[] messages = channel.getMessageHistory(MAX_MESSAGES_READ).asArray();
            for (IMessage message : messages) {
                String content = message.getContent().replaceAll(":\\w+:", "").trim();
                if (content.length() > MAX_CHARACTER_ACCEPTANCE)
                    result.add(content);
            }
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

        try {
            if (better.getKey() != null)
                return better.getKey();
        } catch(NullPointerException e){
            LoggerFactory.getLogger(Translator.class).warn("Translator.detectLanguage", e);
        }
        return result;
    }

    public synchronized static String getLabel(Language lang, String property){
        if (labels == null){
            labels = new ConcurrentHashMap<>();

            for(Language language : Language.values())
                try(InputStream file = Translator.class.getResourceAsStream("/label_" + language.getAbrev())) {

                    Properties prop = new Properties();
                    prop.load(new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8)));
                    labels.put(language, prop);

                } catch (IOException e) {
                    LoggerFactory.getLogger(Translator.class).error("Translator.getLabel", e);
                }
        }

        String value = labels.get(lang).getProperty(property);
        if (value == null || value.trim().isEmpty())
            if (Constants.defaultLanguage != lang) {
                LoggerFactory.getLogger(Translator.class).warn("Missing label in " + lang.getAbrev() + " : " + property);
                return getLabel(Constants.defaultLanguage, property);
            }
            else
                return property;
        return value;
    }
}

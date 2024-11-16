package util;

import data.ChannelLanguage;
import data.Constants;
import data.Guild;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Created by steve on 06/06/2017.
 */
public class Translator {

    private final static Logger LOG = LoggerFactory.getLogger(Translator.class);
    private static Map<Language, Properties> labels;

    /**
     * Fournit la langue utilisée dans un salon textuel
     * @param channel Salon textuel
     * @return Langue de la guilde ou du salon si précisé
     */
    public static Language getLanguageFrom(MessageChannel channel){
        Language result = Constants.defaultLanguage;
        if (channel instanceof GuildMessageChannel) {

            Guild guild = Guild.getGuild(((GuildMessageChannel) channel).getGuild().block());
            result = guild.getLanguage();
            ChannelLanguage channelLanguage = ChannelLanguage.getChannelLanguages().get(channel.getId().asLong());
            if (channelLanguage != null)
                result = channelLanguage.getLang();
        }
        return result;
    }

    public static Language mapLocale(String locale){
        return Stream.of(Language.values())
                .filter(lg -> lg.getLocales().contains(locale))
                .findFirst()
                .orElse(Constants.defaultLanguage);
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
}

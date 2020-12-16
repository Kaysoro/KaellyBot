package mapper;

import discord4j.core.spec.EmbedCreateSpec;
import enums.Dimension;
import enums.Language;
import org.apache.commons.lang3.time.DateUtils;
import payloads.PortalDto;
import util.Translator;

import java.time.Duration;
import java.time.Instant;

public final class PortalMapper {

    private PortalMapper(){}

    public static void decorateSpec(EmbedCreateSpec spec, PortalDto portal, Language language){
        Dimension dimension = Dimension.valueOfCaseUnsensitive(portal.getDimension(), language);

        spec.setTitle(portal.getDimension())
                .setThumbnail(dimension.getImage());

        if (Boolean.TRUE.equals(portal.getIsAvailable()) && portal.getNearestZaap() != null
                && portal.getPosition() != null && portal.getUtilisation() > 0){
            spec.addField(Translator.getLabel(language, "portal.position"),
                    "**" + portal.getPosition() + "**", true);

            // Utilisation
            spec.addField(Translator.getLabel(language, "portal.utilisation.title"),
                    portal.getUtilisation() + " "
                            + Translator.getLabel(language, "portal.utilisation.desc")
                            + (portal.getUtilisation() > 1 ? "s" : ""), true);

            // Transports
            if (portal.getNearestTransportLimited() != null)
                spec.addField(Translator.getLabel(language, "portal.private_zaap")
                                .replace("{transport}", portal.getNearestTransportLimited().getType()),
                        portal.getNearestTransportLimited().toString(), false);

                spec.addField(Translator.getLabel(language, "portal.zaap"),
                        portal.getNearestZaap().toString(), false);

            spec.setFooter(getDateInformation(portal, language),
                    "https://i.imgur.com/u2PUyt5.png");
        }
        else
            spec.setDescription(Translator.getLabel(language, "portal.unknown"));
    }

    private static String getDateInformation(PortalDto portal, Language language){
        StringBuilder st = new StringBuilder(Translator.getLabel(language, "portal.date.added")).append(" ")
                .append(getLabelTimeAgo(portal.getCreationDate(), language)).append(" ")
                .append(Translator.getLabel(language, "portal.date.by")).append(" ")
                .append(portal.getCreationAuthor().getName());

        if (portal.getLastUpdateDate() != null){
            st.append(" - ").append(Translator.getLabel(language, "portal.date.edited")).append(" ")
                    .append(getLabelTimeAgo(portal.getLastUpdateDate(), language)).append(" ")
                    .append(Translator.getLabel(language, "portal.date.by")).append(" ")
                    .append(portal.getLastAuthorUpdate().getName());
        }

        return st.append(" ").append(Translator.getLabel(language, "portal.date.via"))
                .append(" dofus-portals.fr").toString();
    }

    private static String getLabelTimeAgo(Instant time, Language lg){
        long timeLeft = Math.abs(Duration.between(time, Instant.now()).toMillis());
        if (timeLeft < DateUtils.MILLIS_PER_MINUTE)
            return Translator.getLabel(lg, "portal.date.now");
        else if (timeLeft < DateUtils.MILLIS_PER_HOUR)
            return Translator.getLabel(lg, "portal.date.minutes_ago")
                    .replace("{time}", String.valueOf(timeLeft / DateUtils.MILLIS_PER_MINUTE));
        else
            return Translator.getLabel(lg, "portal.date.hours_ago")
                    .replace("{time}", String.valueOf(timeLeft / DateUtils.MILLIS_PER_HOUR));
    }
}

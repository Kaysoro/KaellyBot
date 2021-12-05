package mapper;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import enums.Language;
import org.apache.commons.lang3.time.DateUtils;
import payloads.PortalDto;
import payloads.PositionDto;
import payloads.TransportDto;
import util.Translator;

import java.time.Duration;
import java.time.Instant;

public final class PortalMapper {

    private PortalMapper(){}

    public static EmbedCreateSpec decorateSpec(PortalDto portal, Language language){
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .title(portal.getDimension().getName())
                .color(Color.of(portal.getDimension().getColor()))
                .thumbnail(portal.getDimension().getImage());

        if (Boolean.TRUE.equals(portal.getIsAvailable()) && portal.getNearestZaap() != null
                && portal.getPosition() != null && portal.getUtilisation() > 0){
            builder.addField(Translator.getLabel(language, "portal.position"),
                    "**" + getPosition(portal.getPosition()) + "**", true);

            // Utilisation
            builder.addField(Translator.getLabel(language, "portal.utilisation.title"),
                    portal.getUtilisation() + " "
                            + Translator.getLabel(language, "portal.utilisation.desc")
                            + (portal.getUtilisation() > 1 ? "s" : ""), true);

            // Transports
            if (portal.getNearestTransportLimited() != null)
                builder.addField(Translator.getLabel(language, "portal.private_zaap")
                                .replace("{transport}", portal.getNearestTransportLimited().getType()),
                        getTransport(portal.getNearestTransportLimited()), false);

            builder.addField(Translator.getLabel(language, "portal.zaap"),
                        getTransport(portal.getNearestZaap()), false);

            builder.footer(getDateInformation(portal, language),
                    "https://i.imgur.com/u2PUyt5.png");
        }
        else
            builder.description(Translator.getLabel(language, "portal.unknown"));
        return builder.build();
    }

    private static String getPosition(PositionDto position){
        return "[" + position.getX() + ", " + position.getY() + "]";
    }

    private static String getTransport(TransportDto transport){
        return transport.getArea() + " (" + transport.getSubArea()
                + ") **[" + transport.getPosition().getX() + ", " + transport.getPosition().getY() + "]**";
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

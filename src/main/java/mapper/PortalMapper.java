package mapper;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import enums.Language;
import org.apache.commons.lang3.time.DateUtils;
import payloads.DimensionDto;
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
                .title(Translator.getLabel(language, "dimension." + portal.getDimension()))
                .color(Color.of(DimensionDto.of(portal.getDimension()).getColor()))
                .thumbnail(DimensionDto.of(portal.getDimension()).getImage());

        if (portal.getPosition() != null && portal.getRemainingUses() > 0){
            builder.addField(Translator.getLabel(language, "portal.position"),
                    "**" + getPosition(portal.getPosition()) + "**", true);

            // Utilisation
            builder.addField(Translator.getLabel(language, "portal.utilisation.title"),
                    portal.getRemainingUses() + " "
                            + Translator.getLabel(language, "portal.utilisation.desc")
                            + (portal.getRemainingUses() > 1 ? "s" : ""), true);

            // Transports
            if (portal.getPosition().getConditionalTransport() != null) {
                String transportType = Translator.getLabel(language, "dp.transport."
                        + portal.getPosition().getConditionalTransport().getType());
                builder.addField(Translator.getLabel(language, "portal.private_zaap")
                                .replace("{transport}", transportType),
                        getTransport(portal.getPosition().getConditionalTransport(), language), false);
            }

            builder.addField(Translator.getLabel(language, "portal.zaap"),
                        getTransport(portal.getPosition().getTransport(), language), false);

            builder.footer(getDateInformation(portal, language),
                    "https://i.imgur.com/j8p3M2D.png");
        }
        else
            builder.description(Translator.getLabel(language, "portal.unknown"));
        return builder.build();
    }

    private static String getPosition(PositionDto position){
        return "[" + position.getX() + ", " + position.getY() + "]";
    }

    private static String getTransport(TransportDto transport, Language language){
        return Translator.getLabel(language, "dp.area." + transport.getArea()) + " ("
                + Translator.getLabel(language, "dp.sub_area." + transport.getSubArea())
                + ") **[" + transport.getX() + ", " + transport.getY() + "]**";
    }

    private static String getDateInformation(PortalDto portal, Language language){
        StringBuilder st = new StringBuilder(Translator.getLabel(language, "portal.date.added")).append(" ")
                .append(getLabelTimeAgo(portal.getCreatedAt(), language)).append(" ")
                .append(Translator.getLabel(language, "portal.date.by")).append(" ")
                .append(portal.getCreatedBy().getName());

        if (portal.getUpdatedAt() != null){
            st.append(" - ").append(Translator.getLabel(language, "portal.date.edited")).append(" ")
                    .append(getLabelTimeAgo(portal.getUpdatedAt(), language)).append(" ")
                    .append(Translator.getLabel(language, "portal.date.by")).append(" ")
                    .append(portal.getUpdatedBy().getName());
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

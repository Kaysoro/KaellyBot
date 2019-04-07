package com.github.kaysoro.kaellybot.portal.mapper;

import com.github.kaysoro.kaellybot.portal.model.dto.PortalDto;
import com.github.kaysoro.kaellybot.portal.model.entity.Portal;

import java.time.Duration;
import java.time.Instant;

public class PortalMapper {

    private final static long PORTAL_LIFETIME_IN_DAYS = 2;

    public static PortalDto map(Portal portal) {
        PortalDto result = new PortalDto()
                .withDimension(portal.getPortalId().getDimension())
                .withAvailable(portal.isAvailable());

        if (isPortalStillFresh(portal)) {
            result.withPosition(PositionMapper.map(portal.getPosition()))
                    .withUtilisation(portal.getUtilisation())
                    .withCreationDate(portal.getCreationDate())
                    .withCreationAuthor(AuthorMapper.map(portal.getCreationAuthor()))
                    .withNearestZaap(TransportMapper.map(portal.getNearestZaap()));

            if (portal.isUpdated())
                result.withLastUpdateDate(portal.getLastUpdateDate())
                        .withLastAuthorUpdate(AuthorMapper.map(portal.getLastAuthorUpdate()));

            if (portal.isTransportLimitedNearest())
                result.withNearestTransportLimited(TransportMapper.map(portal.getNearestTransportLimited()));
        }

        return result;
    }

    private static boolean isPortalStillFresh(Portal portal){
        return portal.isAvailable() &&
                Math.abs(Duration.between(Instant.now(), portal.getCreationDate()).toDays()) < PORTAL_LIFETIME_IN_DAYS;
    }
}
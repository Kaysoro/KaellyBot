package com.github.kaysoro.kaellybot.portal.mapper;

import com.github.kaysoro.kaellybot.portal.model.dto.PortalDto;
import com.github.kaysoro.kaellybot.portal.model.entity.Portal;
import com.github.kaysoro.kaellybot.portal.model.entity.Position;
import com.github.kaysoro.kaellybot.portal.model.entity.Transport;

public class PortalMapper {

    public static PortalDto map(Portal portal){

        // Mock
        return new PortalDto()
                .withServer("MockServer")
                .withDimension("MockDimension")
                .withPosition(PositionMapper.map(Position.of(0, 0)))
                .withNearestZaap(Transport.CITE_D_ASTRUB)
                .withNearestTransportLimited(Transport.CITE_D_ASTRUB)
                .withTransportLimitedNearest(false);
        /**
                .withServer(portal.getServer())
                .withDimension(portal.getDimension())
                .withPosition(PositionMapper.map(portal.getPosition()))
                .withNearestZaap(portal.getnearestZaap())
                .withNearestTransportLimited(portal.getNearestTransportLimited())
                .withTransportLimitedNearest(portal.isTransportLimitedNearest());
         */
    }
}

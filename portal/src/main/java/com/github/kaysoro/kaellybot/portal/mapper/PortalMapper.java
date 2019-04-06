package com.github.kaysoro.kaellybot.portal.mapper;

import com.github.kaysoro.kaellybot.portal.model.dto.PortalDto;
import com.github.kaysoro.kaellybot.portal.model.entity.Portal;

public class PortalMapper {

    public static PortalDto map(Portal portal){
        return new PortalDto()
                .withServer(portal.getServer())
                .withDimension(portal.getDimension())
                .withPosition(PositionMapper.map(portal.getPosition()))
                .withNearestZaap(TransportMapper.map(portal.getnearestZaap()))
                .withNearestTransportLimited(TransportMapper.map(portal.getNearestTransportLimited()))
                .withTransportLimitedNearest(portal.isTransportLimitedNearest());
    }
}
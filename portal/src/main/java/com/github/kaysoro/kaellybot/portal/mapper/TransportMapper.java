package com.github.kaysoro.kaellybot.portal.mapper;

import com.github.kaysoro.kaellybot.portal.model.dto.TransportDto;
import com.github.kaysoro.kaellybot.portal.model.constants.Transport;

public class TransportMapper {

    public static TransportDto map(Transport transport){
        return new TransportDto().withType(transport.getType())
                .withArea(transport.getArea())
                .withSubArea(transport.getSubArea())
                .withPosition(PositionMapper.map(transport.getPosition()))
                .withAvailableUnderConditions(transport.isAvailableUnderConditions());
    }
}

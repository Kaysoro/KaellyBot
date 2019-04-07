package com.github.kaysoro.kaellybot.portal.mapper;

import com.github.kaysoro.kaellybot.portal.model.dto.PositionDto;
import com.github.kaysoro.kaellybot.portal.model.entity.Position;

public class PositionMapper {

    public static PositionDto map(Position position){
        return new PositionDto()
                .withX(position.getX())
                .withY(position.getY());
    }
}

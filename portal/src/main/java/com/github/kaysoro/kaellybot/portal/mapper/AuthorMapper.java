package com.github.kaysoro.kaellybot.portal.mapper;

import com.github.kaysoro.kaellybot.portal.model.dto.AuthorDto;
import com.github.kaysoro.kaellybot.portal.model.entity.Author;

public class AuthorMapper {

    public static AuthorDto map(Author author){
        return new AuthorDto()
                .withName(author.getName())
                .withPlatform(author.getPlatform());
    }
}

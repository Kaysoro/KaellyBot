package com.github.kaysoro.kaellybot.portal.model.dto;

public class AuthorDto {

    private String name;
    private String platform;

    public String getName() {
        return name;
    }

    public AuthorDto withName(String name) {
        this.name = name;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public AuthorDto withPlatform(String platform) {
        this.platform = platform;
        return this;
    }
}

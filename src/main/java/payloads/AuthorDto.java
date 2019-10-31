package payloads;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = AuthorDto.AuthorDtoBuilder.class)
@Builder(builderClassName = "AuthorDtoBuilder", toBuilder = true)
public class AuthorDto {

    private String name;
    private String platform;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AuthorDtoBuilder {}
}
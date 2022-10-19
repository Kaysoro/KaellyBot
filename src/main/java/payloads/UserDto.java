package payloads;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = UserDto.UserDtoBuilder.class)
@Builder(builderClassName = "UserDtoBuilder", toBuilder = true)
public class UserDto {

    String name;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserDtoBuilder {}
}
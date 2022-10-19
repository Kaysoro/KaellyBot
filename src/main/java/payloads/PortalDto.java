package payloads;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@JsonDeserialize(builder = PortalDto.PortalDtoBuilder.class)
@Builder(builderClassName = "PortalDtoBuilder", toBuilder = true)
public class PortalDto {

    String server;
    String dimension;
    int remainingUses;
    PositionDto position;
    UserDto createdBy;
    Instant createdAt;
    UserDto updatedBy;
    Instant updatedAt;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PortalDtoBuilder {}
}
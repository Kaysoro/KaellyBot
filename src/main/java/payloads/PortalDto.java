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
    private String dimension;
    private PositionDto position;
    private Boolean isAvailable;
    private Integer utilisation;
    private Instant creationDate;
    private AuthorDto creationAuthor;
    private Instant lastUpdateDate;
    private AuthorDto lastAuthorUpdate;
    private TransportDto nearestZaap;
    private TransportDto nearestTransportLimited;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PortalDtoBuilder {}
}
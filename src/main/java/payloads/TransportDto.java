package payloads;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = TransportDto.TransportDtoBuilder.class)
@Builder(builderClassName = "TransportDtoBuilder", toBuilder = true)
public class TransportDto {
    private String type;
    private String area;
    private String subArea;
    private PositionDto position;
    private boolean availableUnderConditions;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransportDtoBuilder {}

    @Override
    public String toString(){
        return area + " (" + subArea + ") **" + position.toString() + "**";
    }
}
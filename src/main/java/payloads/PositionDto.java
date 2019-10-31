package payloads;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@JsonDeserialize(builder = PositionDto.PositionDtoBuilder.class)
@Builder(builderClassName = "PositionDtoBuilder", toBuilder = true)
public class PositionDto {
    private @NotNull Integer x;
    private @NotNull Integer y;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PositionDtoBuilder {}

    @Override
    public String toString(){
        return "[" + x + ", " + y + "]";
    }
}

package payloads;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
@Value
@JsonDeserialize(builder = TweetDto.TweetDtoBuilder.class)
@Builder(builderClassName = "TweetDtoBuilder", toBuilder = true)
public class TweetDto {
    String url;
    Instant createdAt;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TweetDtoBuilder {}
}

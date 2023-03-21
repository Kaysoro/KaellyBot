package util.twitter.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class TweetData {
    private String id;
    private String text;
    @JsonProperty("created_at")
    private Instant createdAt;
    private Entities entities;
    @JsonProperty("author_id")
    private String authorId;
}
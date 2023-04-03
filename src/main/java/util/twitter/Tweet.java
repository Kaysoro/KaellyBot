package util.twitter;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Tweet {

    private String text;
    private String url;
    private String mediaUrl;
    private Instant createdAt;
}

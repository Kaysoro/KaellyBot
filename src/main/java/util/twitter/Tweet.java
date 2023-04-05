package util.twitter;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Tweet {

    private String url;
    private Instant createdAt;
}

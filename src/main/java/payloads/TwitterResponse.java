package payloads;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class TwitterResponse {
    private User author;
    private List<Tweet> tweets;

    @Data
    @Builder
    public static class Tweet {
        private String url;
        private Instant createdAt;
    }

    @Data
    @Builder
    public static class User {
        private String id;
        private String screenName;
    }
}

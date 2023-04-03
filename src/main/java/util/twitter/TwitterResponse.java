package util.twitter;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TwitterResponse {

    private User author;
    private List<Tweet> tweets;
}

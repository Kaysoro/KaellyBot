package util.twitter.payload;

import lombok.Data;

import java.util.List;

@Data
public class TwitterResponse {
    private List<TweetData> data;
    private Includes includes;
}

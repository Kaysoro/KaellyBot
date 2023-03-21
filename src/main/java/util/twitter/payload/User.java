package util.twitter.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    private String id;
    private String username;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
}

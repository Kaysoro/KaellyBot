package util.twitter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String id;
    private String name;
    private String screenName;
    private String iconUrl;
    private String url;
}

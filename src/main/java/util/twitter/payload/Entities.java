package util.twitter.payload;

import lombok.Data;

import java.util.List;

@Data
public class Entities {
    private List<MediaEntity> media;
}

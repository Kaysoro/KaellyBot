package util.twitter.payload;

import lombok.Data;

import java.util.List;

@Data
public class Includes {
    private List<User> users;
}
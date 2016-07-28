package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve on 28/07/2016.
 */
public class User {
    public static int RIGHT_INVITE = 0;
    public static int RIGHT_MEMBER = 1;
    public static int RIGHT_MODERATOR = 2;
    public static int RIGHT_ADMIN = 3;

    private static Map<String, Map<String, User>> users;
    private String id;
    private int rights;
    private String guild;

    public User(String id, int rights, String guild){
        this.id = id;
        this.rights = rights;
        this.guild = guild;
    }

    public void addToDatabase(){
        getUsers(guild).put(id, this);

        //TODO SQL Part
    }

    public void changeRight(int right){
        this.rights = right;

        //TODO SQL Part
    }

    public static Map<String, User> getUsers(String guild){
        if (users == null){
            users = new HashMap<String, Map<String, User>>();

            //TODO SQL Part
        }
        return users.get(guild);
    }

    public int getRights() {
        return rights;
    }

    public String getGuild() {
        return guild;
    }

    public String getId() {
        return id;
    }
}

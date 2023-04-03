import data.Constants;
import org.slf4j.LoggerFactory;
import util.ClientConfig;

/**
 * Created by steve on 14/07/2016.
 */
public class Main {

    public static void main(String[] args) {
        LoggerFactory.getLogger(Main.class).info("=======================================================");
        LoggerFactory.getLogger(Main.class).info("               " + Constants.name + " v" + Constants.version
                + " for " + Constants.game.getName());
        LoggerFactory.getLogger(Main.class).info("=======================================================");

        ClientConfig.getInstance().loginDiscord();
    }
}

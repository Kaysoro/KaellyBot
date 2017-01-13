import commands.*;
import data.Constants;
import junit.framework.TestCase;

import java.util.regex.Pattern;

/**
 * Created by steve on 08/11/2016.
 */
public class CommandPatternTest extends TestCase {

    public void testRSSCommand(){
        Pattern pattern = new RSSCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "rss").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss -rm").find());
    }

    public void testAlmanaxCommand(){
        Pattern pattern = new AlmanaxCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax -b").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax -o").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax 20/02/2016").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax -b 20/02/2016").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax -o 20/02/2016").find());
    }

    public void testGitCommand(){
        Pattern pattern = new GitCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "git").find());
    }

    public void testHelpCommand(){
        Pattern pattern = new HelpCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help hélp").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help !help").find());
    }

    public void testItemCommand(){
        Pattern pattern = new ItemCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "item test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "item tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "item").find());
    }

    public void testJobCommand(){
        Pattern pattern = new JobCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "job job").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job job 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job job 78").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job job 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job pêcheur 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job -all 200").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "job").find());
    }

    public void testMapCommand(){
        Pattern pattern = new MapCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "map").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map un deux trois").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map un deûx trôis").find());
    }

    public void testParrotCommand(){
        Pattern pattern = new ParrotCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "parrot test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "parrot").find());
    }

    public void testPortalCommand(){
        Pattern pattern = new PortalCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "pos").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos -reset dimension").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos -update").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimensiôn").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension [1,-1]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension -1 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension 1,-1 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension [1,-1] 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension 1").find());
    }

    public void testRightCommand(){
        Pattern pattern = new RightCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "right").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@!1234>").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@&1234>").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@!1234> 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@&1234> 1").find());
    }
}

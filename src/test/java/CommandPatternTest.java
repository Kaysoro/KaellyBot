import commands.*;
import junit.framework.TestCase;

import java.util.regex.Pattern;

/**
 * Created by steve on 08/11/2016.
 */
public class CommandPatternTest extends TestCase {

    public void testRSSCommand(){
        Pattern pattern = new RSSCommand().getPattern();

        assertTrue(pattern.matcher("!rss").find());
        assertTrue(pattern.matcher("!rss -add http://rss.xml").find());
        assertTrue(pattern.matcher("!rss -rm 1").find());
    }

    public void testAlmanaxCommand(){
        Pattern pattern = new AlmanaxCommand().getPattern();

        assertTrue(pattern.matcher("!almanax").find());
        assertTrue(pattern.matcher("!almanax -b").find());
        assertTrue(pattern.matcher("!almanax -o").find());
        assertTrue(pattern.matcher("!almanax 20/02/2016").find());
        assertTrue(pattern.matcher("!almanax -b 20/02/2016").find());
        assertTrue(pattern.matcher("!almanax -o 20/02/2016").find());
    }

    public void testHelpCommand(){
        Pattern pattern = new HelpCommand().getPattern();

        assertTrue(pattern.matcher("!help").find());
        assertTrue(pattern.matcher("!help help").find());
        assertTrue(pattern.matcher("!help !help").find());
    }

    public void testItemCommand(){
        Pattern pattern = new ItemCommand().getPattern();

        assertTrue(pattern.matcher("!item test").find());
        assertFalse(pattern.matcher("!item").find());
    }

    public void testMapCommand(){
        Pattern pattern = new MapCommand().getPattern();

        assertTrue(pattern.matcher("!map").find());
        assertTrue(pattern.matcher("!map un deux trois").find());
    }

    public void testParrotCommand(){
        Pattern pattern = new ParrotCommand().getPattern();

        assertTrue(pattern.matcher("!parrot test").find());
        assertFalse(pattern.matcher("!parrot").find());
    }

    public void testPortalCommand(){
        Pattern pattern = new PortalCommand().getPattern();

        assertTrue(pattern.matcher("!pos").find());
        assertTrue(pattern.matcher("!pos dimension").find());
        assertTrue(pattern.matcher("!pos dimension [1,-1]").find());
        assertTrue(pattern.matcher("!pos dimension -1 1").find());
        assertTrue(pattern.matcher("!pos dimension [1,-1] 1").find());
        assertTrue(pattern.matcher("!pos dimension [1,-1] 1").find());
        assertTrue(pattern.matcher("!pos dimension 1").find());
    }

    public void testRightCommand(){
        Pattern pattern = new RightCommand().getPattern();

        assertTrue(pattern.matcher("!right <@!1234> 1").find());
        assertTrue(pattern.matcher("!right <@&1234> 1").find());
        assertFalse(pattern.matcher("!right").find());
    }
}

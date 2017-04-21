import commands.*;
import data.Constants;
import junit.framework.TestCase;

import java.util.regex.Pattern;

/**
 * Created by steve on 08/11/2016.
 */
public class CommandPatternTest extends TestCase {

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
        assertTrue(pattern.matcher(Constants.prefixCommand + "help !help2").find());
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
        assertTrue(pattern.matcher(Constants.prefixCommand + "job").find());
    }

    public void testMapCommand(){
        Pattern pattern = new MapCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "map").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map I II III").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map 1 2 3").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map i ii iii").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map 1 ii III").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "map un deûx trôis").find());
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
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@1234>").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@&1234>").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@!1234> 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@&1234> 1").find());
    }

    public void testMusicCommand(){
        Pattern pattern = new MusicCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "music https://www.youtube.com/watch?v=dQw4w9WgXcQ").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -join").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -play").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -pause").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -skip").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -shuffle").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -leave").find());
    }

    public void testNSFWAuthorizationCommand(){
        Pattern pattern = new NSFWCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "nsfw true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "nsfw on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "nsfw 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "nsfw false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "nsfw off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "nsfw 1").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "nsfw").find());
    }

    public void testTwitterCommand(){
        Pattern pattern = new TwitterCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter 1").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "twitter").find());
    }

    public void testRSSCommand(){
        Pattern pattern = new RSSCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "rss true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss 1").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "rss").find());
    }

    public void testRule34Command(){
        Pattern pattern = new Rule34Command().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "rule34").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rule34 dofus").find());
    }

    public void testSoundCommand(){
        Pattern pattern = new SoundCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "sound").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "sound dofus").find());
    }

    public void testRandomCommand(){
        Pattern pattern = new RandomCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm un deûx trôis").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "rdm ").find());
    }

    public void testWhoisCommand(){
        Pattern pattern = new WhoisCommand().getPattern();

        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test-test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test-test server").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "whois").find());
    }
}

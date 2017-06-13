import commands.*;
import data.Constants;
import junit.framework.TestCase;

import java.util.regex.Pattern;

/**
 * Created by steve on 08/11/2016.
 */
public class CommandPatternTest extends TestCase {

    // BASIC COMMANDS

    public void testAlmanaxCommand(){
        Command cmd = new AlmanaxCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax 20/02/2016").find());
    }

    public void testAboutCommand(){
        Command cmd = new AboutCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "about").find());
    }

    public void testHelpCommand(){
        Command cmd = new HelpCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help hélp").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help !help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help !help2").find());
    }

    public void testItemCommand(){
        Command cmd = new ItemCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "item test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "item tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "item").find());
    }

    public void testJobCommand(){
        Command cmd = new JobCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "job job").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job job 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job job 78").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job job 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job pêcheur 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job forgeur d'épée 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job -all 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job").find());
    }

    public void testMapCommand(){
        Command cmd = new MapCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "map").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map I II III").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map 1 2 3").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map i ii iii").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map 1 ii III").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "map un deûx trôis").find());
    }

    public void testPortalCommand(){
        Command cmd = new PortalCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

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
        Command cmd = new RightCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "right").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@!1234>").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@1234>").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@&1234>").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@!1234> 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "right <@&1234> 1").find());
    }

    public void testMusicCommand(){
        Command cmd = new MusicCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "music https://www.youtube.com/watch?v=dQw4w9WgXcQ").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -join").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -play").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -pause").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -skip").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -shuffle").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "music -leave").find());
    }

    public void testTwitterCommand(){
        Command cmd = new TwitterCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter 1").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "twitter").find());
    }

    public void testRSSCommand(){
        Command cmd = new RSSCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "rss true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss 1").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "rss").find());
    }

    public void testRule34Command(){
        Command cmd = new Rule34Command();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertFalse(pattern.matcher(Constants.prefixCommand + "rule34").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rule34 dofus").find());
    }

    public void testSoundCommand(){
        Command cmd = new SoundCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "sound").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "sound dofus").find());
    }

    public void testRandomCommand(){
        Command cmd = new RandomCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm un deûx trôis").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "rdm ").find());
    }

    public void testWhoisCommand(){
        Command cmd = new WhoisCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test-test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test-test server").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test-test EL server").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "whois").find());
    }

    public void testPrefixeCommand(){
        Command cmd = new PrefixCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertFalse(pattern.matcher(Constants.prefixCommand + "prefix").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "prefix test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "prefix !&").find());
    }

    // ADMIN COMMANDS
    public void testAdminCommand(){
        Command cmd = new AdminCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "admin").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "admin help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "admin hélp").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "admin !help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "admin !help2").find());
    }

    public void testAnnounceCommand(){
        Command cmd = new AnnounceCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "announce test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "announce -confirm test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "announce").find());
    }

    public void testTalkCommand(){
        Command cmd = new TalkCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "talk test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "talk 5681 test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "talk").find());
    }
}

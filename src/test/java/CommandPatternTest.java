import commands.admin.*;
import commands.classic.*;
import commands.config.*;
import commands.model.Command;
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
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax +9").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax 1").find());
    }

    public void testAboutCommand(){
        Command cmd = new AboutCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "about").find());
    }

    public void testCommandCommand(){
        Command cmd = new CommandCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden 1").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "cmd").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden").find());
    }

    public void testDistCommand(){
        Command cmd = new DistanceCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "dist [20,20]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "dist [20 20]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "dist [2, -20]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "dist -20 20").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "dist").find());
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
        assertTrue(pattern.matcher(Constants.prefixCommand + "item -more tést test").find());
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

    public void testLangCommand(){
        Command cmd = new LanguageCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "lang FR").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "lang -channel FR").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "lang").find());
    }

    public void testMonsterCommand(){
        Command cmd = new MonsterCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "monster test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "monster tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "monster -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "monster").find());
    }

    public void testTutorialCommand(){
        Command cmd = new TutorialCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "tuto test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "tuto tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "tuto").find());
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

    public void testSetCommand(){
        Command cmd = new SetCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "set test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "set tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "set -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "set").find());
    }

    public void testSoundCommand(){
        Command cmd = new SoundCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "sound").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "sound dofus").find());
    }

    public void testResourceCommand(){
        Command cmd = new ResourceCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "resource test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "resource tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "resource -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "resource").find());
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

    public void testAvailableCommand(){
        Command cmd = new AvailableCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "available CommandForbidden true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "available CommandForbidden on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "available CommandForbidden 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "available CommandForbidden false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "available CommandForbidden off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "available CommandForbidden 1").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "available").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "available CommandForbidden").find());
    }

    public void testTalkCommand(){
        Command cmd = new TalkCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "talk test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "talk 5681 test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "talk").find());
    }

    public void testStatCommand(){
        Command cmd = new StatCommand();
        Pattern pattern = Pattern.compile(("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$"));

        assertTrue(pattern.matcher(Constants.prefixCommand + "stat").find());
    }
}

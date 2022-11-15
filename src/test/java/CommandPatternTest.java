import commands.admin.*;
import commands.classic.*;
import commands.config.*;
import commands.model.LegacyCommand;
import data.Constants;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by steve on 08/11/2016.
 */
 class CommandPatternTest {

    // BASIC COMMANDS

    @Test
     void testAlmanaxCommand(){
        LegacyCommand cmd = new AlmanaxCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax 20/02/2016").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax +9").find());
    }

    @Test
     void testAlmanaxAutoCommand(){
        LegacyCommand cmd = new AlmanaxAutoCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax-auto true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax-auto on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax-auto 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax-auto false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax-auto off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax-auto 1").find());
    }

    @Test
     void testAlignmentCommand(){
        LegacyCommand cmd = new AlignmentCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "align").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "align bonta oeil 20").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "align bonta oeil 20 oto mustam").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "align -user 145618941615").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "align -user 145618941615 oto mustam").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "align -user 145618941615 bonta oeil 100").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "align -user 145618941615 bonta oeil 0 ilyzaelle").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "align > 20").find());
    }

    @Test
     void testAllianceCommand(){
        LegacyCommand cmd = new AllianceCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "alliance La Fratrie des Oublies").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "alliance La-Fratrie' [Oublies]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "alliance La Fratrie Oublies -serv Furye").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "alliance La-Fratrie' [Oublies] -serv Furye").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "alliance").find());
    }

    @Test
     void testAboutCommand(){
        LegacyCommand cmd = new AboutCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "about").find());
    }

    @Test
     void testCommandCommand(){
        LegacyCommand cmd = new CommandCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden 1").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "cmd").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "cmd CommandForbidden").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd Command-Forbidden true").find());
    }

    @Test
     void testDonateCommand(){
        LegacyCommand cmd = new DonateCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "donate").find());
    }

    @Test
     void testDistCommand(){
        LegacyCommand cmd = new DistanceCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "dist [20,20]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "dist [20 20]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "dist [2, -20]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "dist -20 20").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "dist").find());
    }

    @Test
     void testGuildCommand(){
        LegacyCommand cmd = new GuildCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "guild La Feuille Verte").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "guild La-Feuil' [Verte]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "guild La Feuille Verte -serv Furye").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "guild La-Feuil' [Verte] -serv Furye").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "guild").find());
    }

    @Test
     void testHelpCommand(){
        LegacyCommand cmd = new HelpCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help hélp").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help !help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help !help2").find());
    }

    @Test
     void testInviteCommand(){
        LegacyCommand cmd = new InviteCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "invite").find());
    }

    @Test
     void testItemCommand(){
        LegacyCommand cmd = new ItemCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "item test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "item tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "item -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "item").find());
    }

    @Test
     void testJobCommand(){
        LegacyCommand cmd = new JobCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "job").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job -serv furye").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job @user -serv furye").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job > 100 job1, job2, job3 -serv furye").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job job1, job2, job3 -serv furye").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "job job1, job2, job3 200 -serv furye").find());
    }

    @Test
     void testLangCommand(){
        LegacyCommand cmd = new LanguageCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "lang FR").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "lang -channel FR").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "lang").find());
    }

    @Test
     void testMapCommand(){
        LegacyCommand cmd = new MapCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "map").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map I II III").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map 1 2 3").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map i ii iii").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "map 1 ii III").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "map un deûx trôis").find());
    }

    @Test
     void testMonsterCommand(){
        LegacyCommand cmd = new MonsterCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "monster test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "monster tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "monster -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "monster").find());
    }

    @Test
     void testPortalCommand(){
        LegacyCommand cmd = new PortalCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "pos").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimensiôn").find());
    }

    @Test
     void testTutorialCommand(){
        LegacyCommand cmd = new TutorialCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "tuto test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "tuto tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "tuto").find());
    }

    @Test
     void testTwitterCommand(){
        LegacyCommand cmd = new TwitterCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "twitter 1").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "twitter").find());
    }

    @Test
     void testRSSCommand(){
        LegacyCommand cmd = new RSSCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "rss true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rss 1").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "rss").find());
    }

    @Test
     void testSetCommand(){
        LegacyCommand cmd = new SetCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "set test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "set tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "set -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "set").find());
    }

    @Test
     void testServerCommand(){
        LegacyCommand cmd = new ServerCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "server").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "server -list").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "server dofus").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "server -reset").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "server -channel -reset").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "server -channel dofus").find());
    }

    @Test
     void testResourceCommand(){
        LegacyCommand cmd = new ResourceCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "resource test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "resource tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "resource -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "resource").find());
    }

    @Test
     void testRandomCommand(){
        LegacyCommand cmd = new RandomCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm un deûx trôis").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm -dj 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm -dj 190 10").find());
    }

    @Test
     void testWhoisCommand(){
        LegacyCommand cmd = new WhoisCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test-test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test-test server").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "whois test-test EL server").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "whois -more test-test EL server").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "whois").find());
    }

    @Test
     void testPingCommand(){
        LegacyCommand cmd = new PingCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "ping").find());
    }

    @Test
     void testPrefixeCommand(){
        LegacyCommand cmd = new PrefixCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertFalse(pattern.matcher(Constants.prefixCommand + "prefix").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "prefix test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "prefix !&").find());
    }

    @Test
     void testStatCommand(){
        LegacyCommand cmd = new StatCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "stats").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "stats -g").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "stats -g 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "stats -cmd").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "stats -cmd 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "stats -hist").find());
    }
}

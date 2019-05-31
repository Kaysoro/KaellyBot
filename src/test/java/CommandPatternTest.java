import commands.admin.*;
import commands.classic.*;
import commands.config.*;
import commands.model.Command;
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
        Command cmd = new AlmanaxCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax 20/02/2016").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "almanax +9").find());
    }

    @Test
     void testAlmanaxAutoCommand(){
        Command cmd = new AlmanaxAutoCommand();
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
        Command cmd = new AlignmentCommand();
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
        Command cmd = new AllianceCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "alliance La Fratrie des Oublies").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "alliance La-Fratrie' [Oublies]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "alliance La Fratrie Oublies -serv Furye").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "alliance La-Fratrie' [Oublies] -serv Furye").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "alliance").find());
    }

    @Test
     void testAboutCommand(){
        Command cmd = new AboutCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "about").find());
    }

    @Test
     void testCommandCommand(){
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
        assertTrue(pattern.matcher(Constants.prefixCommand + "cmd Command-Forbidden true").find());
    }

    @Test
     void testDonateCommand(){
        Command cmd = new DonateCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "donate").find());
    }

    @Test
     void testDistCommand(){
        Command cmd = new DistanceCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "dist [20,20]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "dist [20 20]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "dist [2, -20]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "dist -20 20").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "dist").find());
    }

    @Test
     void testGuildCommand(){
        Command cmd = new GuildCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "guild La Feuille Verte").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "guild La-Feuil' [Verte]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "guild La Feuille Verte -serv Furye").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "guild La-Feuil' [Verte] -serv Furye").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "guild").find());
    }

    @Test
     void testHelpCommand(){
        Command cmd = new HelpCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help hélp").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help !help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "help !help2").find());
    }

    @Test
     void testInviteCommand(){
        Command cmd = new InviteCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "invite").find());
    }

    @Test
     void testItemCommand(){
        Command cmd = new ItemCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "item test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "item tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "item -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "item").find());
    }

    @Test
     void testJobCommand(){
        Command cmd = new JobCommand();
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
        Command cmd = new LanguageCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "lang FR").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "lang -channel FR").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "lang").find());
    }

    @Test
     void testMapCommand(){
        Command cmd = new MapCommand();
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
        Command cmd = new MonsterCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "monster test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "monster tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "monster -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "monster").find());
    }

    @Test
     void testPortalCommand(){
        Command cmd = new PortalCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "pos").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimensiôn").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension [1,-1]").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension -1 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension 1,-1 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension [1,-1] 1").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos dimension 1").find());
    }

    @Test
     void testPortalAutoCommand(){
        Command cmd = new PortalAutoCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "pos-auto true").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos-auto on").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos-auto 0").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos-auto false").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos-auto off").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "pos-auto 1").find());
    }

    @Test
     void testTutorialCommand(){
        Command cmd = new TutorialCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "tuto test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "tuto tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "tuto").find());
    }

    @Test
     void testTwitterCommand(){
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

    @Test
     void testRSSCommand(){
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

    @Test
     void testSetCommand(){
        Command cmd = new SetCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "set test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "set tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "set -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "set").find());
    }

    @Test
     void testServerCommand(){
        Command cmd = new ServerCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "server").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "server -list").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "server dofus").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "server -reset").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "server -channel -reset").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "server -channel dofus").find());
    }

    @Test
     void testSoundCommand(){
        Command cmd = new SoundCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "sound").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "sound dofus").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "sound -leave").find());
    }

    @Test
     void testResourceCommand(){
        Command cmd = new ResourceCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "resource test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "resource tést test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "resource -more tést test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "resource").find());
    }

    @Test
     void testRandomCommand(){
        Command cmd = new RandomCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm un deûx trôis").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm -dj 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "rdm -dj 190 10").find());
    }

    @Test
     void testWhoisCommand(){
        Command cmd = new WhoisCommand();
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
        Command cmd = new PingCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "ping").find());
    }

    @Test
     void testPrefixeCommand(){
        Command cmd = new PrefixCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertFalse(pattern.matcher(Constants.prefixCommand + "prefix").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "prefix test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "prefix !&").find());
    }

    @Test
    // ADMIN COMMANDS
     void testAdminCommand(){
        Command cmd = new AdminCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "admin").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "admin help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "admin hélp").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "admin !help").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "admin !help2").find());
    }

    @Test
     void testAnnounceCommand(){
        Command cmd = new AnnounceCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "announce test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "announce -confirm test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "announce").find());
    }

    @Test
     void testAvailableCommand(){
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
        assertTrue(pattern.matcher(Constants.prefixCommand + "available Command-Forbidden true").find());
    }

    @Test
     void testTalkCommand(){
        Command cmd = new TalkCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "talk test").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "talk 5681 test").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "talk").find());
    }

    @Test
     void testStatCommand(){
        Command cmd = new StatCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "stats").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "stats -g").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "stats -g 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "stats -cmd").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "stats -cmd 200").find());
        assertTrue(pattern.matcher(Constants.prefixCommand + "stats -hist").find());
    }

    @Test
     void testVacuumCommand(){
        Command cmd = new VacuumCommand();
        Pattern pattern = Pattern.compile("^" + Constants.prefixCommand + cmd.getName() + cmd.getPattern() + "$");

        assertTrue(pattern.matcher(Constants.prefixCommand + "vacuum").find());
        assertFalse(pattern.matcher(Constants.prefixCommand + "  ").find());
    }
}

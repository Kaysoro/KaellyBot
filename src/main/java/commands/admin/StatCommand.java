package commands.admin;

import commands.model.AbstractCommand;
import enums.Language;
import exceptions.BasicDiscordException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import util.ClientConfig;
import util.Message;
import util.Translator;

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by steve on 23/12/2017.
 */
public class StatCommand extends AbstractCommand {

    private final static int GULD_LIMIT = 10;
    private final static int CMD_LIMIT = 1;

    public StatCommand(){
        super("stats","(\\s+-g(\\s+\\d+)?|\\s+-cmd(\\s+\\d+)?|\\s+-hist)?");
        setAdmin(true);
    }

    @Override
    public void request(IMessage message, Matcher m, Language lg) {
        if (m.group(1) == null || m.group(1).replaceAll("^\\s+", "").isEmpty()){
            int totalUser = 0;
            for (IGuild guild : ClientConfig.DISCORD().getGuilds())
                totalUser += guild.getUsers().size();

            String answer = Translator.getLabel(lg, "stat.request")
                    .replace("{guilds.size}", String.valueOf(ClientConfig.DISCORD().getGuilds().size()))
                    .replace("{users.size}", String.valueOf(ClientConfig.DISCORD().getUsers().size()))
                    .replace("{users_max.size}", String.valueOf(totalUser));
            Message.sendText(message.getChannel(), answer);
        }
        else if (m.group(1).matches("\\s+-g(\\s+\\d+)?")){
            int limit = GULD_LIMIT;
            if (m.group(2) != null) limit = Integer.parseInt(m.group(2).trim());
            StringBuilder st = new StringBuilder();
            List<IGuild> guilds = getBiggestGuilds(limit);
            int ladder = 1;
            for(IGuild guild : guilds)
                st.append(ladder++).append(" : **").append(guild.getName()).append("**, ")
                        .append(guild.getTotalMemberCount()).append(" users\n");

            Message.sendText(message.getChannel(), st.toString());
        }
        else if (m.group(1).matches("\\s+-cmd(\\s+\\d+)?")){
            int limit = CMD_LIMIT;
            if (m.group(2) != null) limit = Integer.parseInt(m.group(2).trim());
            List<String> cmdCalled = getNumberCmdCalled(limit);
            //TODO
            BasicDiscordException.IN_DEVELOPPMENT.throwException(message, this, lg);
        }
        else if (m.group(1).matches("\\s+-hist"))
            Message.sendImage(message.getChannel(), getJoinTimeGuildsGraph(),
                    "stats -hist : " + Instant.now() + ".png");
    }

    /**
     *
     * @param limit nombre de guildes à afficher
     * @return Liste des limit plus grandes guildes qui utilisent kaelly
     */
    private List<IGuild> getBiggestGuilds(int limit){
        return ClientConfig.DISCORD().getGuilds().stream()
                .sorted((guild1, guild2) -> guild2.getTotalMemberCount() - guild1.getTotalMemberCount())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Graphique des arrivés des guildes utilisant kaelly
     */
    private BufferedImage getJoinTimeGuildsGraph(){
        IUser me = ClientConfig.DISCORD().getOurUser();

        List<IGuild> guilds = ClientConfig.DISCORD().getGuilds().stream()
                .sorted(Comparator.comparing(guild -> guild.getJoinTimeForUser(me)))
                .collect(Collectors.toList());

        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("data");
        int guildNumber = 1;
        for(IGuild guild : guilds)
            series.add(new Day(Date.from(guild.getJoinTimeForUser(me))), guildNumber++);
        dataSet.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Guilds join time history",
                "Date",
                "Discord guild number",
                dataSet, false, false, false);

        return chart.createBufferedImage(700, 500);
    }

    /**
     *
     * @param limit nombre de jours d'appels de commande à afficher
     * @return Liste des appels de commandes de kaelly par jour
     */
    private List<String> getNumberCmdCalled(int limit){
        List<String> result = new ArrayList<>();
        //TODO
        return result;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "stat.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + "` : " + Translator.getLabel(lg, "stat.help.detailed.1")
                + "\n`" + prefixe + name + " -g `*`n`* : " + Translator.getLabel(lg, "stat.help.detailed.2")
                + "\n`" + prefixe + name + " -cmd `*`n`* : " + Translator.getLabel(lg, "stat.help.detailed.3")
                + "\n`" + prefixe + name + " -hist` : " + Translator.getLabel(lg, "stat.help.detailed.4")+ "\n";
    }
}

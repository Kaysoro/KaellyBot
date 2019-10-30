package commands.admin;

import commands.model.AbstractCommand;
import discord4j.core.DiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import enums.Language;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import stats.CommandStatistics;
import util.ClientConfig;
import util.Reporter;
import util.Translator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by steve on 23/12/2017.
 */
public class StatCommand extends AbstractCommand {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withLocale( Locale.FRANCE )
                    .withZone(ZoneId.systemDefault());
    private final static int GULD_LIMIT = 10;
    private final static int CMD_LIMIT = 1;

    public StatCommand(){
        super("stats","(\\s+-g(\\s+\\d+)?|\\s+-cmd(\\s+\\d+)?|\\s+-hist)?");
        setAdmin(true);
    }

    @Override
    public void request(Message message, Matcher m, Language lg) {
        if (m.group(1) == null || m.group(1).replaceAll("^\\s+", "").isEmpty()){
            long totalGuild = Flux.fromIterable(ClientConfig.DISCORD())
                    .flatMap(DiscordClient::getGuilds)
                    .count().blockOptional().orElse(0L);

            int totalUser = Flux.fromIterable(ClientConfig.DISCORD())
                    .flatMap(DiscordClient::getGuilds)
                    .map(Guild::getMemberCount)
                    .map(count -> count.orElse(0))
                    .collect(Collectors.summingInt(Integer::intValue))
                    .blockOptional().orElse(0);

            String answer = Translator.getLabel(lg, "stat.request")
                    .replace("{guilds.size}", String.valueOf(totalGuild))
                    .replace("{users_max.size}", String.valueOf(totalUser));
            message.getChannel().flatMap(chan -> chan.createMessage(answer)).subscribe();
        }
        else if (m.group(1).matches("\\s+-g(\\s+\\d+)?")){
            int limit = GULD_LIMIT;
            if (m.group(2) != null) limit = Integer.parseInt(m.group(2).trim());
            StringBuilder st = new StringBuilder();
            List<discord4j.core.object.entity.Guild> guilds = getBiggestGuilds(limit);
            int ladder = 1;
            for(discord4j.core.object.entity.Guild guild : guilds)
                st.append(ladder++).append(" : **").append(guild.getName()).append("**, ")
                        .append(guild.getMemberCount()).append(" users\n");

            message.getChannel().flatMap(chan -> chan.createMessage(st.toString())).subscribe();
        }
        else if (m.group(1).matches("\\s+-cmd(\\s+\\d+)?")){
            int limit = CMD_LIMIT;
            if (m.group(3) != null) limit = Integer.parseInt(m.group(3).trim());
            final int LIMIT = limit;

            message.getChannel().flatMap(chan ->
                chan.createMessage(spec ->
                        decorateImageMessage(spec, "stats -cmd : " + Instant.now() + ".png", getNumberCmdCalledPerCmd(LIMIT)))
                        .then(chan.createMessage(spec ->
                                decorateImageMessage(spec,"stats -cmd : " + Instant.now() + ".png", getNumberCmdCalled(LIMIT))))
                        .then(chan.createMessage(spec ->
                                decorateImageMessage(spec,"stats -cmd : " + Instant.now() + ".png", getForbiddenCommandsChart())))
            ).subscribe();
        }
        else if (m.group(1).matches("\\s+-hist"))
            message.getChannel().flatMap(chan -> chan.createMessage(spec -> decorateImageMessage(spec,
                    "stats -hist : " + Instant.now() + ".png", getJoinTimeGuildsGraph())))
            .subscribe();
    }

    private void decorateImageMessage(MessageCreateSpec spec, String text, BufferedImage image){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            spec.addFile(Instant.now().toString(), is);
        } catch(Exception e){
            Reporter.report(e);
            LoggerFactory.getLogger(StatCommand.class).error("decorateImageMessage", e);
        }

        spec.setContent(text);
    }

    /**
     *
     * @param limit nombre de guildes à afficher
     * @return Liste des limit plus grandes guildes qui utilisent kaelly
     */
    private List<discord4j.core.object.entity.Guild> getBiggestGuilds(int limit){
        return Flux.fromIterable(ClientConfig.DISCORD()).flatMap(DiscordClient::getGuilds)
                .sort((guild1, guild2) -> guild2.getMemberCount().orElse(0) - guild1.getMemberCount().orElse(0))
                .take(limit)
                .collectList().block();
    }

    /**
     *
     * @return Graphique des arrivés des guildes utilisant kaelly
     */
    private BufferedImage getJoinTimeGuildsGraph(){
        List<discord4j.core.object.entity.Guild> guilds = Flux.fromIterable(ClientConfig.DISCORD())
                .flatMap(DiscordClient::getGuilds)
                .sort(Comparator.comparing(guild -> guild.getJoinTime().orElse(Instant.EPOCH)))
                .collectList().blockOptional().orElse(Collections.emptyList());

        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("data");
        int guildNumber = 1;
        for(discord4j.core.object.entity.Guild guild : guilds)
            series.addOrUpdate(new Day(Date.from(guild.getJoinTime().orElse(Instant.EPOCH))), guildNumber++);
        dataSet.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Guilds join time history",
                "Date",
                "Discord guild number",
                dataSet, false, false, false);

        return chart.createBufferedImage(1200, 700);
    }

    /**
     *
     * @param limit nombre de jours d'appels de commande réparti par commandes à afficher
     * @return Liste des appels de commandes de kaelly par jour et par commande
     */
    private BufferedImage getNumberCmdCalledPerCmd(int limit){
        long period = Duration.ofDays(limit).toMillis();
        List<CommandStatistics> stats = CommandStatistics.getStatisticsPerCommand(period);
        DefaultPieDataset dataSet = new DefaultPieDataset();
        for(CommandStatistics stat : stats)
            dataSet.setValue(stat.getCommandName() + " (" + stat.getUse() + ")", stat.getUse());

        JFreeChart chart = ChartFactory.createPieChart(
                "Commands called since " + FORMATTER.format(Instant.ofEpochMilli(System.currentTimeMillis() - period)),
                dataSet, false, false, false);

        return chart.createBufferedImage(1200, 700);
    }

    /**
     *
     * @return Graphe à propos des commandes les plus bloquées
     */
    private BufferedImage getForbiddenCommandsChart(){
        List<CommandStatistics> stats = CommandStatistics.getStatisticsPerCommandForbidden();
        DefaultPieDataset dataSet = new DefaultPieDataset();
        for(CommandStatistics stat : stats)
            dataSet.setValue(stat.getCommandName() + " (" + stat.getUse() + ")", stat.getUse());

        JFreeChart chart = ChartFactory.createPieChart(
                "Commands forbidden",
                dataSet, false, false, false);

        return chart.createBufferedImage(1200, 700);
    }

    /**
     *
     * @param limit nombre de jours d'appels de commande à afficher
     * @return Liste des appels de commandes de kaelly par jour
     */
    private BufferedImage getNumberCmdCalled(int limit){
        long period = Duration.ofDays(limit).toMillis();
        List<CommandStatistics> stats = CommandStatistics.getStatistics(period);
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("data");

        for(CommandStatistics stat : stats)
            series.addOrUpdate(new Day(stat.getDate()), stat.getUse());
        dataSet.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Commands called since " + FORMATTER.format(Instant.ofEpochMilli(System.currentTimeMillis() - period)),
                "Date",
                "Command Calls",
                dataSet, false, false, false);

        return chart.createBufferedImage(1200, 700);
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
                + "\n`" + prefixe + name + " -hist` : " + Translator.getLabel(lg, "stat.help.detailed.4") + "\n";
    }
}

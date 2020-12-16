package commands.admin;

import commands.model.AbstractCommand;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.json.GuildData;
import discord4j.discordjson.json.UserGuildData;
import enums.Language;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.LoggerFactory;
import util.ClientConfig;
import util.Reporter;
import util.Translator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by steve on 23/12/2017.
 */
public class StatCommand extends AbstractCommand {

    private final static int GULD_LIMIT = 10;

    public StatCommand(){
        super("stats","(\\s+-g(\\s+\\d+)?|\\s+-cmd(\\s+\\d+)?|\\s+-hist)?");
        setAdmin(true);
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        if (m.group(1) == null || m.group(1).replaceAll("^\\s+", "").isEmpty()){
            long totalGuild = event.getClient().getGatewayResources().getStateView().getGuildStore().count().block();
            int totalUser = event.getClient().getGatewayResources().getStateView().getGuildStore().values()
                    .map(GuildData::memberCount)
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
            List<GuildData> guilds = event.getClient().getGatewayResources().getStateView().getGuildStore().values()
                    .sort((guild1, guild2) -> guild2.memberCount() - guild1.memberCount())
                    .take(limit)
                    .collectList().block();
            int ladder = 1;
            for(GuildData guild : guilds)
                st.append(ladder++).append(" : **").append(guild.name()).append("**, ")
                        .append(guild.memberCount()).append(" users\n");

            message.getChannel().flatMap(chan -> chan.createMessage(st.toString())).subscribe();
        }
        else if (m.group(1).matches("\\s+-hist"))
            message.getChannel().flatMap(chan -> chan.createMessage(spec ->
                    decorateImageMessage(spec, getJoinTimeGuildsGraph(event))))
            .subscribe();
    }

    private void decorateImageMessage(MessageCreateSpec spec, BufferedImage image){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            spec.addFile(Instant.now().toString() + ".png", is);
        } catch(Exception e){
            Reporter.report(e);
            LoggerFactory.getLogger(StatCommand.class).error("decorateImageMessage", e);
        }
    }

    /**
     *
     * @return Graphique des arrivés des guildes utilisant kaelly
     */
    private BufferedImage getJoinTimeGuildsGraph(MessageCreateEvent event){
        List<GuildData> guilds = event.getClient().getGatewayResources().getStateView().getGuildStore().values()
                .sort(Comparator.comparing(guild -> Instant.parse(guild.joinedAt())))
                .collectList().blockOptional().orElse(Collections.emptyList());

        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("data");
        int guildNumber = 1;
        for(GuildData guild : guilds)
            series.addOrUpdate(new Day(Date.from(Instant.parse(guild.joinedAt()))), guildNumber++);
        dataSet.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Guilds join time history",
                "Date",
                "Discord guild number",
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
                + "\n`" + prefixe + name + " -hist` : " + Translator.getLabel(lg, "stat.help.detailed.4") + "\n";
    }
}

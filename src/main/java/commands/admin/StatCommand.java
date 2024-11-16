package commands.admin;

import commands.model.AbstractLegacyCommand;
import discord4j.common.store.action.read.ReadActions;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateFields;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.json.GuildData;
import enums.Language;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import util.Translator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by steve on 23/12/2017.
 */
@Slf4j
public class StatCommand extends AbstractLegacyCommand {

    private static final int GULD_LIMIT = 10;

    public StatCommand(){
        super("stats","(\\s+-g(\\s+\\d+)?|\\s+-cmd(\\s+\\d+)?|\\s+-hist)?");
        setAdmin(true);
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        if (m.group(1) == null || m.group(1).replaceAll("^\\s+", "").isEmpty()){
            long connectedShards = event.getClient().getGatewayResources().getShardCoordinator()
                    .getConnectedCount()
                    .blockOptional().orElse(0);
            long totalGuild = Mono.from(event.getClient().getGatewayResources().getStore()
                    .execute(ReadActions.countGuilds()))
                    .blockOptional().orElse(0L);
            long totalMembers = event.getClient().getGuilds()
                    .collect(Collectors.summingLong(Guild::getMemberCount))
                    .blockOptional().orElse(0L);

            String answer = Translator.getLabel(lg, "stat.request")
                    .replace("{shards.size}", String.valueOf(connectedShards))
                    .replace("{guilds.size}", String.valueOf(totalGuild))
                    .replace("{users_max.size}", String.valueOf(totalMembers));
            message.getChannel().flatMap(chan -> chan.createMessage(answer)).subscribe();
        }
        else if (m.group(1).matches("\\s+-g(\\s+\\d+)?")){
            int limit = GULD_LIMIT;
            if (m.group(2) != null) limit = Integer.parseInt(m.group(2).trim());
            StringBuilder st = new StringBuilder();

            List<GuildData> guilds = Flux.from(event.getClient().getGatewayResources().getStore().execute(ReadActions.getGuilds()))
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
            message.getChannel().flatMap(chan -> chan.createMessage(decorateImageMessage(getJoinTimeGuildsGraph(event))))
            .subscribe();
    }

    private MessageCreateSpec decorateImageMessage(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            return MessageCreateSpec.builder()
                    .addFile(MessageCreateFields.File.of(Instant.now().toString() + ".png", is))
                    .build();
        } catch(Exception e){
            LoggerFactory.getLogger(StatCommand.class).error("decorateImageMessage", e);
        }
        return MessageCreateSpec.builder().content("Problem during image process").build();
    }

    /**
     *
     * @return Graphique des arrivés des guildes utilisant kaelly
     */
    private BufferedImage getJoinTimeGuildsGraph(MessageCreateEvent event){

        List<GuildData> guilds = Flux.from(event.getClient().getGatewayResources().getStore().execute(ReadActions.getGuilds()))
                .sort(Comparator.comparing(guild -> DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(guild.joinedAt(), Instant::from)))
                .collectList().blockOptional().orElse(Collections.emptyList());

        log.info("Computing " + guilds.size() + " guilds history");
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("data");
        int guildNumber = 1;
        for(GuildData guild : guilds)
            series.addOrUpdate(new Day(Date.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(guild.joinedAt(), Instant::from))), guildNumber++);
        dataSet.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Guilds join time history",
                "Date",
                "Discord guild number",
                dataSet, false, false, false);

        return chart.createBufferedImage(1200, 700);
    }

    @Override
    public String help(Language lg, String prefix) {
        return "";
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return "";
    }
}

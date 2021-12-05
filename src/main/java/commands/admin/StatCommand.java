package commands.admin;

import commands.model.AbstractCommand;
import discord4j.common.store.action.read.ReadActions;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateFields;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import util.ClientConfig;
import util.Reporter;
import util.Translator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
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
            long totalGuild = Mono.from(event.getClient().getGatewayResources().getStore()
                    .execute(ReadActions.countGuilds())).block();
            long totalMembers = Mono.from(event.getClient().getGatewayResources().getStore()
                    .execute(ReadActions.countMembers())).block();
            long totalUsers = Mono.from(event.getClient().getGatewayResources().getStore()
                    .execute(ReadActions.countUsers())).block();

            String answer = Translator.getLabel(lg, "stat.request")
                    .replace("{guilds.size}", String.valueOf(totalGuild))
                    .replace("{members_max.size}", String.valueOf(totalMembers))
                    .replace("{users_max.size}", String.valueOf(totalUsers));
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
            Reporter.report(e);
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

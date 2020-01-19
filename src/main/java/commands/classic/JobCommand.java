package commands.classic;

import commands.model.AbstractCommand;
import data.Guild;
import data.JobUser;
import data.ServerDofus;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import enums.Job;
import enums.Language;
import exceptions.BasicDiscordException;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import util.ServerUtils;
import util.Translator;

import java.text.Normalizer;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by steve on 14/07/2016.
 */
public class JobCommand extends AbstractCommand {

    private final static int MAX_JOB_DISPLAY = 3;

    private DiscordException notFoundServer;

    public JobCommand(){
        super("job", "(.*)");
        setUsableInMP(false);
        notFoundServer = new NotFoundDiscordException("server");
    }

    @Override
    protected void request(Message message, Matcher m, Language lg) {
        String content = m.group(1).trim().replaceAll(",", "");

        // Filter Initialisation
        Optional<discord4j.core.object.entity.Guild> guild = message.getGuild().blockOptional();
        Optional<Member> user = message.getAuthorAsMember().blockOptional();

        if (guild.isPresent() && user.isPresent()) {
            ServerDofus server = Guild.getGuild(guild.get()).getServerDofus();

            // Concerned user is the author?
            if (Pattern.compile("^<@[!|&]?\\d+>").matcher(content).find()) {
                content = content.replaceFirst("<@[!|&]?\\d+>", "").trim();
                Optional<Snowflake> memberId = message.getUserMentionIds().stream().findFirst();
                if (!memberId.isPresent()) {
                    BasicDiscordException.USER_NEEDED.throwException(message, this, lg);
                    return;
                }
                user = guild.get().getMemberById(memberId.get()).blockOptional();
            }

            // Is the server specified ?
            if (Pattern.compile("\\s*-serv\\s+").matcher(content).find()) {
                String[] split = content.split("\\s*-serv\\s+");
                content = split[0];
                ServerUtils.ServerQuery serverQuery = ServerUtils.getServerDofusFromName(split[1]);
                if (serverQuery.hasSucceed())
                    server = serverQuery.getServer();
                else {
                    serverQuery.getExceptions()
                            .forEach(e -> e.throwException(message, this, lg, serverQuery.getServersFound()));
                    return;
                }
            }

            if (server == null) {
                notFoundServer.throwException(message, this, lg);
                return;
            }

            //user data consultation
            if (content.isEmpty()) {
                List<Consumer<EmbedCreateSpec>> embeds = JobUser.getJobsFromUser(user.get(), server, lg);
                for (Consumer<EmbedCreateSpec> embed : embeds)
                    message.getChannel().flatMap(chan -> chan.createEmbed(embed)).subscribe();
            }
            // Data recording
            else if ((m = Pattern.compile("-list|(-all|\\p{L}+(?:\\s+\\p{L}+)*)\\s+(\\d{1,3})").matcher(content)).matches()) {
                if (user.isPresent() && message.getAuthor().isPresent() && user.get().getId().equals(message.getAuthor().get().getId())) {
                    if (!m.group(0).equals("-list")) {
                        // Data Parsing and exceptions processing
                        Set<Job> jobs;
                        StringBuilder found = new StringBuilder();
                        StringBuilder notFound = new StringBuilder();
                        StringBuilder tooMuch = new StringBuilder();
                        if (!m.group(1).equals("-all")) {
                            jobs = new HashSet<>();
                            String[] proposals = m.group(1).split("\\s+");
                            for (String proposal : proposals)
                                if (!proposal.trim().isEmpty()) {
                                    List<Job> tmp = getJob(lg, proposal);
                                    if (tmp.size() == 1) {
                                        jobs.add(tmp.get(0));
                                        found.append(tmp.get(0).getLabel(lg)).append(", ");
                                    } else if (tmp.isEmpty())
                                        notFound.append("*").append(proposal).append("*, ");
                                    else
                                        tooMuch.append("*").append(proposal).append("*, ");
                                }
                        } else
                            jobs = new HashSet<>(Arrays.asList(Job.values()));

                        // Check existing jobs
                        if (jobs.isEmpty()) {
                            message.getChannel().flatMap(chan -> chan.createMessage(Translator
                                    .getLabel(lg, "job.noone"))).subscribe();
                            return;
                        }

                        if (found.length() > 0)
                            found.setLength(found.length() - 2);
                        if (notFound.length() > 0)
                            notFound.setLength(notFound.length() - 2);
                        if (tooMuch.length() > 0)
                            tooMuch.setLength(tooMuch.length() - 2);

                        int level = Integer.parseInt(m.group(2));

                        for (Job job : jobs)
                            if (JobUser.containsKeys(user.get().getId().asLong(), server, job))
                                JobUser.get(user.get().getId().asLong(), server, job).get(0).setLevel(level);
                            else
                                new JobUser(user.get().getId().asLong(), server, job, level).addToDatabase();

                        StringBuilder sb = new StringBuilder();

                        if (jobs.size() < Job.values().length)
                            sb.append(Translator.getLabel(lg, level > 0 ? "job.save" : "job.reset")
                                    .replace("{jobs}", found.toString()));
                        else
                            sb.append(Translator.getLabel(lg, level > 0 ? "job.all_save" : "job.all_reset"));

                        if (notFound.length() > 0)
                            sb.append("\n").append(Translator.getLabel(lg, "job.not_found")
                                    .replace("{jobs}", notFound.toString()));
                        if (tooMuch.length() > 0)
                            sb.append("\n").append(Translator.getLabel(lg, "job.too_much")
                                    .replace("{jobs}", tooMuch.toString()));

                        final String CONTENT = sb.toString();
                        message.getChannel().flatMap(chan -> chan.createMessage(CONTENT)).subscribe();
                    } else {
                        String sb = Translator.getLabel(lg, "job.list") + "\n" + getJobsList(lg);
                        message.getChannel().flatMap(chan -> chan.createMessage(sb)).subscribe();
                    }
                } else
                    badUse.throwException(message, this, lg);
            } else if ((m = Pattern.compile("(?:>\\s*(\\d{1,3})\\s+)?(\\p{L}+(?:\\s+\\p{L}+)*)").matcher(content)).matches()) {
                List<String> proposals = new LinkedList<>(Arrays.asList(m.group(2).split("\\s+")));

                Set<Job> jobs = new HashSet<>();

                for (String proposal : proposals)
                    if (jobs.size() < MAX_JOB_DISPLAY) {
                        if (!proposal.trim().isEmpty()) {
                            List<Job> tmp = getJob(lg, proposal);
                            if (tmp.size() == 1) jobs.add(tmp.get(0));
                        }
                    }

                // Check if a job is found
                if (jobs.isEmpty()) {
                    message.getChannel().flatMap(chan -> chan.createMessage(Translator.getLabel(lg, "job.noone")))
                            .subscribe();
                }

                int level = -1;
                if (m.group(1) != null) level = Integer.parseInt(m.group(1));

                List<Consumer<EmbedCreateSpec>> embeds = JobUser.getJobsFromFilters(guild.get().getMembers()
                                .collectList().blockOptional().orElse(Collections.emptyList()),
                        server, jobs, level, guild.get(), lg);

                for (Consumer<EmbedCreateSpec> embed : embeds)
                    message.getChannel().flatMap(chan -> chan.createEmbed(embed)).subscribe();
            } else
                badUse.throwException(message, this, lg);
        }
    }

    private List<Job> getJob(Language lg, String nameProposed){
        nameProposed = Normalizer.normalize(nameProposed, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        nameProposed = nameProposed.replaceAll("\\W+", "");
        List<Job> jobs = new ArrayList<>();

        for(Job job : Job.values()) {
            String jobLabel = Normalizer.normalize(job.getLabel(lg), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase().replaceAll("\\W+", "");

            if (jobLabel.equals(nameProposed))
                return Collections.singletonList(job);

            if (jobLabel.startsWith(nameProposed))
                jobs.add(job);
        }
        return jobs;
    }

    private String getJobsList(Language lg) {
        final List<String> JOBS = Arrays.stream(Job.values())
                .map(job -> job.getLabel(lg)).sorted().collect(Collectors.toList());
        final long SIZE_MAX = JOBS.stream().map(String::length).max(Integer::compareTo).orElse(20);

        StringBuilder sb = new StringBuilder("```");
        JOBS.forEach(jobName -> sb.append(String.format("%-" + SIZE_MAX + "s",
                Normalizer.normalize(jobName, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .toLowerCase().replaceAll("\\W+", ""))).append("\t"));
        sb.append("```");
        return sb.toString();
    }

    @Override
    public String help(Language lg, String prefix) {
        return "**" + prefix + name + "** " + Translator.getLabel(lg, "job.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefix) {
        return help(lg, prefix)
                + "\n`" + prefix + name + " `*`-serv server`* : " + Translator.getLabel(lg, "job.help.detailed.1")
                + "\n`" + prefix + name + " `*`@user -serv server`* : " + Translator.getLabel(lg, "job.help.detailed.2")
                + "\n`" + prefix + name + " `*`job1 job2 job3 -serv server`* : " + Translator.getLabel(lg, "job.help.detailed.3")
                + "\n`" + prefix + name + " > `*`level job1 job2 job3 -serv server`* : " + Translator.getLabel(lg, "job.help.detailed.4")
                + "\n`" + prefix + name + " `*`job1, job2 job3 level -serv server`* : " + Translator.getLabel(lg, "job.help.detailed.5")
                + "\n`" + prefix + name + " -all `*`level -serv server`* : " + Translator.getLabel(lg, "job.help.detailed.6")
                + "\n`" + prefix + name + " -list` : " + Translator.getLabel(lg, "job.help.detailed.7") + "\n";
    }
}
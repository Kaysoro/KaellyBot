package commands.classic;

import commands.model.AbstractCommand;
import data.Guild;
import data.JobUser;
import data.ServerDofus;
import enums.Job;
import enums.Language;
import exceptions.BasicDiscordException;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;
import util.Message;
import sx.blah.discord.handle.obj.IMessage;
import util.ServerUtils;
import util.Translator;

import java.text.Normalizer;
import java.util.*;
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
    protected void request(IMessage message, Matcher m, Language lg) {
        String content = m.group(1).trim().replaceAll(",", "");

        // Filter Initialisation
        IUser user = message.getAuthor();
        ServerDofus server = Guild.getGuild(message.getGuild()).getServerDofus();

        // Concerned user is the author?
        if(Pattern.compile("^<@[!|&]?\\d+>").matcher(content).find()){
            content = content.replaceFirst("<@[!|&]?\\d+>", "").trim();
            if (message.getMentions().isEmpty()){
                BasicDiscordException.USER_NEEDED.throwException(message, this, lg);
                return;
            }
            user = message.getMentions().get(0);
        }

        //user data consultation
        ServerUtils.ServerQuery serverQuery = ServerUtils.getServerDofusFromName(content);
        if (! serverQuery.getServersFound().isEmpty() && Pattern.compile("(.+)").matcher(content).matches()
                || content.isEmpty()){
            if (serverQuery.hasSucceed())
                server = serverQuery.getServer();
            else if (server == null) {
                if (!content.isEmpty())
                    serverQuery.getExceptions()
                            .forEach(e -> e.throwException(message, this, lg, serverQuery.getServersFound()));
                else
                    notFoundServer.throwException(message, this, lg);
                return;
            }

            List<EmbedObject> embeds = JobUser.getJobsFromUser(user, server, message.getGuild(), lg);
            for(EmbedObject embed : embeds)
                Message.sendEmbed(message.getChannel(), embed);
        }
        // Data recording
        else if((m = Pattern.compile("-list|(-all|\\p{L}+(?:\\s+\\p{L}+)*)\\s+(\\d{1,3})(\\s+.+)?").matcher(content)).matches()) {
            if (user == message.getAuthor()) {
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
                        Message.sendText(message.getChannel(), Translator.getLabel(lg, "job.noone"));
                        return;
                    }

                    if (found.length() > 0)
                        found.setLength(found.length() - 2);
                    if (notFound.length() > 0)
                        notFound.setLength(notFound.length() - 2);
                    if (tooMuch.length() > 0)
                        tooMuch.setLength(tooMuch.length() - 2);

                    int level = Integer.parseInt(m.group(2));

                    if (m.group(3) != null) {
                        ServerUtils.ServerQuery query = ServerUtils.getServerDofusFromName(m.group(3));
                        if (serverQuery.hasSucceed())
                            server = serverQuery.getServer();
                        else
                            serverQuery.getExceptions()
                                    .forEach(e -> e.throwException(message, this, lg, query.getServersFound()));
                    } else if (server == null) {
                        notFoundServer.throwException(message, this, lg);
                        return;
                    }

                    for (Job job : jobs)
                        if (JobUser.containsKeys(user.getLongID(), server, job))
                            JobUser.get(user.getLongID(), server, job).get(0).setLevel(level);
                        else
                            new JobUser(user.getLongID(), server, job, level).addToDatabase();

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

                    Message.sendText(message.getChannel(), sb.toString());
                } else {
                    String sb = Translator.getLabel(lg, "job.list") + "\n" + getJobsList(lg);
                    Message.sendText(message.getChannel(), sb);
                }
            } else
                badUse.throwException(message, this, lg);
        }
        else if ((m = Pattern.compile("(?:>\\s*(\\d{1,3})\\s+)?(\\p{L}+(?:\\s+\\p{L}+)*)").matcher(content)).matches()){
            List<String> proposals = new LinkedList<>(Arrays.asList(m.group(2).split("\\s+")));

            if (proposals.size() > 1) {
                String potentialServer = proposals.get(proposals.size() - 1);
                ServerUtils.ServerQuery query = ServerUtils.getServerDofusFromName(potentialServer);
                if (serverQuery.hasSucceed()){
                    server = serverQuery.getServer();
                    proposals.remove(potentialServer);
                }
                else {
                    serverQuery.getExceptions()
                            .forEach(e -> e.throwException(message, this, lg, query.getServersFound()));
                    return;
                }
            }
            else if (server == null){
                notFoundServer.throwException(message, this, lg);
                return;
            }

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
                Message.sendText(message.getChannel(), Translator.getLabel(lg, "job.noone"));
                return;
            }

            int level = -1;
            if (m.group(1) != null) level = Integer.parseInt(m.group(1));

            List<EmbedObject> embeds = JobUser.getJobsFromFilters(message.getGuild().getUsers(), server,
                    jobs, level, message.getGuild(), lg);

            for(EmbedObject embed : embeds)
                Message.sendEmbed(message.getChannel(), embed);
        }
        else
            badUse.throwException(message, this, lg);
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
        JOBS.forEach(jobName -> sb.append(String.format("%-" + SIZE_MAX + "s", jobName)).append("\t"));
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
                + "\n`" + prefix + name + " `*`server`* : " + Translator.getLabel(lg, "job.help.detailed.1")
                + "\n`" + prefix + name + " `*`@user server`* : " + Translator.getLabel(lg, "job.help.detailed.2")
                + "\n`" + prefix + name + " `*`job1 job2 job3 server`* : " + Translator.getLabel(lg, "job.help.detailed.3")
                + "\n`" + prefix + name + " > `*`level job1 job2 job3 server`* : " + Translator.getLabel(lg, "job.help.detailed.4")
                + "\n`" + prefix + name + " `*`job1, job2 job3 level server`* : " + Translator.getLabel(lg, "job.help.detailed.5")
                + "\n`" + prefix + name + " -all `*`level server`* : " + Translator.getLabel(lg, "job.help.detailed.6")
                + "\n`" + prefix + name + " -list` : " + Translator.getLabel(lg, "job.help.detailed.7") + "\n";
    }
}
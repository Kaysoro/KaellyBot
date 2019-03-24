package commands.classic;

import commands.model.FetchCommand;
import data.Guild;
import data.JobUser;
import data.ServerDofus;
import enums.Job;
import enums.Language;
import exceptions.BasicDiscordException;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;
import util.Message;
import sx.blah.discord.handle.obj.IMessage;
import util.Translator;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class JobCommand extends FetchCommand {

    private final static int MAX_JOB_DISPLAY = 3;

    public JobCommand(){
        super("job", "(.*)");
        setUsableInMP(false);
    }

    @Override
    protected void request(IMessage message, Matcher m, Language lg) {
        String content = m.group(1).trim().replaceAll(",", "");

        // Initialisation du Filtre
        IUser user = message.getAuthor();
        ServerDofus server = Guild.getGuild(message.getGuild()).getServerDofus();
        List<ServerDofus> servers;

        // L'utilisateur concerné est-il l'auteur de la commande ?
        if(Pattern.compile("^<@[!|&]?\\d+>").matcher(content).find()){
            content = content.replaceFirst("<@[!|&]?\\d+>", "").trim();
            if (message.getMentions().isEmpty()){
                BasicDiscordException.USER_NEEDED.throwException(message, this, lg);
                return;
            }
            user = message.getMentions().get(0);
        }

        //Consultation des données filtrés par utilisateur
        if (!findServer(content).isEmpty() && Pattern.compile("(.+)").matcher(content).matches()
                || content.isEmpty()){
            boolean found = (m = Pattern.compile("(.+)").matcher(content)).matches();
            if (found) {
                servers = findServer(m.group(1));
                if (checkData(servers, tooMuchServers, notFoundServer, message, lg)) return;
                server = servers.get(0);
            } else if (server == null){
                notFoundServer.throwException(message, this, lg);
                return;
            }

            List<EmbedObject> embeds = JobUser.getJobsFromUser(user, server, message.getGuild(), lg);
            for(EmbedObject embed : embeds)
                Message.sendEmbed(message.getChannel(), embed);
        }
        // Enregistrement des données
        else if((m = Pattern.compile("(-all|\\p{L}+(?:\\s+\\p{L}+)*)\\s+(\\d{1,3})(\\s+.+)?").matcher(content)).matches()) {
            if (user == message.getAuthor()) {
                // Parsing des données et traitement des divers exceptions
                Set<Job> jobs;
                StringBuilder found = new StringBuilder();
                StringBuilder notFound = new StringBuilder();
                StringBuilder tooMuch = new StringBuilder();
                if (! m.group(1).equals("-all")) {
                    jobs = new HashSet<>();
                    String[] proposals = m.group(1).split("\\s+");
                    for(String proposal : proposals)
                        if (!proposal.trim().isEmpty()){
                            List<Job> tmp = getJob(lg, proposal);
                            if (tmp.size() == 1) {
                                jobs.add(tmp.get(0));
                                found.append(tmp.get(0).getLabel(lg)).append(", ");
                            }
                            else if (tmp.isEmpty())
                                notFound.append("*").append(proposal).append("*, ");
                            else
                                tooMuch.append("*").append(proposal).append("*, ");
                        }
                } else
                    jobs = new HashSet<>(Arrays.asList(Job.values()));

                // Avant d'aller plus loin, on test si on a au moins un métier de trouvé
                if (jobs.isEmpty()){
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
                    servers = findServer(m.group(3));
                    if (checkData(servers, tooMuchServers, notFoundServer, message, lg)) return;
                    server = servers.get(0);
                } else if (server == null) {
                    notFoundServer.throwException(message, this, lg);
                    return;
                }

                for (Job job : jobs)
                    if (JobUser.containsKeys(user.getLongID(), server, job))
                        JobUser.get(user.getLongID(), server, job).get(0).setLevel(level);
                    else
                        new JobUser(user.getLongID(), server,job, level).addToDatabase();

                StringBuilder st = new StringBuilder();

                if (jobs.size() < Job.values().length)
                    st.append(Translator.getLabel(lg, level > 0 ? "job.save" : "job.reset")
                            .replace("{jobs}", found.toString()));
                else
                    st.append(Translator.getLabel(lg, level > 0 ? "job.all_save" : "job.all_reset"));

                if (notFound.length() > 0)
                    st.append("\n").append(Translator.getLabel(lg, "job.not_found")
                            .replace("{jobs}", notFound.toString()));
                if (tooMuch.length() > 0)
                    st.append("\n").append(Translator.getLabel(lg, "job.too_much")
                            .replace("{jobs}", tooMuch.toString()));

                Message.sendText(message.getChannel(), st.toString());
            } else
                badUse.throwException(message, this, lg);
        }
        else if ((m = Pattern.compile("(?:>\\s*(\\d{1,3})\\s+)?(\\p{L}+(?:\\s+\\p{L}+)*)").matcher(content)).matches()){
            List<String> proposals = new LinkedList<>(Arrays.asList(m.group(2).split("\\s+")));

            if (proposals.size() > 1) {
                String potentialServer = proposals.get(proposals.size() - 1);
                servers = findServer(potentialServer);
                if (servers.size() > 1) {
                    tooMuchServers.throwException(message, this, lg, servers);
                    return;
                } else if (servers.isEmpty() && server == null) {
                    notFoundServer.throwException(message, this, lg);
                    return;
                } else if (servers.size() == 1) {
                    server = servers.get(0);
                    proposals.remove(potentialServer);
                }
            }
            else if (server == null){
                notFoundServer.throwException(message, this, lg);
                return;
            }

            Set<Job> jobs = new HashSet<>();
            StringBuilder ignoredWords = new StringBuilder();

            for (String proposal : proposals)
                if (jobs.size() < MAX_JOB_DISPLAY) {
                    if (!proposal.trim().isEmpty()) {
                        List<Job> tmp = getJob(lg, proposal);
                        if (tmp.size() == 1) jobs.add(tmp.get(0));
                        else ignoredWords.append(proposal).append(", ");
                    }
                } else ignoredWords.append(proposal).append(", ");
            if (ignoredWords.length() > 0)
                ignoredWords.setLength(ignoredWords.length() - 2);

            // Avant d'aller plus loin, on test si on a au moins un métier de trouvé
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

        for(Job job : Job.values())
            if (Normalizer.normalize(job.getLabel(lg), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase().replaceAll("\\W+", "").startsWith(nameProposed))
                jobs.add(job);
        return jobs;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "job.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`server`* : " + Translator.getLabel(lg, "job.help.detailed.1")
                + "\n`" + prefixe + name + " `*`@user server`* : " + Translator.getLabel(lg, "job.help.detailed.2")
                + "\n`" + prefixe + name + " `*`job1 job2 job3 server`* : " + Translator.getLabel(lg, "job.help.detailed.3")
                + "\n`" + prefixe + name + " > `*`level job1 job2 job3 server`* : " + Translator.getLabel(lg, "job.help.detailed.4")
                + "\n`" + prefixe + name + " `*`job1, job2 job3 level server`* : " + Translator.getLabel(lg, "job.help.detailed.5")
                + "\n`" + prefixe + name + " -all `*`level server`* : " + Translator.getLabel(lg, "job.help.detailed.6") + "\n";
    }
}

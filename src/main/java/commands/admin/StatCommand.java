package commands.admin;

import commands.model.AbstractCommand;
import enums.Language;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import util.ClientConfig;
import util.Message;
import util.Translator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by steve on 23/12/2017.
 */
public class StatCommand extends AbstractCommand {

    private final static int LIMIT = 10;

    public StatCommand(){
        super("stats","(\\s+-g(\\s+\\d+)?)?");
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
            int limit = LIMIT;
            if (m.group(2) != null) limit = Integer.parseInt(m.group(2).trim());
            StringBuilder st = new StringBuilder();
            List<IGuild> guilds = getBiggestGuilds(limit);
            int ladder = 1;
            for(IGuild guild : guilds)
                st.append(ladder++).append(" : **").append(guild.getName()).append("**, ")
                        .append(guild.getTotalMemberCount()).append(" users\n");

            Message.sendText(message.getChannel(), st.toString());
        }
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

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "stat.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + "` : " + Translator.getLabel(lg, "stat.help.detailed.1")
                + "\n`" + prefixe + name + " -g `*`n`* : " + Translator.getLabel(lg, "stat.help.detailed.2") + "\n";
    }
}

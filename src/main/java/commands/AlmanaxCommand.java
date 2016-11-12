package commands;

import data.Almanax;
import data.ClientConfig;
import data.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by steve on 14/07/2016.
 */
public class AlmanaxCommand extends AbstractCommand{

    private final static Logger LOG = LoggerFactory.getLogger(AlmanaxCommand.class);
    private final static DateFormat discordToBot = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private final static DateFormat botToAlmanax = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

    public AlmanaxCommand(){
        super(Pattern.compile("almanax"),
                Pattern.compile("^(!almanax)(\\s+-[b|o])?(\\s+\\d{2}/\\d{2}/\\d{4})?$"));
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {

            Date date = new Date();

            if (m.group(3) != null){
                try {
                    date = discordToBot.parse(m.group(3));
                } catch (ParseException e) {
                    RequestBuffer.request(() -> {
                        try {
                            new MessageBuilder(ClientConfig.CLIENT())
                                    .withChannel(message.getChannel())
                                    .withContent("La date spécifiée ne correspond pas au format dd/mm/aaaa.")
                                    .build();
                        } catch (DiscordException e1) {
                            LOG.error(e1.getErrorMessage());
                        } catch (MissingPermissionsException e1) {
                            LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                        }
                        return null;
                    });
                    return false;
                }
            }

            Almanax almanax = Almanax.get(botToAlmanax.format(date));

            if (almanax != null) {
                StringBuilder st = new StringBuilder("**Almanax du ")
                        .append(discordToBot.format(date))
                        .append(" :**\n");
                LOG.info("\"" + m.group(2) + "\"");
                if (m.group(2) == null || m.group(2).matches("\\W+-b"))
                    st.append(almanax.getBonus()).append("\n");
                if (m.group(2) == null || m.group(2).matches("\\W+-o"))
                    st.append(almanax.getOffrande()).append("\n");

                RequestBuffer.request(() -> {
                    try {
                        new MessageBuilder(ClientConfig.CLIENT())
                                .withChannel(message.getChannel())
                                .withContent(st.toString())
                                .build();
                    } catch (DiscordException e) {
                        LOG.error(e.getErrorMessage());
                    } catch (MissingPermissionsException e) {
                        LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                    }
                    return null;
                });
                return false;
            }
            else {
                RequestBuffer.request(() -> {
                    try {
                        new MessageBuilder(ClientConfig.CLIENT())
                                .withChannel(message.getChannel())
                                .withContent("Impossible de trouver des informations sur l'almanax désiré.")
                                .build();
                    } catch (DiscordException e) {
                        LOG.error(e.getErrorMessage());
                    } catch (MissingPermissionsException e) {
                        LOG.warn(Constants.name + " n'a pas les permissions pour appliquer cette requête.");
                    }
                    return null;
                });
                return false;
            }
        }
        return false;
    }

    @Override
    public String help() {
        return "**!almanax** donne le bonus et l'offrande d'une date particulière.";
    }

    @Override
    public String helpDetailed() {
        return help()
                + "\n`!almanax` : donne le bonus et l'offrande du jour actuel."
                + "\n`!almanax -b` : donne uniquement le bonus du jour actuel."
                + "\n`!almanax -o` : donne uniquement l'offrande du jour actuel."
                + "\n`!almanax `*`jj/mm/aaaa`* : donne le bonus et l'offrande du jour spécifié."
                + "\n`!almanax -b `*`jj/mm/aaaa`* : donne uniquement le bonus du jour spécifié."
                + "\n`!almanax -o `*`jj/mm/aaaa`* : donne uniquement l'offrande du jour spécifié.\n";
    }
}

package data;

import discord4j.core.object.Embed;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import enums.Language;
import org.apache.commons.lang3.StringUtils;
import util.Translator;
import discord4j.rest.util.Image;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public abstract class ObjectUser {

    protected long idUser;
    protected int level;
    public ServerDofus server;

    protected long getUser(){
        return idUser;
    }

    protected int getLevel() {
        return level;
    }

    protected ServerDofus getServer() {
        return server;
    }

    protected abstract String displayLine(discord4j.core.object.entity.Guild guild, Language lg);

    protected static<T extends ObjectUser> List<EmbedCreateSpec> getPlayersList(List<T> objUsers, discord4j.core.object.entity.Guild guild,
                                                                                          ServerDofus server, Language lg, String prefix) {
        List<EmbedCreateSpec> embed = new ArrayList<>();
        // On s'occupe d'abord de générer chaque ligne de field
        if (!objUsers.isEmpty()) {
            List<List<String>> fieldsPerEmbed = new ArrayList<>();
            List<String> fields = new ArrayList<>();
            int fieldsSize = 0;
            StringBuilder field = new StringBuilder();

            for (ObjectUser objUser : objUsers) {
                String line = objUser.displayLine(guild, lg);

                // Est-ce qu'on dépasse l'embed ? Si oui, on change de collection
                if (fieldsSize + line.length() > Message.MAX_TOTAL_EMBEDS_CHARACTER_LENGTH) {
                    fieldsPerEmbed.add(fields);
                    fields = new ArrayList<>();
                    fieldsSize = 0;
                }
                // Est-ce qu'on dépasse le field ? Si oui, on change de field
                else if (field.length() + line.length() > Embed.Field.MAX_VALUE_LENGTH) {
                    fields.add(field.toString());
                    fieldsSize += field.length();
                    field.setLength(0);
                }
                field.append(line);
            }

            if (field.length() > 0)
                fields.add(field.toString());
            if (!fields.isEmpty())
                fieldsPerEmbed.add(fields);

            // Il ne reste plus qu'à les parcourir et à créer autant d'embed que nécessaire
            for (int i = 0; i < fieldsPerEmbed.size(); i++) {
                EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                        .title(Translator.getLabel(lg, prefix + ".list")
                                + (fieldsPerEmbed.size() > 1 ? " (" + (i + 1) + "/"
                                + fieldsPerEmbed.size() + ")" : ""))
                        .thumbnail(guild.getIconUrl(Image.Format.PNG).orElse(StringUtils.EMPTY))
                        .footer(server.getLabel(lg), null);
                List<String> texts = fieldsPerEmbed.get(i);
                for (int j = 0; j < texts.size(); j++) {
                    builder.addField(Translator.getLabel(lg, prefix + "." + prefix + "s")
                                    + (texts.size() > 1 ? " (" + (j + 1) + "/"
                                    + texts.size() + ")" : "") + " : ",
                            texts.get(j), true);
                }

                embed.add(builder.build());
            }
        } else
            embed.add(EmbedCreateSpec.builder()
                .thumbnail(guild.getIconUrl(Image.Format.PNG).orElse(StringUtils.EMPTY))
                .description(Translator.getLabel(lg, prefix + ".empty"))
                .footer(server.getLabel(lg), null)
                .build());

        return embed;
    }
}

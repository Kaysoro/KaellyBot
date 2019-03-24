package data;

import enums.Language;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;
import util.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    protected abstract String displayLine(IGuild guild, Language lg);

    protected static<T extends ObjectUser> List<EmbedObject> getPlayersList(List<T> objUsers, IGuild guild,
                                                                            ServerDofus server, Language lg,
                                                                            String prefix){
        List<EmbedObject> embed = new ArrayList<>();

        // On s'occupe d'abord de générer chaque ligne de field
        if (!objUsers.isEmpty()){
            List<List<String>> fieldsPerEmbed = new ArrayList<>();
            List<String> fields = new ArrayList<>();
            int fieldsSize = 0;
            StringBuilder field = new StringBuilder();

            for(ObjectUser objUser : objUsers){
                String line = objUser.displayLine(guild, lg);

                // Est-ce qu'on dépasse l'embed ? Si oui, on change de collection
                if (fieldsSize + line.length() > EmbedBuilder.MAX_CHAR_LIMIT){
                    fieldsPerEmbed.add(fields);
                    fields = new ArrayList<>();
                    fieldsSize = 0;
                }
                // Est-ce qu'on dépasse le field ? Si oui, on change de field
                else if (field.length() + line.length() > EmbedBuilder.FIELD_CONTENT_LIMIT){
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
            for(int i = 0; i < fieldsPerEmbed.size(); i++){
                EmbedBuilder builder = new EmbedBuilder()
                        .withTitle(Translator.getLabel(lg, prefix + ".list")
                                + (fieldsPerEmbed.size() > 1? " (" + (i + 1) + "/"
                                + fieldsPerEmbed.size() + ")" : ""))
                        .withThumbnail(guild.getIconURL())
                        .withColor(new Random().nextInt(16777216));

                List<String> texts = fieldsPerEmbed.get(i);
                for(int j = 0; j < texts.size(); j++){
                    builder.appendField(Translator.getLabel(lg, prefix + "." + prefix + "s")
                                    + (texts.size() > 1? " (" + (j + 1) + "/"
                                    + texts.size() + ")" : "") + " : ",
                            texts.get(j), true);
                }
                builder.withFooterText(server.getName());
                embed.add(builder.build());
            }
        }
        else
            embed.add(new EmbedBuilder()
                    .withThumbnail(guild.getIconURL())
                    .withColor(new Random().nextInt(16777216))
                    .withDescription(Translator.getLabel(lg, prefix + ".empty"))
                    .withFooterText(server.getName()).build());

        return embed;
    }
}

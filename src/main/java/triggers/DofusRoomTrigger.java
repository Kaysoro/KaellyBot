package triggers;

import data.Constants;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import enums.Language;
import org.apache.commons.lang3.StringUtils;
import triggers.model.Trigger;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DofusRoomTrigger implements Trigger {

    private final Pattern DOFUS_ROOM_REGEX = Pattern.compile(Constants.dofusRoomBuildUrl + "(\\d+)");
    @Override
    public boolean isTriggered(Message message, Language lg) {
        return message.getContent()
                .map(content -> DOFUS_ROOM_REGEX.matcher(content).find())
                .orElse(false);
    }

    @Override
    public void execute(Message message, Language lg) {
        Matcher m = DOFUS_ROOM_REGEX.matcher(message.getContent().orElse(StringUtils.EMPTY));

        while(m.find()){
            String dofusRoomBuildId = m.group(1);
            // API dofusroom TODO
            Member author = message.getAuthorAsMember().block();
            Consumer<EmbedCreateSpec> embedCreator = createEmbedCreator(dofusRoomBuildId, author);
            message.getChannel().flatMap(channel -> channel.createEmbed(embedCreator)).subscribe();
        }
    }

    private Consumer<EmbedCreateSpec> createEmbedCreator(String id, Member author){
        return embed -> embed.setTitle("DofusRoom build " + id)
                .setUrl(Constants.dofusRoomBuildUrl + id)
                .setFooter("Provided by " + author.getDisplayName(), author.getAvatarUrl());
    }
}
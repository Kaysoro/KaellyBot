package data;

import enums.Language;
import sx.blah.discord.api.internal.json.objects.EmbedObject;

/**
 * Created by steve on 15/05/2017.
 */
public interface Embedded {
    EmbedObject getEmbedObject(Language lg);
    EmbedObject getMoreEmbedObject(Language lg);
}

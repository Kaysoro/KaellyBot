package data;

import discord4j.core.spec.EmbedCreateSpec;
import enums.Language;

/**
 * Created by steve on 15/05/2017.
 */
public interface Embedded {
    EmbedCreateSpec decorateEmbedObject(Language lg);
    EmbedCreateSpec decorateMoreEmbedObject(Language lg);
}

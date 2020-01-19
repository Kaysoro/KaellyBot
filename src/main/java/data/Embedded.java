package data;

import discord4j.core.spec.EmbedCreateSpec;
import enums.Language;

/**
 * Created by steve on 15/05/2017.
 */
public interface Embedded {
    void decorateEmbedObject(EmbedCreateSpec spec, Language lg);
    void decorateMoreEmbedObject(EmbedCreateSpec spec, Language lg);
}

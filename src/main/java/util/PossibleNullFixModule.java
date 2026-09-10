package util;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ReferenceType;
import discord4j.discordjson.possible.Possible;
import discord4j.discordjson.possible.PossibleDeserializer;

import java.util.Optional;

/**
 * Discord envoie désormais explicitement {@code null} sur des champs que discord-json déclare en
 * {@code Possible<Id>} et non en {@code Possible<Optional<Id>>} (owner_id, application_id, guild_id).
 * Le désérialiseur d'origine renvoie {@code Optional.empty()} dans tous les cas, ce qui casse la
 * construction de l'objet : "class java.util.Optional cannot be cast to class discord4j.discordjson.Id".
 *
 * Ce module remplace le désérialiseur de {@code Possible} pour renvoyer {@code Possible.absent()}
 * quand le type contenu n'est pas un {@code Optional}.
 */
public class PossibleNullFixModule extends Module {

    @Override
    public String getModuleName() {
        return "PossibleNullFixModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addDeserializers(new Deserializers.Base() {
            @Override
            public JsonDeserializer<?> findReferenceDeserializer(ReferenceType type, DeserializationConfig config,
                                                                 BeanDescription beanDesc,
                                                                 TypeDeserializer contentTypeDeserializer,
                                                                 JsonDeserializer<?> contentDeserializer) {
                if (type.hasRawClass(Possible.class))
                    return new NullSafePossibleDeserializer(type, null, contentTypeDeserializer, contentDeserializer);
                return null;
            }
        });
    }

    private static class NullSafePossibleDeserializer extends PossibleDeserializer {

        private final boolean nullable;

        NullSafePossibleDeserializer(JavaType fullType, ValueInstantiator valueInstantiator,
                                     TypeDeserializer typeDeserializer, JsonDeserializer<?> deserializer) {
            super(fullType, valueInstantiator, typeDeserializer, deserializer, false);
            this.nullable = fullType.getContentType() != null
                    && fullType.getContentType().hasRawClass(Optional.class);
        }

        @Override
        protected NullSafePossibleDeserializer withResolved(TypeDeserializer typeDeserializer,
                                                            JsonDeserializer<?> deserializer) {
            return new NullSafePossibleDeserializer(_fullType, _valueInstantiator, typeDeserializer, deserializer);
        }

        @Override
        public Possible<?> getNullValue(DeserializationContext context) {
            return nullable ? Possible.of(Optional.empty()) : Possible.absent();
        }
    }
}

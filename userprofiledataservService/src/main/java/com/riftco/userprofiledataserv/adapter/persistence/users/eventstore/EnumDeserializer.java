package com.riftco.userprofiledataserv.adapter.persistence.users.eventstore;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Generic deserializer for enum types that handles null values
 * and converts string values to their corresponding enum constants.
 *
 * @param <T> Type of the enum to deserialize
 */
public class EnumDeserializer<T extends Enum<T>> extends JsonDeserializer<T> {

    private final Class<T> enumClass;

    public EnumDeserializer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        
        if (node.isNull()) {
            return null;
        }
        
        String enumValue = node.asText();
        try {
            return Enum.valueOf(enumClass, enumValue);
        } catch (IllegalArgumentException e) {
            throw new IOException("Unknown enum value '" + enumValue + "' for enum class " + enumClass.getName(), e);
        }
    }
}

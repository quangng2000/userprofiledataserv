package com.riftco.userprofiledataserv.adapter.persistence.users.eventstore;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Generic serializer for enum types that handles null values
 * and converts them to their name representation.
 *
 * @param <T> Type of the enum to serialize
 */
public class EnumSerializer<T extends Enum<T>> extends JsonSerializer<T> {

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.name());
        }
    }
}

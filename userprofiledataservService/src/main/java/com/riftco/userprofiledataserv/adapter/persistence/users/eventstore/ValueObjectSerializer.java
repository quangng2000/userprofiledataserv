package com.riftco.userprofiledataserv.adapter.persistence.users.eventstore;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.riftco.userprofiledataserv.domain.common.ValueObject;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Generic serializer for value objects that converts them to their string representation
 * using the toString() method which all value objects implement.
 *
 * @param <T> Type of the value object to serialize
 */
public class ValueObjectSerializer<T extends ValueObject> extends JsonSerializer<T> {

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        
        // All value objects have a 'value' field that contains the actual value
        try {
            Method valueGetter = value.getClass().getMethod("getValue");
            Object rawValue = valueGetter.invoke(value);
            gen.writeString(rawValue.toString());
        } catch (Exception e) {
            // Fallback to toString if getValue doesn't work
            gen.writeString(value.toString());
        }
    }
}

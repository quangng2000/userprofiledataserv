package com.riftco.userprofiledataserv.adapter.persistence.users.eventstore;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.riftco.userprofiledataserv.domain.common.ValueObject;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Generic deserializer for value objects that uses the static 'of' factory method
 * present in all value objects to recreate them from their string representation.
 *
 * @param <T> Type of the value object to deserialize
 */
public class ValueObjectDeserializer<T extends ValueObject> extends JsonDeserializer<T> {

    private final Class<T> valueObjectClass;
    
    public ValueObjectDeserializer(Class<T> valueObjectClass) {
        this.valueObjectClass = valueObjectClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        
        try {
            // Handle both simple string values and objects with a value field
            String stringValue;
            if (node.isTextual()) {
                stringValue = node.asText();
            } else if (node.has("value")) {
                stringValue = node.get("value").asText();
            } else {
                throw new IOException("Cannot deserialize value object from: " + node);
            }
            
            // All value objects have a static 'of' method for construction
            Method ofMethod = valueObjectClass.getMethod("of", String.class);
            return (T) ofMethod.invoke(null, stringValue);
        } catch (Exception e) {
            throw new IOException("Error deserializing value object: " + e.getMessage(), e);
        }
    }
}

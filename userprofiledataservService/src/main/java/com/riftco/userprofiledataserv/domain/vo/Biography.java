package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value object for user biography
 */
@Getter
@EqualsAndHashCode
public final class Biography implements ValueObject {
    private static final int MAX_LENGTH = 1000;
    private final String value;
    
    private Biography(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Biography cannot be null");
        }
        
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Biography cannot exceed " + MAX_LENGTH + " characters");
        }
        
        this.value = value;
    }
    
    public static Biography of(String value) {
        return new Biography(value);
    }
    
    public static Biography empty() {
        return new Biography("");
    }
    
    public boolean isEmpty() {
        return value.isEmpty();
    }
    
    @Override
    public String toString() {
        return value;
    }
}

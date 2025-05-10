package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value object for user location
 */
@Getter
@EqualsAndHashCode
public final class Location implements ValueObject {
    private static final int MAX_LENGTH = 200;
    private final String value;
    
    private Location(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Location cannot exceed " + MAX_LENGTH + " characters");
        }
        
        this.value = value.trim();
    }
    
    public static Location of(String value) {
        return new Location(value);
    }
    
    public static Location empty() {
        return new Location("");
    }
    
    public boolean isEmpty() {
        return value.isEmpty();
    }
    
    @Override
    public String toString() {
        return value;
    }
}

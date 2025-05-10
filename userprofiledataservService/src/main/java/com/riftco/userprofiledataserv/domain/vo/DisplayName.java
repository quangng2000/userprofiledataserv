package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value object for user display name
 */
@Getter
@EqualsAndHashCode
public final class DisplayName implements ValueObject {
    private final String value;
    
    private DisplayName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("Display name cannot exceed 100 characters");
        }
        this.value = value.trim();
    }
    
    public static DisplayName of(String value) {
        return new DisplayName(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}

package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value object for user department
 */
@Getter
@EqualsAndHashCode
public final class Department implements ValueObject {
    private static final int MAX_LENGTH = 100;
    private final String value;
    
    private Department(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Department cannot exceed " + MAX_LENGTH + " characters");
        }
        
        this.value = value.trim();
    }
    
    public static Department of(String value) {
        return new Department(value);
    }
    
    public static Department empty() {
        return new Department("");
    }
    
    public boolean isEmpty() {
        return value.isEmpty();
    }
    
    @Override
    public String toString() {
        return value;
    }
}

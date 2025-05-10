package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value object for user job title
 */
@Getter
@EqualsAndHashCode
public final class JobTitle implements ValueObject {
    private static final int MAX_LENGTH = 100;
    private final String value;
    
    private JobTitle(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Job title cannot be null");
        }
        
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Job title cannot exceed " + MAX_LENGTH + " characters");
        }
        
        this.value = value.trim();
    }
    
    public static JobTitle of(String value) {
        return new JobTitle(value);
    }
    
    public static JobTitle empty() {
        return new JobTitle("");
    }
    
    public boolean isEmpty() {
        return value.isEmpty();
    }
    
    @Override
    public String toString() {
        return value;
    }
}

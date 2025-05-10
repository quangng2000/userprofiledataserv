package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.net.URI;

/**
 * Value object for user avatar URLs
 */
@Getter
@EqualsAndHashCode
public final class AvatarUrl implements ValueObject {
    private final String value;
    
    private AvatarUrl(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Avatar URL cannot be null");
        }
        
        // Empty value is allowed (no avatar)
        if (!value.trim().isEmpty()) {
            try {
                // Validate URL format if not empty - using URI to avoid deprecation
                new URI(value).toURL();
            } catch (Exception e) {
                // Also accept relative paths like /avatars/default.png
                if (!value.startsWith("/")) {
                    throw new IllegalArgumentException("Invalid avatar URL format: " + e.getMessage());
                }
            }
        }
        
        this.value = value;
    }
    
    public static AvatarUrl of(String value) {
        return new AvatarUrl(value);
    }
    
    public static AvatarUrl empty() {
        return new AvatarUrl("");
    }
    
    public boolean isEmpty() {
        return value.isEmpty();
    }
    
    @Override
    public String toString() {
        return value;
    }
}

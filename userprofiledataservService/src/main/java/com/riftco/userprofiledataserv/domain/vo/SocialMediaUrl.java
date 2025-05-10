package com.riftco.userprofiledataserv.domain.vo;

import com.riftco.userprofiledataserv.domain.common.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Base value object for social media URLs
 */
@Getter
@EqualsAndHashCode
public abstract class SocialMediaUrl implements ValueObject {
    protected final String value;
    
    protected SocialMediaUrl(String value, String domainPattern) {
        if (value == null) {
            throw new IllegalArgumentException("Social media URL cannot be null");
        }
        
        // Empty is allowed
        if (!value.trim().isEmpty()) {
            try {
                URI uri = new URI(value);
                String host = uri.getHost();
                if (host == null || !host.contains(domainPattern)) {
                    throw new IllegalArgumentException("URL must be from " + domainPattern);
                }
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid URL format: " + e.getMessage());
            }
        }
        
        this.value = value;
    }
    
    public boolean isEmpty() {
        return value.isEmpty();
    }
    
    @Override
    public String toString() {
        return value;
    }
}

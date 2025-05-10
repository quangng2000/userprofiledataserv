package com.riftco.userprofiledataserv.domain.vo;

/**
 * Value object for LinkedIn profile URLs
 */
public final class LinkedInUrl extends SocialMediaUrl {
    private static final String DOMAIN_PATTERN = "linkedin.com";
    
    private LinkedInUrl(String value) {
        super(value, DOMAIN_PATTERN);
    }
    
    public static LinkedInUrl of(String value) {
        return new LinkedInUrl(value);
    }
    
    public static LinkedInUrl empty() {
        return new LinkedInUrl("");
    }
}

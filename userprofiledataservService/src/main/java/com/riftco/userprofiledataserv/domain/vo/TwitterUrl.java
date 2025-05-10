package com.riftco.userprofiledataserv.domain.vo;

/**
 * Value object for Twitter/X profile URLs
 */
public final class TwitterUrl extends SocialMediaUrl {
    private static final String DOMAIN_PATTERN = "twitter.com";
    
    private TwitterUrl(String value) {
        super(value, DOMAIN_PATTERN);
    }
    
    public static TwitterUrl of(String value) {
        return new TwitterUrl(value);
    }
    
    public static TwitterUrl empty() {
        return new TwitterUrl("");
    }
}

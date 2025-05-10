package com.riftco.userprofiledataserv.domain.vo;

/**
 * Value object for GitHub profile URLs
 */
public final class GitHubUrl extends SocialMediaUrl {
    private static final String DOMAIN_PATTERN = "github.com";
    
    private GitHubUrl(String value) {
        super(value, DOMAIN_PATTERN);
    }
    
    public static GitHubUrl of(String value) {
        return new GitHubUrl(value);
    }
    
    public static GitHubUrl empty() {
        return new GitHubUrl("");
    }
}

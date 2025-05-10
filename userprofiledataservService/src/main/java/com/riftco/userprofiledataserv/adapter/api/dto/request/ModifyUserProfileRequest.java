package com.riftco.userprofiledataserv.adapter.api.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Consolidated DTO for modifying user profile attributes.
 * The controller will check which fields are non-null to determine what to modify.
 */
@Getter
@Setter
public class ModifyUserProfileRequest {
    // All fields are optional - only populated fields will be updated
    private String displayName;
    
    private String avatarUrl;
    
    private String biography;
    
    private String jobTitle;
    
    private String department;
    
    private String location;
    
    private String linkedInUrl;
    
    private String twitterUrl;
    
    private String gitHubUrl;
}

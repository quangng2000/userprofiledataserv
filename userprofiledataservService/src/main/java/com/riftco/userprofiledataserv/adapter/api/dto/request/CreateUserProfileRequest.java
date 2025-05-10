package com.riftco.userprofiledataserv.adapter.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Consolidated DTO for creating a user profile
 */
@Getter
@Setter
public class CreateUserProfileRequest {
    @NotNull(message = "User ID is required")
    private UUID userId;
    
    @NotNull(message = "Tenant ID is required")
    private UUID tenantId;
    
    @NotEmpty(message = "Display name is required")
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

package com.riftco.userprofiledataserv.adapter.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for user profile data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private UUID id;
    private UUID userId;
    private UUID tenantId;
    private String displayName;
    private String avatarUrl;
    private String biography;
    private String jobTitle;
    private String department;
    private String location;
    private String linkedInUrl;
    private String twitterUrl;
    private String gitHubUrl;
    private Instant createdAt;
    private Instant updatedAt;
}

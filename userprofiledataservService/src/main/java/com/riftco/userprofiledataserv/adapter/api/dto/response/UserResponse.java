package com.riftco.userprofiledataserv.adapter.api.dto.response;

import com.riftco.userprofiledataserv.domain.UserRole;
import com.riftco.userprofiledataserv.domain.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Response DTO for user data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String contactNumber;
    private UserState state;
    private Instant createdAt;
    private Instant updatedAt;
}

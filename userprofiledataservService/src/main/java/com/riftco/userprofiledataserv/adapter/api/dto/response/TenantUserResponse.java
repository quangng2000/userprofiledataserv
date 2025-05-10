package com.riftco.userprofiledataserv.adapter.api.dto.response;

import com.riftco.userprofiledataserv.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Response DTO for tenant-user association data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantUserResponse {
    private UUID id;
    private UUID userId;
    private UUID tenantId;
    private Set<UserRole> roles;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}

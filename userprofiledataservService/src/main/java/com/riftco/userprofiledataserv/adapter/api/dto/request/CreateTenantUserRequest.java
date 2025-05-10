package com.riftco.userprofiledataserv.adapter.api.dto.request;

import com.riftco.userprofiledataserv.domain.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

/**
 * DTO for creating a tenant-user association
 */
@Getter
@Setter
public class CreateTenantUserRequest {
    @NotNull(message = "User ID is required")
    private UUID userId;
    
    @NotNull(message = "Tenant ID is required")
    private UUID tenantId;
    
    @NotNull(message = "At least one role is required")
    private Set<UserRole> roles;
}

package com.riftco.userprofiledataserv.adapter.api.dto.request;

import com.riftco.userprofiledataserv.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Consolidated DTO for modifying tenant-user association attributes.
 * The controller will check which fields are non-null to determine what to modify.
 */
@Getter
@Setter
public class ModifyTenantUserRequest {
    // All fields are optional - only populated fields will be updated
    private Set<UserRole> roles;
    
}

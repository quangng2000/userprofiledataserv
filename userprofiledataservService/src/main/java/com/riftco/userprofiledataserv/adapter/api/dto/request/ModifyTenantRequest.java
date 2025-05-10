package com.riftco.userprofiledataserv.adapter.api.dto.request;

import com.riftco.userprofiledataserv.domain.vo.TenantStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Consolidated DTO for modifying tenant attributes.
 * The controller will check which fields are non-null to determine what to modify.
 */
@Getter
@Setter
public class ModifyTenantRequest {
    // All fields are optional - only populated fields will be updated
    private String name;
    
    private String description;
    
    private TenantStatus status;
    
    private String subscriptionPlanType;
}

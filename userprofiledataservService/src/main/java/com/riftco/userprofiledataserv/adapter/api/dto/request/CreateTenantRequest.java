package com.riftco.userprofiledataserv.adapter.api.dto.request;

import com.riftco.userprofiledataserv.domain.TenantType;
import com.riftco.userprofiledataserv.domain.vo.TenantStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Consolidated DTO for creating a tenant
 */
@Getter
@Setter
public class CreateTenantRequest {
    @NotEmpty(message = "Tenant name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Tenant type is required")
    private TenantType tenantType;
    
    private TenantStatus status;  // Optional, will default to ACTIVE in service
    
    private String subscriptionPlanType;
}

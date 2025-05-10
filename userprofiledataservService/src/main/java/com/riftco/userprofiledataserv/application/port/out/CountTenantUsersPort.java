package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.vo.TenantId;

/**
 * Port for counting users associated with a tenant.
 */
public interface CountTenantUsersPort {
    
    /**
     * Counts the number of users associated with a specific tenant.
     *
     * @param tenantId The ID of the tenant
     * @return The number of users associated with the tenant
     */
    int countByTenantId(TenantId tenantId);
}

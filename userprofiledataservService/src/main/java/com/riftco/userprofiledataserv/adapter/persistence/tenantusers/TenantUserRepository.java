package com.riftco.userprofiledataserv.adapter.persistence.tenantusers;

import com.riftco.userprofiledataserv.domain.TenantUser;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TenantUserRepository {
    TenantUser save(TenantUser aggregate);

    TenantUser getByUUID(UUID uuid);

    TenantUser getByUUIDat(UUID uuid, Instant at);
    
    /**
     * Count the number of users associated with a specific tenant
     * 
     * @param tenantId The tenant ID
     * @return The count of users in the tenant
     */
    int countByTenantId(UUID tenantId);
    
    /**
     * Find all tenant users by tenant ID
     * 
     * @param tenantId The tenant ID
     * @return List of tenant users in the tenant
     */
    List<TenantUser> findByTenantId(UUID tenantId);
}

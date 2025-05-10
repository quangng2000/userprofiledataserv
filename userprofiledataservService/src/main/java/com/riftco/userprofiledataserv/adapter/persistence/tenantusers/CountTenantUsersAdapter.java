package com.riftco.userprofiledataserv.adapter.persistence.tenantusers;

import com.riftco.userprofiledataserv.application.port.out.CountTenantUsersPort;
import com.riftco.userprofiledataserv.domain.vo.TenantId;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adapter implementation for counting users in a tenant.
 * Follows the Hexagonal Architecture by implementing the port interface.
 */
@Component
public class CountTenantUsersAdapter implements CountTenantUsersPort {

    private final TenantUserRepository tenantUserRepository;

    public CountTenantUsersAdapter(TenantUserRepository tenantUserRepository) {
        this.tenantUserRepository = tenantUserRepository;
    }

    /**
     * Counts the number of users associated with a specific tenant.
     * 
     * @param tenantId The ID of the tenant
     * @return The number of users associated with the tenant
     */
    @Override
    public int countByTenantId(TenantId tenantId) {
        return tenantUserRepository.countByTenantId(UUID.fromString(tenantId.getValue()));
    }
}

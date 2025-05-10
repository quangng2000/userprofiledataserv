package com.riftco.userprofiledataserv.adapter.persistence.tenantusers;

import com.riftco.userprofiledataserv.domain.TenantUser;

import java.time.Instant;
import java.util.UUID;

public interface TenantUserRepository {
    TenantUser save(TenantUser aggregate);

    TenantUser getByUUID(UUID uuid);

    TenantUser getByUUIDat(UUID uuid, Instant at);
}

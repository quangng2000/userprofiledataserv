package com.riftco.userprofiledataserv.adapter.persistence.tenants;

import com.riftco.userprofiledataserv.domain.Tenant;

import java.time.Instant;
import java.util.UUID;

public interface TenantRepository {
    Tenant save(Tenant aggregate);

    Tenant getByUUID(UUID uuid);

    Tenant getByUUIDat(UUID uuid, Instant at);
}

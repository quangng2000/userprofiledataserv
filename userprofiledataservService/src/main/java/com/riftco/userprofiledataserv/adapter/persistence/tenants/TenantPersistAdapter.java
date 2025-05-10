package com.riftco.userprofiledataserv.adapter.persistence.tenants;

import com.riftco.userprofiledataserv.application.port.out.FindTenantPort;
import com.riftco.userprofiledataserv.application.port.out.SourceTenantPort;
import com.riftco.userprofiledataserv.domain.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TenantPersistAdapter implements SourceTenantPort, FindTenantPort {

    private final TenantRepository tenantRepository;

    @Override
    public Tenant find(UUID uuid) {
        return this.tenantRepository.getByUUID(uuid);
    }

    @Override
    public void source(Tenant tenant) {
        this.tenantRepository.save(tenant);
    }
}

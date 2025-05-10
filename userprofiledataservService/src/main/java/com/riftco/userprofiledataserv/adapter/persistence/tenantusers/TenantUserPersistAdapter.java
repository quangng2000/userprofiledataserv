package com.riftco.userprofiledataserv.adapter.persistence.tenantusers;

import com.riftco.userprofiledataserv.application.port.out.FindTenantUserPort;
import com.riftco.userprofiledataserv.application.port.out.SourceTenantUserPort;
import com.riftco.userprofiledataserv.domain.TenantUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TenantUserPersistAdapter implements SourceTenantUserPort, FindTenantUserPort {

    private final TenantUserRepository tenantUserRepository;

    @Override
    public TenantUser find(UUID uuid) {
        return this.tenantUserRepository.getByUUID(uuid);
    }

    @Override
    public void source(TenantUser tenantUser) {
        this.tenantUserRepository.save(tenantUser);
    }
}

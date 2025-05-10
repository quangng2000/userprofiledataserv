package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.TenantUser;

public interface SourceTenantUserPort {
    void source(TenantUser tenantUser);
}

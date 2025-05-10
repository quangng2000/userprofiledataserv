package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.Tenant;

public interface SourceTenantPort {
    void source(Tenant tenant);
}

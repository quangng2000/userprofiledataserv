package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.Tenant;

import java.util.UUID;

public interface FindTenantPort {
    Tenant find(UUID uuid);
}

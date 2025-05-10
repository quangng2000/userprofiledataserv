package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.TenantUser;

import java.util.UUID;

public interface FindTenantUserPort {
    TenantUser find(UUID uuid);
}

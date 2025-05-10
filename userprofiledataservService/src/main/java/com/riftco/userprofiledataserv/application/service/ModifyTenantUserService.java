package com.riftco.userprofiledataserv.application.service;

import com.riftco.userprofiledataserv.application.port.in.ModifyTenantUserUseCase;
import com.riftco.userprofiledataserv.application.port.out.FindTenantUserPort;
import com.riftco.userprofiledataserv.application.port.out.SourceTenantUserPort;
import com.riftco.userprofiledataserv.domain.TenantUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for modifying tenant-user relationships.
 * Provides transactional handling and operations for updating tenant-user information.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ModifyTenantUserService implements ModifyTenantUserUseCase {

    private final FindTenantUserPort findTenantUserPort;
    private final SourceTenantUserPort sourceTenantUserPort;

    /**
     * Changes a tenant-user's role
     *
     * @param command The command containing tenant-user ID and new role
     */
    @Override
    @Transactional
    public void changeRole(ChangeRoleCommand command) {
        log.debug("Changing role for tenant-user: {} to {}", command.getTenantUserId(), command.getRole());
        
        TenantUser tenantUser = this.findTenantUserPort.find(command.getTenantUserId());
        tenantUser.changeRole(command.getRole());
        
        this.sourceTenantUserPort.source(tenantUser);
        
        log.info("Role changed successfully for tenant-user: {} to {}", 
                command.getTenantUserId(), command.getRole());
    }
}

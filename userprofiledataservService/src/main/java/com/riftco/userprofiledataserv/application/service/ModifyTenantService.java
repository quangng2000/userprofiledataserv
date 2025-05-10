package com.riftco.userprofiledataserv.application.service;

import com.riftco.userprofiledataserv.application.port.in.ModifyTenantUseCase;
import com.riftco.userprofiledataserv.application.port.out.FindTenantPort;
import com.riftco.userprofiledataserv.application.port.out.SourceTenantPort;
import com.riftco.userprofiledataserv.domain.Tenant;
import com.riftco.userprofiledataserv.domain.vo.TenantName;
import com.riftco.userprofiledataserv.domain.vo.TenantStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for modifying existing tenants.
 * Provides transaction handling and operations for updating tenant information.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ModifyTenantService implements ModifyTenantUseCase {

    private final FindTenantPort findTenantPort;
    private final SourceTenantPort sourceTenantPort;

    /**
     * Changes a tenant's name
     *
     * @param command The command containing tenant ID and new name
     */
    @Override
    @Transactional
    public void changeName(ChangeTenantNameCommand command) {
        log.debug("Changing name for tenant: {}", command.getTenantId());
        
        Tenant tenant = this.findTenantPort.find(command.getTenantId());
        tenant.changeName(TenantName.of(command.getName()));
        
        this.sourceTenantPort.source(tenant);
        
        log.info("Name changed successfully for tenant: {}", command.getTenantId());
    }

    /**
     * Changes a tenant's description
     *
     * @param command The command containing tenant ID and new description
     */
    @Override
    @Transactional
    public void changeDescription(ChangeTenantDescriptionCommand command) {
        log.debug("Changing description for tenant: {}", command.getTenantId());
        
        Tenant tenant = this.findTenantPort.find(command.getTenantId());
        tenant.changeDescription(command.getDescription());
        
        this.sourceTenantPort.source(tenant);
        
        log.info("Description changed successfully for tenant: {}", command.getTenantId());
    }

    /**
     * Changes a tenant's status
     *
     * @param command The command containing tenant ID and new status
     */
    @Override
    @Transactional
    public void changeStatus(ChangeTenantStatusCommand command) {
        log.debug("Changing status for tenant: {} to {}", command.getTenantId(), command.getStatus());
        
        Tenant tenant = this.findTenantPort.find(command.getTenantId());
        
        // Use the appropriate public method based on the requested status
        Tenant updatedTenant;
        if (command.getStatus().equals(TenantStatus.ACTIVE)) {
            updatedTenant = tenant.activate();
        } else if (command.getStatus().equals(TenantStatus.INACTIVE)) {
            updatedTenant = tenant.deactivate();
        } else {
            throw new IllegalArgumentException("Unsupported tenant status: " + command.getStatus());
        }
        
        this.sourceTenantPort.source(updatedTenant);
        
        log.info("Status changed successfully for tenant: {} to {}", command.getTenantId(), command.getStatus());
    }
}

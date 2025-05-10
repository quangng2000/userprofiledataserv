package com.riftco.userprofiledataserv.application.service;

import com.riftco.userprofiledataserv.application.port.in.CreateTenantUserUseCase;
import com.riftco.userprofiledataserv.application.port.out.CountTenantUsersPort;
import com.riftco.userprofiledataserv.application.port.out.FindTenantPort;
import com.riftco.userprofiledataserv.application.port.out.FindUserPort;
import com.riftco.userprofiledataserv.application.port.out.SourceTenantUserPort;
import com.riftco.userprofiledataserv.domain.Tenant;
import com.riftco.userprofiledataserv.domain.TenantUser;
import com.riftco.userprofiledataserv.domain.User;
import com.riftco.userprofiledataserv.domain.vo.TenantId;
import com.riftco.userprofiledataserv.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.riftco.userprofiledataserv.domain.TenantUser.NONE;

/**
 * Service implementation for creating tenant-user relationships.
 * Provides transactional handling and validation.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CreateTenantUserService implements CreateTenantUserUseCase {

    private final FindTenantPort findTenantPort;
    private final FindUserPort findUserPort;
    private final CountTenantUsersPort countTenantUsersPort;
    private final SourceTenantUserPort sourceTenantUserPort;

    /**
     * Creates a new tenant-user relationship with the specified details.
     * 
     * @param command Contains the tenant-user relationship details
     * @throws IllegalArgumentException if the command contains invalid data
     */
    @Override
    @Transactional
    public void createTenantUser(CreateTenantUserCommand command) {
        log.debug("Creating new tenant-user relationship. Tenant: {}, User: {}", 
                command.getTenantId(), command.getUserId());
        
        // Validate input
        validateCreateTenantUserCommand(command);
        
        try {
            // Load required domain objects
            Tenant tenant = findTenantPort.find(command.getTenantId());
            UserId userId = UserId.of(command.getUserId().toString());
            
            // Check if user exists
            findUserPort.find(command.getUserId());
            
            // Count existing users in the tenant (for validation in PERSON tenant type)
            int existingUserCount = countTenantUsersPort.countByTenantId(
                    TenantId.of(command.getTenantId().toString()));
            
            // Create tenant-user domain object
            TenantUser tenantUser = NONE.create(
                    tenant,
                    userId,
                    command.getRole(),
                    existingUserCount);
            
            // Persist to event store
            this.sourceTenantUserPort.source(tenantUser);
            
            log.info("Tenant-user relationship created successfully. ID: {}", tenantUser.getUUID());
        } catch (Exception e) {
            log.error("Failed to create tenant-user relationship. Tenant: {}, User: {}", 
                    command.getTenantId(), command.getUserId(), e);
            throw new RuntimeException("Tenant-user creation failed", e);
        }
    }
    
    /**
     * Validates the create tenant-user command parameters.
     * 
     * @param command Command to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateCreateTenantUserCommand(CreateTenantUserCommand command) {
        Assert.notNull(command, "Command cannot be null");
        Assert.notNull(command.getTenantId(), "Tenant ID cannot be null");
        Assert.notNull(command.getUserId(), "User ID cannot be null");
        Assert.notNull(command.getRole(), "Role cannot be null");
    }
}

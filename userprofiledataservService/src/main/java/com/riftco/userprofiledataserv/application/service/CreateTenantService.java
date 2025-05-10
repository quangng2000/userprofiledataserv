package com.riftco.userprofiledataserv.application.service;

import com.riftco.userprofiledataserv.application.port.in.CreateTenantUseCase;
import com.riftco.userprofiledataserv.application.port.out.SourceTenantPort;
import com.riftco.userprofiledataserv.domain.Tenant;
import com.riftco.userprofiledataserv.domain.vo.SubscriptionPlan;
import com.riftco.userprofiledataserv.domain.vo.TenantId;
import com.riftco.userprofiledataserv.domain.vo.TenantName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;



import static com.riftco.userprofiledataserv.domain.Tenant.NONE;

/**
 * Service implementation for creating new tenants in the system.
 * Provides transactional handling and input validation.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CreateTenantService implements CreateTenantUseCase {

    private final SourceTenantPort sourceTenantPort;

    /**
     * Creates a new tenant with the specified details.
     * 
     * @param command Contains the tenant details for creation
     * @throws IllegalArgumentException if the command contains invalid data
     */
    @Override
    @Transactional
    public void createTenant(CreateTenantCommand command) {
        log.debug("Creating new tenant with name: {}", command.getName());
        
        // Validate input
        validateCreateTenantCommand(command);
        
        try {
            TenantId tenantId = TenantId.generate();
            TenantName tenantName = TenantName.of(command.getName());
            SubscriptionPlan subscriptionPlan;
            if (command.getSubscriptionPlanType() != null) {
                switch (command.getSubscriptionPlanType().toUpperCase()) {
                    case "BASIC":
                        subscriptionPlan = SubscriptionPlan.basicPlan();
                        break;
                    case "PROFESSIONAL":
                        subscriptionPlan = SubscriptionPlan.professionalPlan();
                        break;
                    case "ENTERPRISE":
                        subscriptionPlan = SubscriptionPlan.enterprisePlan();
                        break;
                    default:
                        subscriptionPlan = SubscriptionPlan.freePlan();
                }
            } else {
                subscriptionPlan = SubscriptionPlan.freePlan();
            }
            
            // Create tenant domain object
            Tenant tenant = NONE.create(
                    tenantId,
                    tenantName,
                    command.getTenantType(),
                    command.getDescription(),
                    subscriptionPlan);
            
            // Persist to event store
            this.sourceTenantPort.source(tenant);
            
            log.info("Tenant created successfully with id: {}", tenant.getUUID());
        } catch (Exception e) {
            log.error("Failed to create tenant with name: {}", command.getName(), e);
            throw new RuntimeException("Tenant creation failed", e);
        }
    }
    
    /**
     * Validates the create tenant command parameters.
     * 
     * @param command Command to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateCreateTenantCommand(CreateTenantCommand command) {
        Assert.notNull(command, "Command cannot be null");
        Assert.hasText(command.getName(), "Tenant name cannot be empty");
        Assert.notNull(command.getTenantType(), "Tenant type cannot be null");
        Assert.notNull(command.getStatus(), "Tenant status cannot be null");
    }
}

package com.riftco.userprofiledataserv.application.port.in;

import com.riftco.userprofiledataserv.domain.TenantType;
import com.riftco.userprofiledataserv.domain.vo.TenantStatus;
import lombok.Value;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface CreateTenantUseCase {

    void createTenant(CreateTenantCommand command);

    @Value
    class CreateTenantCommand {
        @NotEmpty
        private String name;
        
        private String description;
        
        @NotNull
        private TenantType tenantType;
        
        @NotNull
        private TenantStatus status;
        
        private String subscriptionPlanType;
    }
}

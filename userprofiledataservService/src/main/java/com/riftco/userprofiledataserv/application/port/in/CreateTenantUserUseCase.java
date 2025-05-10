package com.riftco.userprofiledataserv.application.port.in;

import com.riftco.userprofiledataserv.domain.UserRole;
import lombok.Value;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface CreateTenantUserUseCase {

    void createTenantUser(CreateTenantUserCommand command);

    @Value
    class CreateTenantUserCommand {
        @NotNull
        private UUID userId;
        
        @NotNull
        private UUID tenantId;
        
        @NotNull
        private UserRole role;
    }
}

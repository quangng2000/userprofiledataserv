package com.riftco.userprofiledataserv.application.port.in;

import com.riftco.userprofiledataserv.domain.UserRole;
import lombok.Value;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface ModifyTenantUserUseCase {

    void changeRole(ChangeRoleCommand command);

    @Value
    class ChangeRoleCommand {
        @NotNull
        private UUID tenantUserId;
        
        @NotNull
        private UserRole role;
    }
}

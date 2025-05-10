package com.riftco.userprofiledataserv.application.port.in;

import com.riftco.userprofiledataserv.domain.vo.TenantStatus;
import lombok.Value;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface ModifyTenantUseCase {

    void changeName(ChangeTenantNameCommand command);
    
    void changeDescription(ChangeTenantDescriptionCommand command);
    
    void changeStatus(ChangeTenantStatusCommand command);

    @Value
    class ChangeTenantNameCommand {
        @NotNull
        private UUID tenantId;
        
        @NotEmpty
        private String name;
    }
    
    @Value
    class ChangeTenantDescriptionCommand {
        @NotNull
        private UUID tenantId;
        
        private String description;
    }
    
    @Value
    class ChangeTenantStatusCommand {
        @NotNull
        private UUID tenantId;
        
        @NotNull
        private TenantStatus status;
    }
}

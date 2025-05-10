package com.riftco.userprofiledataserv.application.port.in;

import com.riftco.userprofiledataserv.application.SelfValidating;
import com.riftco.userprofiledataserv.domain.vo.TenantId;
import lombok.EqualsAndHashCode;
import lombok.Value;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface CreateUserUseCase {
    void createUser(CreateUserCommand command);

    @Value
    @EqualsAndHashCode(callSuper = false)
    class CreateUserCommand extends SelfValidating<CreateUserCommand> {
        @NotNull
        private TenantId tenantId;
        @NotEmpty
        private String name;
        @NotEmpty
        private String email;
        @NotEmpty
        private String contactNumber;

        public CreateUserCommand(UUID tenantUuid, String name, String email, String contactNumber) {
            this.tenantId =  TenantId.of(tenantUuid.toString());
            this.name = name;
            this.email = email;
            this.contactNumber = contactNumber;
            this.validateSelf();
        }
    }
}

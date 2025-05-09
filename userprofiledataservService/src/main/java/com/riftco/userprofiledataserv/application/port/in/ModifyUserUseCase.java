package com.riftco.userprofiledataserv.application.port.in;

import com.riftco.userprofiledataserv.application.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface ModifyUserUseCase {
    void changeName(ChangeNameUserCommand command);

    @Value
    @EqualsAndHashCode(callSuper = false)
    class ChangeNameUserCommand extends SelfValidating<ChangeNameUserCommand> {
        @NotNull
        private UUID uuid;
        @NotEmpty
        private String name;

        public ChangeNameUserCommand(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
            this.validateSelf();
        }
    }
}

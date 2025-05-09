package com.riftco.userprofiledataserv.application.port.in;

import com.riftco.userprofiledataserv.application.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import jakarta.validation.constraints.NotEmpty;

public interface CreateUserUseCase {
    void createUser(CreateUserCommand command);

    @Value
    @EqualsAndHashCode(callSuper = false)
    class CreateUserCommand extends SelfValidating<CreateUserCommand> {
        @NotEmpty
        private String name;
        @NotEmpty
        private String email;
        @NotEmpty
        private String contactNumber;

        public CreateUserCommand(String name, String email, String contactNumber) {
            this.name = name;
            this.email = email;
            this.contactNumber = contactNumber;
            this.validateSelf();
        }
    }
}

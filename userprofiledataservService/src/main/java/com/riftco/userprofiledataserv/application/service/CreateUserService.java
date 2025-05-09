package com.riftco.userprofiledataserv.application.service;

import com.riftco.userprofiledataserv.application.port.in.CreateUserUseCase;
import com.riftco.userprofiledataserv.application.port.out.SourceUserPort;
import com.riftco.userprofiledataserv.domain.User;
import com.riftco.userprofiledataserv.domain.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.riftco.userprofiledataserv.domain.User.NONE;

@RequiredArgsConstructor
@Component
public class CreateUserService implements CreateUserUseCase {

    // save (source event) to event store
    private final SourceUserPort sourceUserPort;

    @Override
    public void createUser(CreateUserCommand command) {
        User user = NONE.create(
                UserState.ACTIVATED,
                command.getName(),
                command.getEmail(),
                command.getContactNumber());
        this.sourceUserPort.source(user);
    }
}

package com.riftco.userprofiledataserv.application.service;

import com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase;
import com.riftco.userprofiledataserv.application.port.out.FindUserPort;
import com.riftco.userprofiledataserv.application.port.out.SourceUserPort;
import com.riftco.userprofiledataserv.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ModifyUserService implements ModifyUserUseCase {

    private final FindUserPort findUserPort;
    private final SourceUserPort sourceUserPort;

    @Override
    public void changeName(ChangeNameUserCommand command) {
        User user = this.findUserPort.find(command.getUuid());
        user.changeName(command.getName());
        this.sourceUserPort.source(user);
    }
}

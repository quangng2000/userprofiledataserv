package com.riftco.userprofiledataserv.adapter.persistence.users;

import com.riftco.userprofiledataserv.application.port.out.FindUserPort;
import com.riftco.userprofiledataserv.application.port.out.SourceUserPort;
import com.riftco.userprofiledataserv.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserPersistAdapter implements SourceUserPort, FindUserPort {

    private final UserRepository userRepository;

    @Override
    public User find(UUID uuid) {
        return this.userRepository.getByUUID(uuid);
    }

    @Override
    public void source(User user) {
        this.userRepository.save(user);
    }
}

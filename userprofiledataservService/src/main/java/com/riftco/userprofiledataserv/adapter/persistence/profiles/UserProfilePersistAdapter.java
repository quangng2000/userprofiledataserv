package com.riftco.userprofiledataserv.adapter.persistence.profiles;

import com.riftco.userprofiledataserv.application.port.out.FindUserProfilePort;
import com.riftco.userprofiledataserv.application.port.out.SourceUserProfilePort;
import com.riftco.userprofiledataserv.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserProfilePersistAdapter implements SourceUserProfilePort, FindUserProfilePort {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfile find(UUID uuid) {
        return this.userProfileRepository.getByUUID(uuid);
    }

    @Override
    public void source(UserProfile userProfile) {
        this.userProfileRepository.save(userProfile);
    }
}

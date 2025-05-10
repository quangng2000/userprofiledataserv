package com.riftco.userprofiledataserv.adapter.persistence.profiles;

import com.riftco.userprofiledataserv.domain.UserProfile;

import java.time.Instant;
import java.util.UUID;

public interface UserProfileRepository {
    UserProfile save(UserProfile aggregate);

    UserProfile getByUUID(UUID uuid);

    UserProfile getByUUIDat(UUID uuid, Instant at);
}

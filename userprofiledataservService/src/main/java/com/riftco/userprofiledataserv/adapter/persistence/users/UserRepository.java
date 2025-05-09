package com.riftco.userprofiledataserv.adapter.persistence.users;

import com.riftco.userprofiledataserv.domain.User;

import java.time.Instant;
import java.util.UUID;

public interface UserRepository {
    User save(User aggregate);

    User getByUUID(UUID uuid);

    User getByUUIDat(UUID uuid, Instant at);
}

package com.riftco.userprofiledataserv.application.port.out;

import com.riftco.userprofiledataserv.domain.UserProfile;

import java.util.UUID;

public interface FindUserProfilePort {
    UserProfile find(UUID uuid);
}
